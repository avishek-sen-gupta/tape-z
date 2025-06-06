package com.mojo.loader.pass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MacroExpansionParsePass {
    private static final Logger logger = LoggerFactory.getLogger(MacroExpansionParsePass.class);
    private final List<String> mnemonics;
    private final String macroPath;

    public MacroExpansionParsePass(List<String> mnemonics, String macroPath) {
        this.mnemonics = mnemonics;
        this.macroPath = macroPath;
    }

    public List<String> run(List<String> lines) {
        List<String> expandedSource = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("*")) {
                expandedSource.add(line);
            } else if (startsWithOpcode(line)) {
                expandedSource.add(line);
            } else if (line.trim().isEmpty()) {
                expandedSource.add(line);
            } else {
                // Could become more sophisticated in the future
                String[] macroDetails = line.trim().split(" ");
                List<String> cleanedMacroDetails = Arrays.stream(macroDetails)
                        .filter(macroDetail -> !macroDetail.isEmpty())
                        .toList();
                Path possibleMacroPath = Path.of(macroPath, cleanedMacroDetails.getFirst() + "_Assembler_Copybook.txt");
                logger.debug("Checking macro path:" + possibleMacroPath);
                if (!Files.exists(possibleMacroPath)) {
                    expandedSource.add(line);
                    continue;
                }
                logger.debug("Found macro path: " + possibleMacroPath);
                List<String> macroLines = new HLASMCopybookProcessor().run(possibleMacroPath.toAbsolutePath(), cleanedMacroDetails);
                expandedSource.add(String.format("************EXPANDED MACRO STARTS: %s", cleanedMacroDetails.getFirst()));
                expandedSource.addAll(macroLines);
                expandedSource.add(String.format("************EXPANDED MACRO ENDS: %s", cleanedMacroDetails.getFirst()));
            }
        }

        return expandedSource;
    }

    private boolean startsWithOpcode(String line) {
        String s = firstWord(line);
        return mnemonics.stream().map(s::equals)
                .reduce(false, (a, b) -> a || b);
    }

    private String firstWord(String text) {
        if (text.length() <= 9) return "";
        String lineWithoutLabels = text.substring(8);
        String firstWord = Arrays.stream(lineWithoutLabels.trim().split("\\s+"))
                .findFirst()
                .orElse("");
        logger.debug("First word: " + firstWord);
        return firstWord;
    }
}
