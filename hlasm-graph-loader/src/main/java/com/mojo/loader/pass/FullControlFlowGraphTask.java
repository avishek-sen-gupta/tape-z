package com.mojo.loader.pass;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.cfg.CFGBuilder;
import com.mojo.algorithms.domain.TypedGraphEdge;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Set;

public class FullControlFlowGraphTask {
    private final IdProvider idProvider;

    public FullControlFlowGraphTask(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public Pair<Graph<TranspilerInstruction, TypedGraphEdge>, Set<Pair<TranspilerInstruction, TranspilerInstruction>>> run(List<TranspilerInstruction> flattened) {
        Pair<Graph<TranspilerInstruction, TypedGraphEdge>, Set<Pair<TranspilerInstruction, TranspilerInstruction>>> cfgBuildResult = new CFGBuilder(idProvider).run(flattened);
        return cfgBuildResult;
    }
}
