package com.mojo.loader.main.trace;

import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.transpiler.LabelledTranspilerCodeBlockNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.graph.ConnectionType;
import com.mojo.hlasm.HlasmParserParser;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pass.MarkerInstruction;
import com.mojo.loader.symbolic.AbstractSymbol;
import com.mojo.loader.symbolic.AddressSymbol;
import com.mojo.loader.symbolic.NullSymbolReference;
import com.mojo.loader.symbolic.SymbolRegistry;
import com.mojo.transpiler.hlasm.AsmTranspilerNode;
import lombok.Getter;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HLASMTracer {
    private static final Logger logger = LoggerFactory.getLogger(HLASMTracer.class);
    private final SymbolRegistry symbolRegistry = new SymbolRegistry();
    private final List<TranspilerInstruction> programTrace = new ArrayList<>();

    public List<TranspilerInstruction> run(Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        Optional<TranspilerInstruction> possibleRoot = cfg.vertexSet().stream().filter(this::isRoot).findFirst();
        if (possibleRoot.isEmpty()) {
            throw new IllegalStateException("Cannot find root node");
        }

        TranspilerInstruction rootNode = possibleRoot.get();
        traceCFGLoopIteratively(rootNode, cfg);
        logger.info("COMPLETE!");
        return programTrace;
    }

    private void traceCFGLoopIteratively(TranspilerInstruction root, Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        TranspilerInstruction currentNode = root;
        List<TranspilerInstruction> visited = new ArrayList<>();
        while (true) {
            programTrace.add(currentNode);
            if (visited.contains(currentNode)) {
                logger.info(String.format("Visited node %s already, stopping...", currentNode.label()));
                break;
            }
            visited.add(currentNode);
            logger.info("Interpreting " + currentNode.originalText() + "...");
            interpret(currentNode, cfg);
            Set<TypedGraphEdge> allEdges = cfg.outgoingEdgesOf(currentNode);
            List<TypedGraphEdge> traversableEdges = allEdges.stream()
                    .filter(e -> !e.getRelationshipType().equals(ConnectionType.FLOWS_AFTER_SUBROUTINE.value())
                                 && !e.getRelationshipType().equals(ConnectionType.FLOWS_TO_SYNTAX_ONLY.value()))
                    .toList();

            if (traversableEdges.isEmpty()) {
                logger.info("Reached a dead end, stopping program...");
                break;
            }

            if (traversableEdges.size() > 1) {
                AbstractSymbol returnSymbol = handleBRRE(currentNode);
                if (!(returnSymbol instanceof AddressSymbol)) {
                    int randomIndex = (int) (Math.random() * traversableEdges.size());
                    TypedGraphEdge randomDestination = traversableEdges.stream().toList().get(randomIndex);
                    currentNode = cfg.getEdgeTarget(randomDestination);
                    logger.info(String.format("Reached a decision point, picking %s out of %s to '%s'...", randomIndex + 1, traversableEdges.size(), currentNode.originalText()));
                } else {
                    currentNode = ((AddressSymbol) returnSymbol).getElement();
                    logger.info(String.format("Returning to '%s'", currentNode.originalText()));
                }
            } else {
                TypedGraphEdge first = traversableEdges.getFirst();
                currentNode = cfg.getEdgeTarget(first);
            }
        }

        logger.info("EXITING INTERPRET...");
        symbolRegistry.log();
    }

    private AbstractSymbol handleBRRE(TranspilerInstruction node) {
        if (!(node.ref() instanceof AsmTranspilerNode asm)) return new NullSymbolReference();
        Optional<HlasmParserParser.Br_rule_144Context> possibleLoad = new HLASMInstructionNavigator().possibleBranchRegisterInstruction(asm);
        if (possibleLoad.isEmpty()) return new NullSymbolReference();
        HlasmParserParser.Br_rule_144Context branchRegisterInstruction = possibleLoad.get();
        String sourceRegister = branchRegisterInstruction.operand_1_register().getText();
        return symbolRegistry.get(sourceRegister).resolved();
    }

    private void interpret(TranspilerInstruction instr, Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        if (!(instr.ref() instanceof AsmTranspilerNode)) return;
        handleBranchAndLink(instr, cfg);
        handleStoreInstruction(instr);
        handleLoad(instr);
    }

    private void handleLoad(TranspilerInstruction element) {
        Optional<HlasmParserParser.L_rule_773Context> possibleLoad = new HLASMInstructionNavigator().possibleLoadInstruction((AsmTranspilerNode) element.ref());
        if (possibleLoad.isEmpty()) return;
        HlasmParserParser.L_rule_773Context storeInstruction = possibleLoad.get();
        // Hack! HLASM parser needs to become more modular to identify operands and the first level
        String source = storeInstruction.getText().split(",")[1];
        String target = storeInstruction.operand_1_register().getText();
        symbolRegistry.updateWithNamedSymbol(source, target);
    }

    private void handleStoreInstruction(TranspilerInstruction element) {
        Optional<HlasmParserParser.St_rule_1549Context> possibleStore = new HLASMInstructionNavigator().possibleStoreInstruction((AsmTranspilerNode) element.ref());
        if (possibleStore.isEmpty()) return;
        HlasmParserParser.St_rule_1549Context storeInstruction = possibleStore.get();
        String source = storeInstruction.operand_1_register().getText();
        // Hack! HLASM parser needs to become more modular to identify operands and the first level
        String target = storeInstruction.getText().split(",")[1];
        symbolRegistry.updateWithNamedSymbol(source, target);
    }

    private void handleBranchAndLink(TranspilerInstruction element, Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        HlasmParserParser.StartRuleContext top = (HlasmParserParser.StartRuleContext) ((AsmTranspilerNode) element.ref()).getInstructionTree();
        Optional<HlasmParserParser.Bal_rule_88Context> possibleBranchAndLink = Optional.ofNullable(top)
                .map(HlasmParserParser.StartRuleContext::assemblerInstruction)
                .map(HlasmParserParser.AssemblerInstructionContext::instruction_group_1)
                .map(HlasmParserParser.Instruction_group_1Context::bal_rule_88);
        if (possibleBranchAndLink.isEmpty()) return;
        TypedGraphEdge subroutineReturnContinuationEdge = cfg.outgoingEdgesOf(element).stream()
                .filter(e -> ConnectionType.FLOWS_AFTER_SUBROUTINE.value().equals(e.getRelationshipType()))
                .toList().getFirst();
        symbolRegistry.update("RE", new AddressSymbol(cfg.getEdgeTarget(subroutineReturnContinuationEdge)));
    }

    private boolean isRoot(TranspilerInstruction instr) {
        if (!(instr instanceof MarkerInstruction mi)) return false;
        if (!(mi.ref() instanceof TranspilerCodeBlockNode bn)) return false;
        return bn.getCategories().contains(SemanticCategory.CODE_ROOT);
    }
}
