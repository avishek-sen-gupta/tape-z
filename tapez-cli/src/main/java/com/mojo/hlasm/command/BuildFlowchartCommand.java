package com.mojo.hlasm.command;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.llm.AdvisorFactory;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.visualisation.*;
import org.jgrapht.Graph;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "flowchart", mixinStandardHelpOptions = true, version = "0.1",
        description = "Builds the flowchart for the entire program in one go")
public class BuildFlowchartCommand implements Callable<Integer> {
    @CommandLine.Option(names = {"-s", "--srcDir"},
            required = true,
            description = "The HLASM source directory")
    private String sourceDir;

    @CommandLine.Parameters(index = "0",
            description = "HLASM The program to analyse")
    private String programName;

    @CommandLine.Option(names = {"-cp", "--copyBooksDir"},
            required = true,
            description = "Copybook directory")
    private String copyBookDirs;

    @CommandLine.Option(names = {"-o", "--outputDir"},
            required = true,
            description = "Output directoru")
    private String outputDir;

    @CommandLine.Option(names = {"-e", "--external"},
            required = true,
            description = "Path for external programs")
    private String externalProgramsSearchPath;

    @CommandLine.Option(names = {"-m", "--model"},
            required = false,
            description = "Foundation model to use")
    private String model;

    public Integer call() throws IOException {
        String copybookPath = copyBookDirs;
        String filePath = Paths.get(sourceDir, programName).toString();
        UUIDProvider idProvider = new UUIDProvider();
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(idProvider, false).structure(filePath, copybookPath, externalProgramsSearchPath);
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        new BuildSingleFlowchartTask().run(idProvider, outputDir, AdvisorFactory.advisor(model), programName, analysisResult.controlFlowGraph());
        System.out.println("COMPLETE!");
        return 0;
    }
}
