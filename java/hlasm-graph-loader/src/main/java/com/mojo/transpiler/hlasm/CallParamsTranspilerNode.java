package com.mojo.transpiler.hlasm;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;

import java.util.List;

public class CallParamsTranspilerNode extends TranspilerNode {
    private final String destination;
    private final List<String> params;
    private final String text;

    public CallParamsTranspilerNode(String destination, List<String> params, String text) {
        super(ImmutableList.of(SemanticCategory.CALL_EXTERNAL));
        this.destination = destination;
        this.params = params;
        this.text = text;
    }

    @Override
    public String description() {
        return text;
    }
}
