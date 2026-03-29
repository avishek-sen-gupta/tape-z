package com.mojo.loader;

import com.mojo.hlasm.HlasmParserBaseVisitor;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.stream.IntStream;

public class ErrorNodeVisitor extends HlasmParserBaseVisitor<Void> {
    private boolean parseError;

    @Override
    public Void visitErrorNode(ErrorNode node) {
        parseError = true;
        return super.visitErrorNode(node);
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (parseError) return null;
        return super.visitChildren(node);
    }

    public boolean isError() {
        return parseError;
    }
}
