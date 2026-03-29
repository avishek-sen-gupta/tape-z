package com.mojo.visualisation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.woof.GraphSDK;
import com.mojo.woof.Neo4JDriverBuilder;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.jgrapht.Graph;

import java.util.List;

public class ExportFlowchartToNeo4JTask {
    public void run(Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph) {
        List<WoofNode> graphNodes = blockGraph.vertexSet().stream()
                .map(v -> new WoofNode(ImmutableMap.of("uuid", v.id(), "text", text(v), "type", type(v, blockGraph)), ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = blockGraph.edgeSet().stream()
                .map(v -> new WoofEdge(
                        new WoofNode(ImmutableMap.of("uuid", blockGraph.getEdgeSource(v).id()), ImmutableList.of()),
                        new WoofNode(ImmutableMap.of("uuid", blockGraph.getEdgeTarget(v).id()), ImmutableList.of()),
                        ImmutableMap.of(),
                        ImmutableList.of(v.getRelationshipType())

                )).toList();

        try (GraphSDK graphSDK = new GraphSDK(new Neo4JDriverBuilder().fromEnv())) {
            graphSDK.addGraph(graphNodes, graphEdges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String type(BasicBlock<TranspilerInstruction> v, Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph) {
        return blockGraph.outgoingEdgesOf(v).size() > 1 ? "DECISION" : "GENERIC_BLOCK";
    }

    private String text(BasicBlock<TranspilerInstruction> blk) {
        return blk.getInstructions().stream()
                .map(TranspilerInstruction::originalText)
                .reduce("", (a, b) -> a + "\n" + b);
    }
}
