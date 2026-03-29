package com.mojo.loader.pass;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.string.TranspilerNodeFormatter;
import com.mojo.transpiler.type.TranspilerInstructionTypeGetter;

import java.util.List;

public record StackRecord(TranspilerInstruction instruction, Integer currentPathOriginIndex,
                          List<TypedGraphEdge> allPathOrigins) {
    @Override
    public String toString() {
        return String.format("(%s, [%s...], %s, %s)",
                TranspilerInstructionTypeGetter.instructionType(instruction),
                new TranspilerNodeFormatter().prettyShort(instruction.ref().shortDescription()),
                currentPathOriginIndex, allPathOrigins.size());
    }
}
