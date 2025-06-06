package com.mojo.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;

public class MacroTranspilerNode extends TranspilerNode {
    private final String text;

    public MacroTranspilerNode(String id, String text) {
        super(ImmutableList.of(SemanticCategory.MACRO), id);
        this.text = text;
    }

    @Override
    public String description() {
        return text;
    }
}
