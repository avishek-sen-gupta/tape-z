package com.mojo.visualisation;

import com.mojo.algorithms.domain.BasicBlock;
import com.mojo.algorithms.domain.CodeSentinelType;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.InstructionLike;

public record SummarisedBasicBlock(String id, String label,
                                   BasicBlock<TranspilerInstruction> bb) implements InstructionLike {
    @Override
    public CodeSentinelType sentinel() {
        return CodeSentinelType.BODY;
    }
}
