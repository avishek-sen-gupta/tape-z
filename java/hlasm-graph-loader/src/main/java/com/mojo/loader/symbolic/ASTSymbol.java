package com.mojo.loader.symbolic;

import org.antlr.v4.runtime.tree.ParseTree;

public class ASTSymbol implements AbstractSymbol {
    private final String text;
    private final ParseTree argument;

    public ASTSymbol(String text, ParseTree argument) {
        this.text = text;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return argument.getText();
    }

    @Override
    public AbstractSymbol resolved() {
        return this;
    }
}
