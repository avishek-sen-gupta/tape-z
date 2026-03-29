package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.loader.CodeElementVisitor;
import com.mojo.loader.ParseNode;
import lombok.Getter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.Graph;

import java.util.function.Function;

@Getter
public class AsmCodeElement extends CodeElement {
    @Expose(serialize = false, deserialize = false) final Graph<TypedGraphVertex, TypedGraphEdge> asmGraph;
    @Expose @Getter private final ParseNode tree;
    @Getter private final ParseTree instructionTree;
    @Expose private final CodeElement originalCodeElement;

    public AsmCodeElement(String id, CodeElement originalCodeElement, Graph<TypedGraphVertex, TypedGraphEdge> asmGraph, ParseNode tree, ParseTree instructionTree) {
        super(id, "CODE");
        this.originalCodeElement = originalCodeElement;
        this.asmGraph = asmGraph;
        this.tree = tree;
        this.instructionTree = instructionTree;
    }

    @Override
    public void add(CodeElement codeElement) {

    }

    @Override
    public CodeElement map(Function<CodeElement, CodeElement> mapper) {
        return mapper.apply(this);
    }

    @Override
    public CodeElement reduce() {
        return this;
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public CodeElement merge(CodeElement element) {
        return this;
    }

    @Override
    public String text() {
        return originalCodeElement.text();
    }

    @Override
    public String toString() {
        return text();
    }

    @Override
    public String originalText() {
        return originalCodeElement.text();
    }
}
