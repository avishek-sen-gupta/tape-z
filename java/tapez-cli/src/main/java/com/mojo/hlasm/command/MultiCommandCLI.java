package com.mojo.hlasm.command;

import picocli.CommandLine;

public class MultiCommandCLI {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new RootCommand()).execute(args);
        System.exit(exitCode);
    }

    static {
        // Set default log level if not already set via system property
        if (System.getProperty("org.slf4j.simpleLogger.defaultLogLevel") == null)
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
    }
}
