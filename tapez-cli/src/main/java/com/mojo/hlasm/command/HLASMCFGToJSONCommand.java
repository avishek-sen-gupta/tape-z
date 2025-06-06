package com.mojo.hlasm.command;

import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.main.HLASMCFGToJSONTask;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "cfg-to-json", mixinStandardHelpOptions = true, version = "0.1",
        description = "Exports the CFG to JSON")
public class HLASMCFGToJSONCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "The path to the HLASM file to analyze")
    private String filePath;

    @CommandLine.Option(names = {"-c", "--copybook"}, description = "The path to the copybook directory", required = true)
    private String copyBookPath;

    @CommandLine.Option(names = {"-o", "--output"}, description = "The path where the output JSON file will be written", required = true)
    private String outputPath;

    @CommandLine.Option(names = {"-e", "--external"},
            required = true,
            description = "Path for external programs")
    private String externalProgramsSearchPath;

    public Integer call() throws Exception {
        try {
            HLASMCFGToJSONTask task = new HLASMCFGToJSONTask(new UUIDProvider());
            task.run(filePath, copyBookPath, outputPath, externalProgramsSearchPath);
            return 0;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}
