package com.mojo.loader.main;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.string.VerbatimBasicBlockTextMaker;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.visualisation.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HLASMFlowchartMain {
    private static final Logger logger = LoggerFactory.getLogger(HLASMFlowchartMain.class);

    public static void main(String[] args) {
        String outputDir = "/Users/asgupta/code/tape-z";
//        String filePath = "/Users/asgupta/code/asmcode/Root Module/main.txt";
//        String filePath = "/Users/asgupta/code/asmcode/just-ownupd.txt";
//        String filePath = "/Users/asgupta/code/hlasm/test.txt";
        String filePath = "/Users/asgupta/code/hlasm/flowchart-hlasm.txt";
        String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        UUIDProvider idProvider = new UUIDProvider();
        String externalProgramsSearchPath = "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(idProvider, false).structure(filePath, copybookPath, externalProgramsSearchPath);
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        Pair<Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge>, Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge>> flowcharts = new BuildSingleFlowchartTask().run(analysisResult.controlFlowGraph(), "program", outputDir, new VerbatimBasicBlockTextMaker(), idProvider);

        new ExportFlowchartToNeo4JTask().run(flowcharts.getLeft());
        System.out.println("COMPLETE!");
    }
}
