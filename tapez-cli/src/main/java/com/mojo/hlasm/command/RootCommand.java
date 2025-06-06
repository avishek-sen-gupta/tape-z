package com.mojo.hlasm.command;

import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "app", mixinStandardHelpOptions = true, version = "0.1",
        subcommands = {HLASMCFGToJSONCommand.class, BuildFlowchartCommand.class, BuildFlowchartPerSectionCommand.class},
        description = "Implements various operations useful for reverse engineering Cobol code")
public class RootCommand implements Callable<Integer> {
    @Override
    public Integer call() {
        return 0;
    }
}
