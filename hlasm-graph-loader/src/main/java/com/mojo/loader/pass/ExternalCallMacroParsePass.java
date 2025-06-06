package com.mojo.loader.pass;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.code.CallExternalCustomElement;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.MacroElement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalCallMacroParsePass {

    private static final Logger logger = LoggerFactory.getLogger(ExternalCallMacroParsePass.class);
    public static final String CALL_ROUTINE_INSTRUCTION = "NOLCALL";
    private final IdProvider idProvider;
    private Map<String, String> dependencies = new HashMap<>();
    private final ExternalCallResolutionStrategy filePathResolutionStrategy;

    public ExternalCallMacroParsePass(IdProvider idProvider, ExternalCallResolutionStrategy filePathResolutionStrategy1) {
        this.idProvider = idProvider;
        this.filePathResolutionStrategy = filePathResolutionStrategy1;
    }

    public Pair<CodeElement, Map<String, String>> run(CodeElement element) {
//        return element;
        return ImmutablePair.of(element.map(this::toMacro), dependencies);
    }

    private CodeElement toMacro(CodeElement codeElement) {
        if (!(codeElement instanceof MacroElement)) return codeElement;
        String line = ((MacroElement) codeElement).getLine().trim();
        if (!line.startsWith(CALL_ROUTINE_INSTRUCTION))
            return codeElement;
        return macro(line);
    }

    private CodeElement macro(String line) {
        logger.debug("Possible call macro found: " + line);
        Pattern compile = Pattern.compile(String.format("%s\\s+(\\w+)(?:,\\(?([^)]*)\\))?", CALL_ROUTINE_INSTRUCTION));
        Matcher matcher = compile.matcher(line.trim());
        if (!matcher.find())
            throw new RuntimeException("No call macro target found: " + line);
        String externalProgram = matcher.group(1);
        List<String> parameters = matcher.group(2) != null ? params(matcher.group(2)) : ImmutableList.of();
        logger.debug("External program: " + externalProgram);
        logger.debug("Parameters: " + parameters);
//            String copybookPath = "/Users/asgupta/code/asmcode/Assembler Copybook";
        Optional<String> actualPath = filePathResolutionStrategy.run(externalProgram);
        if (actualPath.isEmpty()) {
            logger.warn("Could not find file matching pattern: " + externalProgram);
            return new CallExternalCustomElement(idProvider.next(), externalProgram, parameters, line);
        }

        dependencies.put(externalProgram, actualPath.get());
        return new CallExternalCustomElement(idProvider.next(), externalProgram, parameters, line);
    }

    private List<String> params(String allParams) {
        return Arrays.stream(allParams.split(",")).toList();
    }
}
