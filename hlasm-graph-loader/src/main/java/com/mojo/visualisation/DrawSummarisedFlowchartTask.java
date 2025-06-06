package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.string.TranspilerNodeFormatter;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.visualisation.FlowchartOutputFormat;
import com.mojo.algorithms.visualisation.GraphGenerator;
import com.mojo.algorithms.visualisation.GraphvizStyleScheme;
import com.mojo.graph.ConnectionType;
import com.mojo.loader.pass.MarkerInstruction;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.engine.Engine;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static guru.nidi.graphviz.model.Factory.mutNode;

public class DrawSummarisedFlowchartTask {
    private final Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> blockGraph;
    private final IdProvider idProvider;
    private final MutableGraph graph;
    Logger LOGGER = LoggerFactory.getLogger(DrawSummarisedFlowchartTask.class.getName());
    private final TranspilerNodeFormatter textFormatter = new TranspilerNodeFormatter();

    public DrawSummarisedFlowchartTask(Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph, IdProvider idProvider) {
        this.blockGraph = summarisedBlockGraph;
        this.idProvider = idProvider;
        graph = Factory.mutGraph("example1").setDirected(true).setCluster(true);
        Graphviz.useEngine(new GraphvizCmdLineEngine().timeout(5, java.util.concurrent.TimeUnit.HOURS));
    }

    public void run(String dotFilePath, String imageOutputPath, FlowchartOutputFormat outputFormat) {
        buildChart();
        write(dotFilePath);
        try {
            new GraphGenerator(outputFormat).generateImage(dotFilePath, imageOutputPath);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void buildChart() {
        List<MutableNode> chartNodes = blockGraph.vertexSet().stream()
                .map(v -> {
                    MutableNode chartNode = chartNode(v);
                    return styled(chartNode, v, blockGraph);
                })
                .toList();
        chartNodes.forEach(graph::add);
        blockGraph.vertexSet().forEach(v -> {
            MutableNode chartSource = chartNodes.stream().filter(cn -> cn.name().toString().equals(v.id())).findFirst().get();
            blockGraph.outgoingEdgesOf(v).forEach(oe -> {
                BasicBlock<SummarisedBasicBlock> edgeTarget = blockGraph.getEdgeTarget(oe);
                String edgeTargetID = edgeTarget.id();
                MutableNode destination = chartNodes.stream()
                        .filter(cn -> edgeTargetID.equals(cn.name().toString()))
                        .findFirst().orElseThrow();
                connectFlow(chartSource, oe, destination);

            });
        });
    }

    private void connectFlow(MutableNode chartSource, TypedGraphEdge oe, MutableNode chartDestination) {
        if (!ConnectionType.FLOWS_TO_IF_TRUE.value().equals(oe.getRelationshipType()) &&
            !ConnectionType.FLOWS_TO_IF_FALSE.value().equals(oe.getRelationshipType()))
            chartSource.addLink(chartSource.linkTo(chartDestination)
                    .with("penwidth", "3"));
        else {
            chartSource.addLink(chartSource.linkTo(chartDestination)
                    .with("penwidth", "3")
                    .with("color", ConnectionType.FLOWS_TO_IF_TRUE.value().equals(oe.getRelationshipType()) ? Color.GREEN2.value : Color.RED.value)
            );
        }
    }

    private MutableNode chartNode(BasicBlock<SummarisedBasicBlock> v) {
        return mutNode(v.id()).add("label", basicBlockText(v));
    }

    private String basicBlockText(BasicBlock<SummarisedBasicBlock> v) {
        String breakCharacter = v.firstInstruction().bb().firstInstruction().ref() instanceof IfTranspilerNode ? "\n" : "\\l";
        return textFormatter.splitLines(v.firstInstruction().label(), 40, breakCharacter);
    }

    private MutableNode styled(MutableNode node, BasicBlock<SummarisedBasicBlock> v, Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> blockGraph) {
        return style(v, blockGraph).apply(node);
    }

    private GraphvizStyleScheme style(BasicBlock<SummarisedBasicBlock> v, Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> blockGraph) {
        BasicBlock<TranspilerInstruction> actualBB = v.firstInstruction().bb();
        if (blockGraph.outgoingEdgesOf(v).size() > 1) return HLASMFlowchartStylePreferences.DECISION;
        else if (actualBB.size() == 1 && actualBB.firstInstruction().ref() instanceof SectionStartTranspilerNode)
            return HLASMFlowchartStylePreferences.SECTION_START;
        else if (actualBB.size() == 1 && actualBB.firstInstruction().ref() instanceof SectionEndTranspilerNode)
            return HLASMFlowchartStylePreferences.SECTION_END;
        else if (actualBB.size() == 1 && actualBB.firstInstruction() instanceof MarkerInstruction)
            return HLASMFlowchartStylePreferences.JOIN;
            // Hides empty ELSE clauses
        else if (blockGraph.incomingEdgesOf(v).size() > 1) return HLASMFlowchartStylePreferences.JOIN;
        else if (actualBB.size() == 2 &&
                 actualBB.firstInstruction() instanceof MarkerInstruction &&
                 actualBB.lastInstruction() instanceof MarkerInstruction)
            return HLASMFlowchartStylePreferences.JOIN;
        return HLASMFlowchartStylePreferences.PROCESSING;
    }

    private void write(String dotFilePath) {
        try {
            Graphviz.fromGraph(graph).engine(Engine.DOT)
                    .render(Format.DOT)
                    .toFile(new File(dotFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
