package com.mojo.transpiler.type;

import com.mojo.algorithms.domain.TranspilerInstruction;

public class TranspilerInstructionTypeGetter {
    public static String instructionType(TranspilerInstruction v) {
        return v.ref() != null ? v.ref().getClass().getSimpleName() : "NULL_TYPE";
    }
}
