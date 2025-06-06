package com.mojo.loader.pipeline;

import com.mojo.loader.code.CodeElement;
import com.mojo.algorithms.domain.TypedGraphEdge;
import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.Graph;

import java.util.List;
import java.util.Set;

public record HlasmCodeAnalysisResult(
        Graph<com.mojo.algorithms.domain.TranspilerInstruction, TypedGraphEdge> controlFlowGraph,
        List<Pair<String, Long>> complexitiesByLabel,
        List<com.mojo.algorithms.domain.TranspilerInstruction> flattened,
        CodeElement hlasmParsedRootNode,
        com.mojo.algorithms.domain.TranspilerNode hlasmSqlMacroParsedRootNode,
        Set<Pair<com.mojo.algorithms.domain.TranspilerInstruction, com.mojo.algorithms.domain.TranspilerInstruction>> possibleSubroutines,
        HLASMDependencyMap dependencyMap) {
}
