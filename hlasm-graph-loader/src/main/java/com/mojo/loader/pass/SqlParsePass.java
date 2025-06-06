package com.mojo.loader.pass;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.Db2ASTBuilder;
import com.mojo.loader.ExecSqlPatternMatcher;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.RawCodeElement;

public class SqlParsePass {
    private final IdProvider idProvider;

    public SqlParsePass(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public CodeElement run(CodeElement node) {
        return node.map(this::parse);
    }

    private CodeElement parse(CodeElement codeElement) {
        if (!(codeElement instanceof RawCodeElement)) return codeElement;
        String line = ((RawCodeElement) codeElement).getLine();
        if (!new ExecSqlPatternMatcher().matches(line)) return codeElement;
        return new Db2ASTBuilder(idProvider).run(codeElement);
    }
}
