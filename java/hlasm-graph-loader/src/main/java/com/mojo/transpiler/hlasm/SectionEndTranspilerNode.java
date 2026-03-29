package com.mojo.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.loader.code.LabelledCodeBlockElement;

public class SectionEndTranspilerNode extends TranspilerNode {
    private final String label;
    private final LabelledCodeBlockElement block;

    public SectionEndTranspilerNode(String id, String label, LabelledCodeBlockElement block) {
        super(ImmutableList.of(SemanticCategory.BLOCK_BOUNDARY), id);
        this.label = label;
        this.block = block;
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public String description() {
        return "END: " + label;
    }
}
