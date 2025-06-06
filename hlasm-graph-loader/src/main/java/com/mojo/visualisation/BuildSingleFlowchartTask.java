package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.algorithms.visualisation.FlowchartOutputFormat;
import com.mojo.loader.pass.BuildFlowchartBlocksTask;
import com.mojo.loader.pass.ExportSummarisedFlowchartBlocksTask;
import org.jgrapht.Graph;

import java.nio.file.Paths;

public class BuildSingleFlowchartTask {
    public void run(UUIDProvider idProvider, String outputDir, BasicBlockTextMaker basicBlockTextMaker, String sectionName, Graph<TranspilerInstruction, TypedGraphEdge> cfg) {
        Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph = new BuildFlowchartBlocksTask(idProvider).run(cfg);
        Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph = new BuildHLASMSummarisedFlowchartBlocksTask(blockGraph, idProvider, basicBlockTextMaker).run();
        new ExportSummarisedFlowchartBlocksTask(summarisedBlockGraph).run(Paths.get(outputDir, String.format("%s-cfg.json", sectionName)).toString());
        new DrawSummarisedFlowchartTask(summarisedBlockGraph, idProvider).run(
                Paths.get(outputDir, String.format("%s-flowchart.dot", sectionName)).toString(),
                Paths.get(outputDir, String.format("%s-flowchart.svg", sectionName)).toString(),
                FlowchartOutputFormat.SVG);
    }
}
