package com.mojo.loader.pass;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.MacroElement;
import com.mojo.loader.code.RawCodeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class UnresolvedMacroParsePass {
    private static final Logger logger = LoggerFactory.getLogger(UnresolvedMacroParsePass.class);
    private final List<String> mnemonics;
    private final IdProvider idProvider;

    public UnresolvedMacroParsePass(List<String> mnemonics, IdProvider idProvider) {
        this.mnemonics = mnemonics;
        this.idProvider = idProvider;
    }

    public CodeElement run(CodeElement element) {
        return element.map(this::toMacro);
    }

    private CodeElement toMacro(CodeElement codeElement) {
        if (!(codeElement instanceof RawCodeElement)) return codeElement;
        String line = ((RawCodeElement) codeElement).getLine();
        String s = firstWord(line);
        Boolean startsWithOpcode = mnemonics.stream().map(s::equals)
                .reduce(false, (a, b) -> a || b);
        if (!startsWithOpcode) logger.debug(s + " did not match any opcode");
        return startsWithOpcode ? codeElement : new MacroElement(idProvider.next(), line);
    }

    private String firstWord(String text) {
        String firstWord = Arrays.stream(text.trim().split("\\s+"))
                .findFirst()
                .orElse("");
        logger.debug("First word: " + firstWord);
        return firstWord;
    }
}
