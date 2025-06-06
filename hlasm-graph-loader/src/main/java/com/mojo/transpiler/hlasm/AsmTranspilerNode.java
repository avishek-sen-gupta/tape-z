package com.mojo.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.loader.ParseNode;
import com.mojo.loader.code.CodeElement;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.Graph;

public class AsmTranspilerNode extends TranspilerNode {
    private final CodeElement originalCodeElement;
    @Getter private final Graph<TypedGraphVertex, TypedGraphEdge> graph;
    @Getter private final ParseNode syntheticTree;
    @Getter private final ParseTree instructionTree;

    public AsmTranspilerNode(String id, CodeElement originalCodeElement, Graph<TypedGraphVertex, TypedGraphEdge> graph, ParseNode syntheticTree, ParseTree instructionTree) {
        super(ImmutableList.of(SemanticCategory.COMPUTATIONAL), id);
        this.originalCodeElement = originalCodeElement;
        this.graph = graph;
        this.syntheticTree = syntheticTree;
        this.instructionTree = instructionTree;
    }

    @Override
    public String description() {
        return originalCodeElement.text();
    }
}
