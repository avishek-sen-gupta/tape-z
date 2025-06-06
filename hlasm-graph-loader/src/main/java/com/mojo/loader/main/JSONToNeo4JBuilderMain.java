package com.mojo.loader.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.graph.JsonToJGraphTBuilder;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.woof.GraphSDK;
import com.mojo.woof.Neo4JDriverBuilder;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONToNeo4JBuilderMain {
    public static void main(String[] args) throws IOException {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        String filePath = "/Users/asgupta/code/hlasm/output.json";
        ObjectMapper mapper = new ObjectMapper();

        // Read JSON from a file
        File jsonFile = new File(filePath);
        JsonNode node = mapper.readTree(jsonFile);
        JsonToJGraphTBuilder jsonToJGraphTBuilder = new JsonToJGraphTBuilder(new UUIDProvider());
        jsonToJGraphTBuilder.traverse(node, "NOKEY");
        Graph<Map<String, Object>, TypedGraphEdge> programGraph = jsonToJGraphTBuilder.getProgramGraph();

        List<WoofNode> graphNodes = programGraph.vertexSet().stream()
                .map(v -> new WoofNode(v, ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = programGraph.edgeSet().stream()
                .map(v -> new WoofEdge(
                        new WoofNode(programGraph.getEdgeSource(v), ImmutableList.of()),
                        new WoofNode(programGraph.getEdgeTarget(v), ImmutableList.of()),
                        ImmutableMap.of(),
                        ImmutableList.of("CONTAINS")
                )).toList();
        System.out.println("COMPLETE!");

        try (GraphSDK graphSDK = new GraphSDK(new Neo4JDriverBuilder().fromEnv())) {
            graphSDK.addGraph(graphNodes, graphEdges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
