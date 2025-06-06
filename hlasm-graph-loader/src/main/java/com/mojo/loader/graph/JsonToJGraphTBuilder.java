package com.mojo.loader.graph;

import com.fasterxml.jackson.databind.JsonNode;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.IdProvider;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DirectedPseudograph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToJGraphTBuilder {
    private static final Logger logger = LoggerFactory.getLogger(JsonToJGraphTBuilder.class);
    @Getter
    private final Graph<Map<String, Object>, TypedGraphEdge> programGraph = new DirectedPseudograph<>(TypedGraphEdge.class);
    private final IdProvider idProvider;
    List<Map<String, Object>> parentStack = new ArrayList<>();

    public JsonToJGraphTBuilder(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public void traverse(JsonNode node, String key) {
        if (node.isObject()) {
            Map<String, Object> current = new HashMap<>();
            current.put("uuid", idProvider.next());
            node.fields().forEachRemaining(entry -> {
                logger.debug("Key: " + entry.getKey());
                JsonNode childNode = entry.getValue();
                if (childNode.isArray() || childNode.isObject()) return;
                current.put(entry.getKey(), childNode.asText());
            });
            if (!current.containsKey("id")) current.put("type", "GenericNode");

            programGraph.addVertex(current);
            if (!parentStack.isEmpty())
                programGraph.addEdge(parentStack.getLast(), current, new TypedGraphEdge("CONTAINS", "NONE"));
            parentStack.add(current);
            node.fields().forEachRemaining(entry -> {
                JsonNode childNode = entry.getValue();
                if (childNode.isObject()) {
                    traverse(childNode, entry.getKey());
                } else if (childNode.isArray()) {
                    traverse(childNode, entry.getKey());
                }
            });

            parentStack.remove(current);
        }
        else if (node.isArray()) {
            Map<String, Object> current = new HashMap<>();
            current.put("uuid", idProvider.next());
            current.put("field", key);
            current.put("type", "ARRAY");
            programGraph.addVertex(current);
            if (!parentStack.isEmpty())
                programGraph.addEdge(parentStack.getLast(), current, new TypedGraphEdge("CONTAINS", "NONE"));
            parentStack.add(current);
            node.forEach(child -> traverse(child, idProvider.next()));
            parentStack.remove(current);
        } else {
            throw new UnsupportedOperationException("Cant come in here");
        }
    }
}
