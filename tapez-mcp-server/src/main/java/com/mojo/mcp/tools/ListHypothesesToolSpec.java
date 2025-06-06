package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.mojo.logic.Hypothesis;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

public class ListHypothesesToolSpec {
    private final List<Hypothesis> hypotheses;
    String schema = """
                {
                  "type": "object",
                  "properties": {}
                }
            """;

    public ListHypothesesToolSpec(List<Hypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("listHypotheses", "List all current hypotheses", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        String json = new Gson().toJson(hypotheses);
        return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(json)), false);
    }
}
