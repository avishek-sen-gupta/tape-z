package com.mojo.loader;

import com.mojo.hlasm.HlasmParserBaseVisitor;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.stream.IntStream;

public class PrettyPrintingVisitor extends HlasmParserBaseVisitor<Void> {
    private int indent = 0;

    @Override
    public Void visitChildren(RuleNode node) {
        String indentation = IntStream.range(0, indent).mapToObj(i -> " ").reduce("", (x, y) -> x + y);
        System.out.printf(indentation + "└──── ");
        System.out.println("" + node.getClass().getSimpleName() + ": " + node.getText());
        indent += 2;
        Void result = super.visitChildren(node);
        indent -= 2;
        return result;
    }
}
