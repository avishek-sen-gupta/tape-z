package com.mojo.loader.main;

import com.google.common.collect.Sets;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.graph.ExportCFGToNeo4JTask;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.woof.Neo4JDriverBuilder;
import org.jgrapht.Graph;

import java.util.List;
import java.util.stream.Collectors;

public class HLASMCFGMain {
    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/code/asmcode/Root Module/main.txt";
//        String filePath = "/Users/asgupta/code/hlasm/test.txt";
//        String filePath = "/Users/asgupta/code/hlasm/simple.txt";
        String filePath = "/Users/asgupta/code/hlasm/flowchart-hlasm.txt";
        String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider(), false).structure(filePath, copybookPath, "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        List<TranspilerInstruction> flattened = analysisResult.flattened();
        Sets.SetView<TranspilerInstruction> difference = Sets.difference(flattened.stream().collect(Collectors.toUnmodifiableSet()), cfg.vertexSet());
        System.out.println("Untouched instructions: ");
        difference.forEach(System.out::println);

        new ExportCFGToNeo4JTask(new Neo4JDriverBuilder()).run(cfg, true);

        System.out.println("COMPLETE!");
    }
}
