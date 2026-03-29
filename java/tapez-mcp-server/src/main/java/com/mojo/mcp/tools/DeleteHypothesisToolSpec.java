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

public class DeleteHypothesisToolSpec {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteHypothesisToolSpec.class);
    private final List<Hypothesis> hypotheses;
    String schema = """
                {
                  "type": "object",
                  "properties": {
                    "index": {
                      "type": "integer",
                      "description": "Index of the hypothesis to delete"
                    }
                  },
                  "required": ["index"]
                }
            """;

    public DeleteHypothesisToolSpec(List<Hypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("deleteHypothesis", "Deletes an existing hypothesis", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        LOGGER.info("DeleteHypothesis tool called with params: " + params);

        // Get the index of the hypothesis to delete
        int index = ((Number) params.get("index")).intValue();

        // Check if the index is valid
        if (index < 0 || index >= hypotheses.size()) {
            return new McpSchema.CallToolResult(
                ImmutableList.of(new McpSchema.TextContent("Error: Invalid hypothesis index: " + index)),
                false
            );
        }

        // Get the hypothesis to be deleted (for the response)
        Hypothesis deleted = hypotheses.get(index);

        // Remove the hypothesis at the specified index
        hypotheses.remove(index);

        // Return the deleted hypothesis as JSON
        String json = new Gson().toJson(deleted);
        return new McpSchema.CallToolResult(
            ImmutableList.of(new McpSchema.TextContent("Deleted hypothesis at index " + index + ": " + json)),
            false
        );
    }
}
