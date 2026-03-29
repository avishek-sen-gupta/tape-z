package com.mojo.loader.navigator;


import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.transpiler.CallTranspilerNode;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.transpiler.NullTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import com.mojo.transpiler.hlasm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleTranspilerNodeTraversal {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTranspilerNodeTraversal.class.getName());

    public void traverse(TranspilerNode node, TranspilerNodeVisitor visitor) {
        if (node instanceof TranspilerCodeBlockNode n) {
            visitor.enter(n);
            node.astChildren().forEach(c -> traverse(c, visitor));
            visitor.exit(n);
        } else if (node instanceof RawTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof CommentTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof CallTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof SectionStartTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof SectionEndTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof AsmTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof MacroTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof SqlTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof NullTranspilerNode n)
            visitor.visit(n);
        else if (node instanceof IfTranspilerNode n) {
            visitor.enter(n);
            traverse(n.getIfThenBlock(), visitor);
            traverse(n.getIfElseBlock(), visitor);
            visitor.exit(n);
        } else {
            LOGGER.error("Unknown transpiler node type: " + node.getClass().getName());
            throw new UnsupportedOperationException("Unknown node type: " + node.getClass().getName());
        }
    }
}
