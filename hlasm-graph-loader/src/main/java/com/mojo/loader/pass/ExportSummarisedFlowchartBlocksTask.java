package com.mojo.loader.pass;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.visualisation.SummarisedBasicBlock;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportSummarisedFlowchartBlocksTask {
    private static final Logger logger = LoggerFactory.getLogger(ExportSummarisedFlowchartBlocksTask.class);
    private final Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph;

    public ExportSummarisedFlowchartBlocksTask(Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph) {
        this.summarisedBlockGraph = summarisedBlockGraph;
    }

    public void run(String outputPath) {
        List<WoofNode> graphNodes = summarisedBlockGraph.vertexSet().stream()
                .map(v -> new WoofNode(
                        ImmutableMap.of("uuid", v.id(), "text", v.firstInstruction().label()),
                        ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = summarisedBlockGraph.edgeSet().stream()
                .map(e -> new WoofEdge(
                        new WoofNode(ImmutableMap.of("uuid", summarisedBlockGraph.getEdgeSource(e).id()), ImmutableList.of()),
                        new WoofNode(ImmutableMap.of("uuid", summarisedBlockGraph.getEdgeTarget(e).id()), ImmutableList.of()),
                        ImmutableMap.of("relationshipType", e.getRelationshipType()),
                        ImmutableList.of(e.getRelationshipType())
                )).toList();

        Map<String, List<?>> nodesEdges = ImmutableMap.of("nodes", graphNodes, "edges", graphEdges);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.info("Writing to: " + outputPath);
        try (FileWriter writer = new FileWriter(outputPath)) {
            gson.toJson(nodesEdges, writer);  // directly write to file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
