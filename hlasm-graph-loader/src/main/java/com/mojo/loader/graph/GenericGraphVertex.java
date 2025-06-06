package com.mojo.loader.graph;

import com.mojo.algorithms.domain.TypedGraphVertex;

public record GenericGraphVertex(
        String id,
        String type,
        String label,
        String name,
        String text,
        String namespace
) implements TypedGraphVertex {
}
