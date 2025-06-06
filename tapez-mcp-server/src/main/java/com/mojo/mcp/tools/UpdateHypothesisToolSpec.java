package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.mojo.logic.Hypothesis;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class UpdateHypothesisToolSpec {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateHypothesisToolSpec.class);
    private final List<Hypothesis> hypotheses;
    String schema = """
                {
                  "type": "object",
                  "properties": {
                    "index": {
                      "type": "integer",
                      "description": "Index of the hypothesis to update"
                    },
                    "subject": {
                      "type": "string",
                      "description": "Updated subject"
                    },
                    "relation": {
                      "type": "string",
                      "description": "Updated relation"
                    },
                    "object": {
                      "type": "string",
                      "description": "Updated object"
                    },
                    "confidence": {
                      "type": "number",
                      "description": "Updated confidence value"
                    }
                  },
                  "required": ["index"]
                }
            """;

    public UpdateHypothesisToolSpec(List<Hypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("updateHypothesis", "Updates an existing hypothesis", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        LOGGER.info("UpdateHypothesis tool called with params: " + params);

        // Get the index of the hypothesis to update
        int index = ((Number) params.get("index")).intValue();

        // Check if the index is valid
        if (index < 0 || index >= hypotheses.size()) {
            return new McpSchema.CallToolResult(
                ImmutableList.of(new McpSchema.TextContent("Error: Invalid hypothesis index: " + index)),
                false
            );
        }

        // Get the current hypothesis
        Hypothesis current = hypotheses.get(index);

        // Create a new hypothesis with updated values, using current values as defaults
        Hypothesis updated = new Hypothesis(
            params.containsKey("subject") ? (String) params.get("subject") : current.subject(),
            params.containsKey("relation") ? (String) params.get("relation") : current.relation(),
            params.containsKey("object") ? (String) params.get("object") : current.object(),
            params.containsKey("confidence") ? ((Number) params.get("confidence")).doubleValue() : current.confidence()
        );

        // Replace the hypothesis at the specified index
        hypotheses.set(index, updated);

        // Return the updated hypothesis as JSON
        String json = new Gson().toJson(updated);
        return new McpSchema.CallToolResult(
            ImmutableList.of(new McpSchema.TextContent("Updated hypothesis at index " + index + ": " + json)),
            false
        );
    }
}
