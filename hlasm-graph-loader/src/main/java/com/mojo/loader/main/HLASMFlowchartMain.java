package com.mojo.loader.main;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.string.VerbatimBasicBlockTextMaker;
import com.mojo.algorithms.visualisation.FlowchartOutputFormat;
import com.mojo.loader.pass.BuildFlowchartBlocksTask;
import com.mojo.loader.pass.ExportSummarisedFlowchartBlocksTask;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.visualisation.*;
import com.mojo.woof.llm.OllamaAdvisor;
import com.mojo.woof.llm.OllamaCredentials;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HLASMFlowchartMain {
    private static final Logger logger = LoggerFactory.getLogger(HLASMFlowchartMain.class);

    public static void main(String[] args) {
        String outputPath = "/Users/asgupta/code/hlasm-analyser/op.json";
        String filePath = "/Users/asgupta/code/asmcode/Root Module/Root module_PSUNA02.txt";
//        String filePath = "/Users/asgupta/code/asmcode/just-ownupd.txt";
//        String filePath = "/Users/asgupta/code/hlasm/test.txt";
//        String filePath = "/Users/asgupta/code/hlasm/simple.txt";
        String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        UUIDProvider idProvider = new UUIDProvider();
        String externalProgramsSearchPath = "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(idProvider, false).structure(filePath, copybookPath, externalProgramsSearchPath);
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph = new BuildFlowchartBlocksTask(idProvider).run(cfg);

        Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph = new BuildHLASMSummarisedFlowchartBlocksTask(blockGraph, idProvider, new AIBasicBlockTextMaker(new OllamaAdvisor(OllamaCredentials.fromEnv()))).run();
        new ExportSummarisedFlowchartBlocksTask(summarisedBlockGraph).run(outputPath);
        new DrawSummarisedFlowchartTask(summarisedBlockGraph, idProvider).run(
                "/Users/asgupta/code/tape-z/flowchart.dot",
                "/Users/asgupta/code/tape-z/flowchart.svg",
                FlowchartOutputFormat.SVG);
//        new ExportFlowchartToNeo4JTask().run(blockGraph);
        System.out.println("COMPLETE!");
    }
}
