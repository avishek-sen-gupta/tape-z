package com.mojo.loader.pass;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.*;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.graph.ConnectionType;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BuildFlowchartBlocksTask {
    private static final Logger logger = LoggerFactory.getLogger(BuildFlowchartBlocksTask.class);
    public static final NullTranspilerInstruction JUST_STOP = new NullTranspilerInstruction("JUST STOP");
    @Getter
    private final List<StackRecord> stack = new ArrayList<>();
    private final IdProvider idProvider;
    private int numberOfInstructionsTraced;
    private List<BasicBlock<TranspilerInstruction>> flowchartBlocks = new ArrayList<>();
    private Map<TranspilerInstruction, BasicBlock<TranspilerInstruction>> instructionToBlockMap = new HashMap<>();

    public BuildFlowchartBlocksTask(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> run(Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        Optional<TranspilerInstruction> possibleRoot = cfg.vertexSet().stream().filter(this::isRoot).findFirst();
        if (possibleRoot.isEmpty()) {
            throw new IllegalStateException("Cannot find root node");
        }

        TranspilerInstruction rootNode = possibleRoot.get();
        Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph = traceCFGLoopIteratively(rootNode, cfg);
        logger.info("COMPLETE!");
        return blockGraph;
    }

    private Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> traceCFGLoopIteratively(TranspilerInstruction root, Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        TranspilerInstruction currentNode = root;
        List<TranspilerInstruction> visited = new ArrayList<>();
        BasicBlock<TranspilerInstruction> currentBlock = new BasicBlock<>(idProvider.next());
        while (true) {
            if (visited.contains(currentNode)) {
                if (!currentBlock.isEmpty()) {
                    closeCurrentBlock(currentBlock);
                    currentBlock = startNewBlock();
                }
                logger.debug(String.format("Visited node %s already, stopping...", currentNode.ref().shortDescription()));
                TranspilerInstruction nextInstruction = endPath(cfg);
                if (nextInstruction == JUST_STOP) break;
                currentNode = nextInstruction;
                continue;
            }
            logger.debug("Interpreting " + currentNode.originalText() + "...");
            interpret(currentNode, cfg, visited);
            List<TypedGraphEdge> outgoingEdges = reachable(cfg.outgoingEdgesOf(currentNode));

            if (outgoingEdges.isEmpty()) {
                logger.debug("Reached a dead end, stopping path...");
                addToCurrentBlock(currentNode, currentBlock);
                closeCurrentBlock(currentBlock);
                currentBlock = startNewBlock();
                TranspilerInstruction nextInstruction = endPath(cfg);
                if (nextInstruction == JUST_STOP) break;
                currentNode = nextInstruction;
            } else if (currentNode.ref() instanceof SectionStartTranspilerNode || currentNode.ref() instanceof SectionEndTranspilerNode) {
                if (currentBlock.isEmpty()) {
                    addToCurrentBlock(currentNode, currentBlock);
                    closeCurrentBlock(currentBlock);
                    currentBlock = startNewBlock();
                } else {
                    closeCurrentBlock(currentBlock);
                    BasicBlock<TranspilerInstruction> sectionTerminalBlock = startNewBlock();
                    addToCurrentBlock(currentNode, sectionTerminalBlock);
                    closeCurrentBlock(sectionTerminalBlock);
                    currentBlock = startNewBlock();
                }
                TypedGraphEdge outgoingEdge = cfg.outgoingEdgesOf(currentNode).stream().findAny().get();
                currentNode = cfg.getEdgeTarget(outgoingEdge);
            } else if (outgoingEdges.size() > 1) {
                if (currentBlock.isEmpty()) {
                    addToCurrentBlock(currentNode, currentBlock);
                } else {
                    closeCurrentBlock(currentBlock);
                    BasicBlock<TranspilerInstruction> decisionBlock = startNewBlock();
                    addToCurrentBlock(currentNode, decisionBlock);
                    closeCurrentBlock(decisionBlock);
                    currentBlock = startNewBlock();
                }
                // Encountered this branch for the first time
                stack.add(new StackRecord(currentNode, 0, outgoingEdges));
                logger.debug(String.format("Discovered a decision point: ", stack.getClass()));
                currentNode = nextPathOfIncompleteParent(cfg);
                logger.debug("Stack is " + formatted(stack));
            } else {
                List<TypedGraphEdge> incomingEdges = reachable(cfg.incomingEdgesOf(currentNode));
                if (incomingEdges.size() > 1) {
                    if (currentBlock.isEmpty()) {
                        addToCurrentBlock(currentNode, currentBlock);
                        closeCurrentBlock(currentBlock);
                        currentBlock = startNewBlock();
                    } else {
                        closeCurrentBlock(currentBlock);
                        currentBlock = startNewBlock();
                        addToCurrentBlock(currentNode, currentBlock);
                    }
                } else {
                    addToCurrentBlock(currentNode, currentBlock);
                }
                TypedGraphEdge first = outgoingEdges.getFirst();
                currentNode = cfg.getEdgeTarget(first);
            }
        }
        HashSet<TranspilerInstruction> remaining = new HashSet<>(cfg.vertexSet());
        remaining.removeAll(visited);
        logger.debug("Remaining not visited==============");
        remaining.forEach(r -> logger.debug(r.ref().shortDescription()));
        logger.info(String.format("Exiting Tracing, traced %s/%s instructions...", numberOfInstructionsTraced, cfg.vertexSet().size()));
        return asBlockGraph(cfg);
    }

    private static List<TypedGraphEdge> reachable(Set<TypedGraphEdge> edges) {
        return edges.stream()
                .filter(e -> !e.getRelationshipType().equals(ConnectionType.FLOWS_AFTER_SUBROUTINE.value())
                             && !e.getRelationshipType().equals(ConnectionType.FLOWS_TO_SYNTAX_ONLY.value()))
                .toList();
    }

    private Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> asBlockGraph(Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        DirectedPseudograph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph = new DirectedPseudograph<>(TypedGraphEdge.class);
        flowchartBlocks.forEach(blockGraph::addVertex);
        flowchartBlocks.forEach(blk -> {
            List<TypedGraphEdge> reachableOutgoing = reachable(cfg.outgoingEdgesOf(blk.lastInstruction()));
            reachableOutgoing.forEach(originalEdge -> {
                TranspilerInstruction instruction = cfg.getEdgeTarget(originalEdge);
                BasicBlock<TranspilerInstruction> tblk = instructionToBlockMap.get(instruction);
                if (tblk == null) return;
                blockGraph.addEdge(blk, tblk, new TypedGraphEdge(originalEdge.getRelationshipType(), "NONAME"));
            });
        });
        return blockGraph;
    }

    private void addToCurrentBlock(TranspilerInstruction instruction, BasicBlock<TranspilerInstruction> block) {
        block.add(instruction);
        instructionToBlockMap.put(instruction, block);
    }

    private BasicBlock<TranspilerInstruction> startNewBlock() {
        return new BasicBlock<>(idProvider.next());
    }

    private void closeCurrentBlock(BasicBlock<TranspilerInstruction> currentBlock) {
        flowchartBlocks.add(currentBlock);
    }

    private TranspilerInstruction endPath(Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        if (stack.isEmpty()) {
            logger.debug("Stack is empty! Exiting...");
            return JUST_STOP;
        }

        updateOriginPathCount();
        unroll();

        if (stack.isEmpty()) {
            logger.debug("Unrolled up all the way to empty stack! Exiting...");
            return JUST_STOP;
        }
        // Unrolled up to an incomplete parent
        return nextPathOfIncompleteParent(cfg);
    }

    private TranspilerInstruction nextPathOfIncompleteParent(Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        StackRecord incompleteParent = stack.getLast();
        int nextIndex = incompleteParent.currentPathOriginIndex();
        logger.debug(String.format("Incomplete decision point, picking %s out of %s...", nextIndex + 1, incompleteParent.allPathOrigins().size()));
        TranspilerInstruction nextInstruction = cfg.getEdgeTarget(incompleteParent.allPathOrigins().get(nextIndex));
        return nextInstruction;
    }

    private void unroll() {
        while (stack.getLast().currentPathOriginIndex() == stack.getLast().allPathOrigins().size()) {
            logger.debug("Finished all branches of last stack parent, unrolling...");
            stack.remove(stack.size() - 1);
            if (stack.isEmpty()) {
                logger.debug("Completed all paths, stopping program...");
                break; // Exit completely please!
            }
            StackRecord grandParent = stack.getLast();
            stack.set(stack.size() - 1, new StackRecord(grandParent.instruction(), grandParent.currentPathOriginIndex() + 1, grandParent.allPathOrigins()));
        }
        logger.debug(String.format("After unrolling, stack (%s) is %s", stack.size(), formatted(stack)));
    }

    private void updateOriginPathCount() {
        logger.debug(String.format("Before unrolling, stack (%s) was %s", stack.size(), formatted(stack)));
        StackRecord lastBranchPoint = stack.getLast();
        stack.set(stack.size() - 1, new StackRecord(lastBranchPoint.instruction(), lastBranchPoint.currentPathOriginIndex() + 1, lastBranchPoint.allPathOrigins()));
    }

    private String formatted(List<StackRecord> stack) {
        return "[\n" + stack.stream()
                .map(StackRecord::toString)
                .reduce("", (srs, sr) -> srs.concat("\n").concat(sr))
               + "\n]";
    }

    private void interpret(TranspilerInstruction instr, Graph<TranspilerInstruction, TypedGraphEdge> cfg, List<TranspilerInstruction> visited) {
        visited.add(instr);
        numberOfInstructionsTraced++;
    }


    private boolean isRoot(TranspilerInstruction instr) {
        if (!(instr instanceof MarkerInstruction mi)) return false;
        if (!(mi.ref() instanceof TranspilerCodeBlockNode bn)) return false;
        return bn.getCategories().contains(SemanticCategory.CODE_ROOT);
//        if (!(instr instanceof NullTranspilerInstruction i)) return false;
//        return ("DUMMY_START".equals(i.getMetadata("data")));
    }
}
