package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.algorithms.string.BasicBlockTextMaker;
import com.mojo.algorithms.visualisation.FlowchartOutputFormat;
import com.mojo.loader.pass.BuildFlowchartBlocksTask;
import com.mojo.loader.pass.ExportSummarisedFlowchartBlocksTask;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;

import java.nio.file.Paths;

public class BuildSingleFlowchartTask {
    public Pair<Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge>, Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge>> run(Graph<TranspilerInstruction, TypedGraphEdge> cfg, String sectionName, String outputDir, BasicBlockTextMaker basicBlockTextMaker, UUIDProvider idProvider) {
        Graph<BasicBlock<TranspilerInstruction>, TypedGraphEdge> blockGraph = new BuildFlowchartBlocksTask(idProvider).run(cfg);
        Graph<BasicBlock<SummarisedBasicBlock>, TypedGraphEdge> summarisedBlockGraph = new BuildHLASMSummarisedFlowchartBlocksTask(blockGraph, idProvider, basicBlockTextMaker).run();
        new ExportSummarisedFlowchartBlocksTask(summarisedBlockGraph).run(Paths.get(outputDir, String.format("%s-cfg.json", sectionName)).toString());
        new DrawSummarisedFlowchartTask(summarisedBlockGraph, idProvider).run(
                Paths.get(outputDir, String.format("%s-flowchart.dot", sectionName)).toString(),
                Paths.get(outputDir, String.format("%s-flowchart.svg", sectionName)).toString(),
                FlowchartOutputFormat.SVG);
        return ImmutablePair.of(blockGraph, summarisedBlockGraph);
    }
}
