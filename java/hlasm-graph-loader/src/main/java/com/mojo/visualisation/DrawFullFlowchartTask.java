package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.algorithms.visualisation.FlowchartOutputFormat;
import com.mojo.loader.pass.BuildFlowchartBlocksTask;
import com.mojo.loader.pass.ExportSummarisedFlowchartBlocksTask;
import org.jgrapht.Graph;

public class DrawFullFlowchartTask {
    private final IdProvider idProvider;
    private final BasicBlockTextMaker basicBlockTextMaker;

    public DrawFullFlowchartTask(IdProvider idProvider, BasicBlockTextMaker basicBlockTextMaker) {
        this.idProvider = idProvider;
        this.basicBlockTextMaker = basicBlockTextMaker;
    }

    public void run(Graph<TranspilerInstruction, TypedGraphEdge> cfg, String outputPath) {
        Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph = new BuildFlowchartBlocksTask(idProvider).run(cfg);
        Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph = new BuildHLASMSummarisedFlowchartBlocksTask(blockGraph, idProvider, basicBlockTextMaker).run();
        new ExportSummarisedFlowchartBlocksTask(summarisedBlockGraph).run(outputPath);
        new DrawSummarisedFlowchartTask(summarisedBlockGraph, idProvider).run(
                "/Users/asgupta/code/tape-z/flowchart.dot",
                "/Users/asgupta/code/tape-z/flowchart.svg",
                FlowchartOutputFormat.SVG);
    }
}
