package com.mojo.hlasm.command;

import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.llm.AdvisorFactory;
import com.mojo.visualisation.AIBasicBlockTextMaker;
import com.mojo.visualisation.BuildFlowchartPerSectionTask;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "flowchart-sections", mixinStandardHelpOptions = true, version = "0.1",
        description = "Builds the flowchart for all the sections of the entire program, section by section")
public class BuildFlowchartPerSectionCommand implements Callable<Integer> {
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
        new BuildFlowchartPerSectionTask().run(new UUIDProvider(), filePath, copybookPath, outputDir, AdvisorFactory.advisor(model), externalProgramsSearchPath);
        System.out.println("COMPLETE!");
        return 0;
    }
}
