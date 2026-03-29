package com.mojo.loader.pass;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.HLASM_ASTBuilder;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.RawCodeElement;

public class HLASMParsePass {
    private final HLASM_ASTBuilder astBuilder;

    public HLASMParsePass(IdProvider idProvider) {
        astBuilder = new HLASM_ASTBuilder(idProvider);
    }

    public CodeElement run(CodeElement node) {
        return node.map(this::parse);
    }

    private CodeElement parse(CodeElement codeElement) {
        if (!(codeElement instanceof RawCodeElement)) return codeElement;
        return astBuilder.run((RawCodeElement) codeElement);
    }
}
