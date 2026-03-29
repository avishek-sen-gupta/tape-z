package com.mojo.loader.main;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.BasicBlockFactory;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.task.BuildBasicBlocksTask;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.woof.GraphSDK;
import com.mojo.woof.Neo4JDriverBuilder;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.List;

public class BasicBlockAnalysisMain {

    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        String filePath = "/Users/asgupta/code/hlasm/test.txt";
        String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        String externalProgramsSearchPath = "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure(filePath, copybookPath, externalProgramsSearchPath);
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        HLASMInstructionNavigator.removeHousekeepingEdges(cfg);
        List<TranspilerInstruction> flattened = analysisResult.flattened().stream().map(e -> (TranspilerInstruction) e).toList();
        DirectedPseudograph<TranspilerInstruction, DefaultEdge> inputCFG = new DirectedPseudograph<>(DefaultEdge.class);
        cfg.vertexSet().forEach(inputCFG::addVertex);
        cfg.edgeSet().forEach(e -> inputCFG.addEdge(cfg.getEdgeSource(e), cfg.getEdgeTarget(e)));


        Pair<Graph<BasicBlock<TranspilerInstruction>, DefaultEdge>, List<BasicBlock<TranspilerInstruction>>> basicBlockTaskResult = new BuildBasicBlocksTask(flattened, inputCFG, new BasicBlockFactory<>(new UUIDProvider()), null).run();
//        List<BasicBlock<CodeElement>> basicBlocks = basicBlockTaskResult.getRight();
        Graph<BasicBlock<TranspilerInstruction>, DefaultEdge> limitGraph = basicBlockTaskResult.getLeft();

        List<WoofNode> graphNodes = limitGraph.vertexSet().stream()
                .map(v -> new WoofNode(ImmutableMap.of(
                        "uuid", v.id(),
                        "text", v.label(),
                        "type", "BASIC_BLOCK",
                        "num_nodes", v.getInstructions().size(),
                        "first_instruction", v.getInstructions().getFirst().label(),
                        "last_instruction", v.getInstructions().getLast().label()
                ),
                        ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = limitGraph.edgeSet().stream()
                .map(v -> new WoofEdge(
                        new WoofNode(ImmutableMap.of("uuid", limitGraph.getEdgeSource(v).id()), ImmutableList.of()),
                        new WoofNode(ImmutableMap.of("uuid", limitGraph.getEdgeTarget(v).id()), ImmutableList.of()),
                        ImmutableMap.of(),
                        ImmutableList.of("FLOWS_INTO")
                )).toList();

        try (GraphSDK graphSDK = new GraphSDK(new Neo4JDriverBuilder().fromEnv())) {
            graphSDK.addGraph(graphNodes, graphEdges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("COMPLETE!");
    }
}
