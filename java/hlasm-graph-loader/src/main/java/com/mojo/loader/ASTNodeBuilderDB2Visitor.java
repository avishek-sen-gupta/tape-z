package com.mojo.loader;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.db2.Db2SqlParserBaseVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class ASTNodeBuilderDB2Visitor extends Db2SqlParserBaseVisitor<Void> {
    @Getter private final ParseNode root;
    private final IdProvider idProvider;
    List<ParseNode> parentStack = new ArrayList<>();

    public ASTNodeBuilderDB2Visitor(IdProvider idProvider) {
        this.idProvider = idProvider;
        root = new ParseNode(idProvider.next(), "DB2ROOT", "CODE", "DB2ROOT", "NONE");
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
