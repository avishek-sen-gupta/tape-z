package com.mojo.loader.pipeline;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.loader.code.CodeElement;
import com.mojo.algorithms.domain.TypedGraphEdge;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Set;

public record HlasmCodeAnalysisResult(
        Graph<TranspilerInstruction, TypedGraphEdge> controlFlowGraph,
        List<Pair<String, Long>> complexitiesByLabel,
        List<TranspilerInstruction> flattened,
        CodeElement hlasmParsedRootNode,
        TranspilerNode hlasmSqlMacroParsedRootNode,
        Set<Pair<TranspilerInstruction, TranspilerInstruction>> possibleSubroutines,
        HLASMDependencyMap dependencyMap) {
}
