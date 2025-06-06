package com.mojo.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.loader.ParseNode;
import org.jgrapht.Graph;

public class SqlTranspilerNode extends TranspilerNode {
    private final String text;
    private final Graph<TypedGraphVertex, TypedGraphEdge> db2Graph;
    private final ParseNode tree;

    public SqlTranspilerNode(String id, String text, Graph<TypedGraphVertex, TypedGraphEdge> db2Graph, ParseNode tree) {
        super(ImmutableList.of(SemanticCategory.MACRO, SemanticCategory.SQL), id);
        this.text = text;
        this.db2Graph = db2Graph;
        this.tree = tree;
    }

    @Override
    public String description() {
        return text;
    }
}
