package com.mojo.loader;

import com.mojo.loader.code.*;
import lombok.Getter;

public class AbstractTextPrinter implements CodeElementVisitor {
    @Getter
    private String output = "";

    @Override
    public void visit(CommentElement comment) {
        output += String.format("@comment_%s_%n", comment.getId());
    }

    @Override
    public void visit(AsmCodeElement element) {
        output += String.format("@asm_%s_%n", element.getId());
    }

    @Override
    public void visit(RawCodeElement element) {
        output += String.format("@raw_%s_%n", element.getId());
    }

    @Override
    public void visit(SqlCodeElement element) {
        output += String.format("@sql_%s_%n", element.getId());
    }

    @Override
    public void visit(IfStatementElement element) {
        output += String.format("@if_%s_%n", element.getId());
    }

    @Override
    public void visit(ElseStatementElement element) {
        output += String.format("@else_%s_%n", element.getId());
    }

    @Override
    public void visit(EndIfStatementElement element) {
        output += String.format("@endif_%s_%n", element.getId());
    }

    @Override
    public void visit(MacroElement element) {
        output += String.format("@macro_%s_%n", element.getId());
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
        output += String.format("@call_%s_%n", element.getId());
    }

    @Override
    public void visitStart(LabelledCodeBlockElement codeBlock) {
        output += String.format("@labelstart_%s_%n", codeBlock.getId());
    }

    @Override
    public void visitEnd(LabelledCodeBlockElement codeBlock) {
        output += String.format("@labelend_%s_%n", codeBlock.getId());
    }

    @Override
    public void visitStart(CodeBlockElement element) {

    }

    @Override
    public void visitEnd(CodeBlockElement element) {

    }
}
