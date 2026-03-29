package com.mojo.graph;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.woof.GraphSDK;
import com.mojo.woof.Neo4JDriverBuilder;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.jgrapht.Graph;

import java.util.List;

import static com.mojo.transpiler.type.TranspilerInstructionTypeGetter.instructionType;

public class ExportCFGToNeo4JTask {

    private final Neo4JDriverBuilder neo4JDriverBuilder;

    public ExportCFGToNeo4JTask(Neo4JDriverBuilder neo4JDriverBuilder) {
        this.neo4JDriverBuilder = neo4JDriverBuilder;
    }

    public void run(Graph<TranspilerInstruction, TypedGraphEdge> cfg, boolean shouldRun) {
        if (!shouldRun) return;
        List<WoofNode> graphNodes = cfg.vertexSet().stream()
                .map(v -> new WoofNode(ImmutableMap.of("uuid", v.id(), "text", v.originalText(), "type", instructionType(v)), ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = cfg.edgeSet().stream()
                .map(v -> new WoofEdge(
                        new WoofNode(ImmutableMap.of("uuid", cfg.getEdgeSource(v).id()), ImmutableList.of()),
                        new WoofNode(ImmutableMap.of("uuid", cfg.getEdgeTarget(v).id()), ImmutableList.of()),
                        ImmutableMap.of(),
                        ImmutableList.of(v.getRelationshipType())

                )).toList();

        try (GraphSDK graphSDK = new GraphSDK(neo4JDriverBuilder.fromEnv())) {
            graphSDK.addGraph(graphNodes, graphEdges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
