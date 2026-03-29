package com.mojo.loader.navigator;

import com.mojo.algorithms.transpiler.CallTranspilerNode;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.transpiler.NullTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import com.mojo.transpiler.hlasm.*;

public interface TranspilerNodeVisitor {
    void enter(IfTranspilerNode n);

    void exit(IfTranspilerNode n);

    void enter(TranspilerCodeBlockNode n);

    void exit(TranspilerCodeBlockNode n);

    void visit(RawTranspilerNode n);

    void visit(CommentTranspilerNode n);

    void visit(CallTranspilerNode n);

    void visit(SectionStartTranspilerNode n);

    void visit(SectionEndTranspilerNode n);

    void visit(AsmTranspilerNode n);

    void visit(MacroTranspilerNode n);

    void visit(SqlTranspilerNode n);

    void visit(NullTranspilerNode n);
}
