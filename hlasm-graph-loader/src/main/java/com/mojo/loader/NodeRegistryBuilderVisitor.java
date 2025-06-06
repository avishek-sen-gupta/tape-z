package com.mojo.loader;

import com.mojo.loader.code.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

// Only used for pre-structured source
public class NodeRegistryBuilderVisitor implements CodeElementVisitor {
    private static final Logger logger = LoggerFactory.getLogger(NodeRegistryBuilderVisitor.class);
    @Getter
    private final Map<String, CodeElement> nodeMap = new HashMap<>();

    @Override
    public void visit(CommentElement element) {
        addToMap(element);
    }

    private void addToMap(CodeElement element) {
        if (nodeMap.containsKey(element.getId())) {
            logger.error("WARNING WTF");
        }
        nodeMap.put(element.getId(), element);
    }

    @Override
    public void visit(AsmCodeElement element) {
        addToMap(element);
    }

    @Override
    public void visit(RawCodeElement element) {
        addToMap(element);
    }

    @Override
    public void visit(SqlCodeElement element) {
        addToMap(element);
    }

    @Override
    public void visit(IfStatementElement element) {
        addToMap(element);
    }

    @Override
    public void visit(ElseStatementElement element) {
        addToMap(element);
    }

    @Override
    public void visit(EndIfStatementElement element) {
        addToMap(element);
    }

    @Override
    public void visitStart(LabelledCodeBlockElement element) {
        addToMap(element);
    }

    @Override
    public void visitEnd(LabelledCodeBlockElement element) {
//        addToMap(element);
    }

    @Override
    public void visitStart(CodeBlockElement element) {

    }

    @Override
    public void visitEnd(CodeBlockElement element) {

    }

    @Override
    public void visit(MacroElement element) {
        addToMap(element);
    }

    @Override
    public void visit(LabelEndElement element) {
    }

    @Override
    public void visit(LabelStartElement element) {

    }

    @Override
    public void visit(TerminalElement terminalElement) {

    }

    @Override
    public void visitStart(IfBodyElement element) {

    }

    @Override
    public void visitEnd(IfBodyElement element) {

    }

    @Override
    public void visit(EmptyLineElement element) {

    }

    @Override
    public void visit(CallExternalCustomElement element) {
        addToMap(element);
    }
}
