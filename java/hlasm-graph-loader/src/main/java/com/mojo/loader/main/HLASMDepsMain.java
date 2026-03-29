package com.mojo.loader.main;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.CodeSentinelType;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.loader.pass.MarkerInstruction;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.woof.GraphSDK;
import com.mojo.woof.Neo4JDriverBuilder;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.jgrapht.Graph;

import java.util.List;

public class HLASMDepsMain {
    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        String filePath = "/Users/asgupta/code/asmcode/Root module/main.txt";
        String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        String externalProgramsSearchPath = "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure(filePath, copybookPath, externalProgramsSearchPath);
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        Graph<String, TypedGraphEdge> dependencyMap = analysisResult.dependencyMap().getDependencyGraph();

        List<TranspilerInstruction> ifStatements = analysisResult.flattened().stream()
                .filter(l -> l instanceof MarkerInstruction m && m.sentinel() == CodeSentinelType.ENTER && m.ref() instanceof IfTranspilerNode ifNode)
                .toList();

        List<WoofNode> graphNodes = dependencyMap.vertexSet().stream()
                .map(v -> new WoofNode(ImmutableMap.of("uuid", v, "text", v, "type", "PROGRAM"), ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = dependencyMap.edgeSet().stream()
                .map(v -> new WoofEdge(
                        new WoofNode(ImmutableMap.of("uuid", dependencyMap.getEdgeSource(v)), ImmutableList.of()),
                        new WoofNode(ImmutableMap.of("uuid", dependencyMap.getEdgeTarget(v)), ImmutableList.of()),
                        ImmutableMap.of(),
                        ImmutableList.of("DEPENDS_ON")

                )).toList();
        try (GraphSDK graphSDK = new GraphSDK(new Neo4JDriverBuilder().fromEnv())) {
            graphSDK.addGraph(graphNodes, graphEdges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("COMPLETE!");
    }
}
