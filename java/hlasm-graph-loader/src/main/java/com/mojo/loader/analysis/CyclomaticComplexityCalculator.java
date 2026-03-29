package com.mojo.loader.analysis;

import com.mojo.algorithms.domain.CodeSentinelType;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pass.MarkerInstruction;

import java.util.List;

public class CyclomaticComplexityCalculator {
    public long cyclomaticComplexity(List<TranspilerInstruction> instructions) {
        long numIfs = instructions.stream().filter(v -> v instanceof MarkerInstruction m
                && m.sentinel() == CodeSentinelType.ENTER
                && m.ref() instanceof IfTranspilerNode).count();
        long conditionalJumps = instructions.stream().filter(HLASMInstructionNavigator::isConditionalJumpInstruction).count();
        return numIfs + conditionalJumps + 1;
    }
}
