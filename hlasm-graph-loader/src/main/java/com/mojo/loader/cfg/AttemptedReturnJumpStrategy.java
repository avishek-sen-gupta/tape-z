package com.mojo.loader.cfg;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.IdProvider;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public interface AttemptedReturnJumpStrategy {
    TranspilerInstruction attemptedReturn(TranspilerInstruction labelStartTerminal, Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals, List<TranspilerInstruction> flattened, IdProvider idProvider);
}
