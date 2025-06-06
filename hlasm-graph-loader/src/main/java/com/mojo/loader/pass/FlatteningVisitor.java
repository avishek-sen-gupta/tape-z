package com.mojo.loader.pass;

import com.mojo.algorithms.domain.CodeSentinelType;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.CallTranspilerNode;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.algorithms.transpiler.NullTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.loader.navigator.TranspilerNodeVisitor;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import com.mojo.transpiler.hlasm.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class FlatteningVisitor implements TranspilerNodeVisitor {
    private final IdProvider idProvider;
    @Getter
    private final List<TranspilerInstruction> flattened = new ArrayList<>();

    public FlatteningVisitor(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    @Override
    public void enter(IfTranspilerNode n) {
        flattened.add(MarkerInstruction.ENTER(n, idProvider));
    }

    @Override
    public void exit(IfTranspilerNode n) {
        flattened.add(MarkerInstruction.EXIT(n, idProvider));
    }

    @Override
    public void enter(TranspilerCodeBlockNode n) {
        flattened.add(MarkerInstruction.ENTER(n, idProvider));
    }

    @Override
    public void exit(TranspilerCodeBlockNode n) {
        flattened.add(MarkerInstruction.EXIT(n, idProvider));
    }

    @Override
    public void visit(RawTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(CommentTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(CallTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(SectionStartTranspilerNode n) {
        flattened.add(MarkerInstruction.ENTER(n, idProvider));
//        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(SectionEndTranspilerNode n) {
        flattened.add(MarkerInstruction.EXIT(n, idProvider));
//        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(AsmTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(MacroTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(SqlTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }

    @Override
    public void visit(NullTranspilerNode n) {
        flattened.add(new TranspilerInstruction(n, CodeSentinelType.BODY, idProvider.next()));
    }
}
