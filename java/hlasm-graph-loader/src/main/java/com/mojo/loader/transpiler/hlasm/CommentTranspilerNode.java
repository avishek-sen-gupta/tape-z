package com.mojo.loader.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;

public class CommentTranspilerNode extends TranspilerNode {
    private final String comment;

    public CommentTranspilerNode(String id, String comment) {
        super(ImmutableList.of(SemanticCategory.COMMENT), id);
        this.comment = comment;
    }

    @Override
    public String description() {
        return comment;
    }
}
