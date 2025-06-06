package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.loader.CodeElementVisitor;
import com.mojo.loader.ParseNode;
import lombok.Getter;
import org.jgrapht.Graph;

import java.util.function.Function;

public class SqlCodeElement extends CodeElement {
    @Expose private final String line;
    @Expose(serialize = false, deserialize = false) @Getter private final Graph<TypedGraphVertex, TypedGraphEdge> db2Graph;
    @Expose @Getter private final ParseNode tree;

    public SqlCodeElement(String id, String line, Graph<TypedGraphVertex, TypedGraphEdge> db2Graph, ParseNode tree) {
        super(id, "SQL");
        this.line = line;
        this.db2Graph = db2Graph;
        this.tree = tree;
    }

    @Override
    public String originalText() {
        return line;
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
        throw new UnsupportedOperationException("Should not be called early enough to merge");
    }

    @Override
    protected boolean continues() {
        return line.endsWith("X") || line.endsWith("-");
    }

    @Override
    public String text() {
        return line;
    }
}
