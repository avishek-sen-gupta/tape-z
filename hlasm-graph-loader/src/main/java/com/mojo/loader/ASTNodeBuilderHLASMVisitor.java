package com.mojo.loader;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmParserBaseVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ASTNodeBuilderHLASMVisitor extends HlasmParserBaseVisitor<Void> {
    @Getter private final ParseNode root;
    private final IdProvider idProvider;
    List<ParseNode> parentStack = new ArrayList<>();

    public ASTNodeBuilderHLASMVisitor(IdProvider idProvider) {
        this.idProvider = idProvider;
        root = new ParseNode(idProvider.next(), "ROOT", "CODE", "HLASM_ROOT", "NONE");
        parentStack.add(root);
    }

    @Override
    public Void visitChildren(RuleNode node) {
        ParseNode current = new ParseNode(idProvider.next(), node.getClass().getSimpleName(), "CODE", node.getText(), "NONE");
        parentStack.getLast().addChild(current);
        parentStack.add(current);
        Void result = super.visitChildren(node);
        parentStack.remove(current);
        return result;
    }

    @Override
    public Void visitTerminal(TerminalNode node) {
        ParseNode current = new ParseNode(idProvider.next(), node.getClass().getSimpleName(), "CODE", node.getText(), "NONE");
        parentStack.getLast().addChild(current);
        return null;
    }
}
