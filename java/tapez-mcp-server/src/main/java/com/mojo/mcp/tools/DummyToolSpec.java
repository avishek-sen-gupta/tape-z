package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;

public class DummyToolSpec {
    String schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
                "stuffName" : {
                  "type" : "string"
                }
              }
            }
            """;

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("stuffGetter", "Gets stuff for you", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> stringObjectMap) {
        Object stuffName = stringObjectMap.get("stuffName");
        return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent("STUFF NAME FROM JAVA: " + stuffName)), false);
    }
}
