package com.mojo.loader;

import com.mojo.algorithms.transpiler.CallTranspilerNode;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.transpiler.NullTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.loader.navigator.TranspilerNodeVisitor;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import com.mojo.transpiler.hlasm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RawCodeReporterVisitor implements TranspilerNodeVisitor {
    private static final Logger logger = LoggerFactory.getLogger(RawCodeReporterVisitor.class);

    @Override
    public void enter(IfTranspilerNode n) {

    }

    @Override
    public void exit(IfTranspilerNode n) {

    }

    @Override
    public void enter(TranspilerCodeBlockNode n) {

    }

    @Override
    public void exit(TranspilerCodeBlockNode n) {

    }

    @Override
    public void visit(RawTranspilerNode n) {
        logger.info(n.shortDescription());
    }

    @Override
    public void visit(CommentTranspilerNode n) {

    }

    @Override
    public void visit(CallTranspilerNode n) {

    }

    @Override
    public void visit(SectionStartTranspilerNode n) {

    }

    @Override
    public void visit(SectionEndTranspilerNode n) {

    }

    @Override
    public void visit(AsmTranspilerNode n) {

    }

    @Override
    public void visit(MacroTranspilerNode n) {

    }

    @Override
    public void visit(SqlTranspilerNode n) {

    }

    @Override
    public void visit(NullTranspilerNode n) {

    }
}
