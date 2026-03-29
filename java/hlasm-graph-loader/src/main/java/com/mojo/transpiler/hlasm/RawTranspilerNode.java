package com.mojo.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;

public class RawTranspilerNode extends TranspilerNode {
    private final String text;

    public RawTranspilerNode(String id, String text) {
        super(ImmutableList.of(SemanticCategory.UNKNOWN), id);
        this.text = text;
    }

    @Override
    public String description() {
        return text;
    }
}
