package com.mojo.loader;

import com.mojo.loader.code.*;

public interface CodeElementVisitor {
    void visit(CommentElement comment);

    void visit(AsmCodeElement element);

    void visit(RawCodeElement element);

    void visit(SqlCodeElement element);

    void visit(IfStatementElement element);

    void visit(ElseStatementElement element);

    void visit(EndIfStatementElement element);

    void visitStart(LabelledCodeBlockElement codeBlock);

    void visitEnd(LabelledCodeBlockElement codeBlock);

    void visitStart(CodeBlockElement element);

    void visitEnd(CodeBlockElement element);

    void visit(MacroElement element);

    void visit(LabelEndElement element);

    void visit(LabelStartElement element);

    void visit(TerminalElement terminalElement);

    void visitStart(IfBodyElement element);

    void visitEnd(IfBodyElement element);

    void visit(EmptyLineElement element);

    void visit(CallExternalCustomElement element);
}
