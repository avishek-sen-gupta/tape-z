package com.mojo.loader.pass;

import com.mojo.algorithms.domain.CodeSentinelType;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.transpiler.hlasm.SectionEndTranspilerNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;

import static com.mojo.transpiler.type.TranspilerInstructionTypeGetter.instructionType;

public class MarkerInstruction extends TranspilerInstruction {
    public MarkerInstruction(TranspilerNode ref, CodeSentinelType sentinel, String id) {
        super(ref, sentinel, id);
    }

    public static TranspilerInstruction ENTER(TranspilerNode n, IdProvider idProvider) {
        return new MarkerInstruction(n, CodeSentinelType.ENTER, idProvider.next());
    }

    public static TranspilerInstruction EXIT(TranspilerNode n, IdProvider idProvider) {
        return new MarkerInstruction(n, CodeSentinelType.EXIT, idProvider.next());
    }

    @Override
    public String originalText() {
        if (ref() instanceof SectionStartTranspilerNode s) return String.format("%s: %s", sentinel(), s.label());
        else if (ref() instanceof SectionEndTranspilerNode s) return String.format("%s: %s", sentinel(), s.label());
        return String.format("%s: %s", sentinel(), instructionType(this));
    }

    @Override
    public String label() {
        return ref().label();
    }
}
