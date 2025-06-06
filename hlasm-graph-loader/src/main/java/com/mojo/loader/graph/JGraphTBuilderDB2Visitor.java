package com.mojo.loader.graph;

import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.db2.Db2SqlParserBaseVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.tree.RuleNode;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.ArrayList;
import java.util.List;

public class JGraphTBuilderDB2Visitor extends Db2SqlParserBaseVisitor<Void> {
    @Getter
    private final Graph<TypedGraphVertex, TypedGraphEdge> db2Graph = new DirectedPseudograph<>(TypedGraphEdge.class);
    private final IdProvider idProvider;
    List<TypedGraphVertex> parentStack = new ArrayList<>();

    public JGraphTBuilderDB2Visitor(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        TypedGraphVertex vertex = new GenericGraphVertex(idProvider.next(), node.getClass().getSimpleName(), "CODE", "NOLABEL", node.getText(), "NONE");
        db2Graph.addVertex(vertex);
        if (!parentStack.isEmpty())
            db2Graph.addEdge(parentStack.getLast(), vertex, new TypedGraphEdge("CONTAINS", "NONE"));
        parentStack.add(vertex);
        Void result = super.visitChildren(node);
        parentStack.remove(vertex);
        return result;
    }
}
