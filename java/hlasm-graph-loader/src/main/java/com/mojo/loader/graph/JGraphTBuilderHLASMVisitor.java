package com.mojo.loader.graph;

import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmParserBaseVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.tree.RuleNode;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.ArrayList;
import java.util.List;

public class JGraphTBuilderHLASMVisitor extends HlasmParserBaseVisitor<Void> {
    @Getter
    private final Graph<TypedGraphVertex, TypedGraphEdge> asmGraph = new DirectedPseudograph<>(TypedGraphEdge.class);
    private final IdProvider idProvider;
    List<TypedGraphVertex> parentStack = new ArrayList<>();

    public JGraphTBuilderHLASMVisitor(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        TypedGraphVertex vertex = new GenericGraphVertex(idProvider.next(), node.getClass().getSimpleName(), "CODE", "NOLABEL", node.getText(), "NONE");
        asmGraph.addVertex(vertex);
        if (!parentStack.isEmpty())
            asmGraph.addEdge(parentStack.getLast(), vertex, new TypedGraphEdge("CONTAINS", "NONE"));
        parentStack.add(vertex);
        Void result = super.visitChildren(node);
        parentStack.remove(vertex);
        return result;
    }
}
