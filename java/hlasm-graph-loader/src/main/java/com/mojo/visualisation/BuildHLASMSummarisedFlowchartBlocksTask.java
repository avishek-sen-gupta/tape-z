package com.mojo.visualisation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildHLASMSummarisedFlowchartBlocksTask {
    Logger LOGGER = LoggerFactory.getLogger(BuildHLASMSummarisedFlowchartBlocksTask.class.getName());
    private final Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph;
    private final IdProvider idProvider;
    private final BasicBlockTextMaker textFormatter;

    public BuildHLASMSummarisedFlowchartBlocksTask(Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph, IdProvider idProvider, BasicBlockTextMaker textMaker) {
        this.blockGraph = blockGraph;
        this.idProvider = idProvider;
        this.textFormatter = textMaker;
    }

    public Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> run() {
        List<BasicBlock<SummarisedBasicBlock>> textVertices = blockGraph.vertexSet().stream()
                .map(bb -> new BasicBlock<>(bb.id(), ImmutableList.of(blockSummary(bb)), ImmutableMap.of())).toList();

        Map<String, BasicBlock<SummarisedBasicBlock>> indexedTextVertices = textVertices.stream()
                .collect(Collectors.toUnmodifiableMap(BasicBlock::id, bb -> bb));
        Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> textGraph = new DirectedPseudograph<>(TypedGraphEdge.class);
        textVertices.forEach(textGraph::addVertex);
        blockGraph.edgeSet().forEach(e -> textGraph.addEdge(
                indexedTextVertices.get(blockGraph.getEdgeSource(e).id()),
                indexedTextVertices.get(blockGraph.getEdgeTarget(e).id()),
                new TypedGraphEdge(e.getRelationshipType(), "NONAME")
        ));

        return textGraph;
    }

    private SummarisedBasicBlock blockSummary(BasicBlock<TranspilerInstruction> bb) {
        if (bb.size() == 1 && bb.firstInstruction().ref() instanceof SectionStartTranspilerNode ||
            bb.firstInstruction().ref() instanceof SectionEndTranspilerNode)
            return new SummarisedBasicBlock(idProvider.next(), bb.firstInstruction().originalText(), bb);
        return new SummarisedBasicBlock(idProvider.next(), textFormatter.format(bb), bb);
    }
}
