package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.Map;

public class CyclomaticComplexityOfEverythingToolSpec {
    private final HlasmCodeAnalysisResult analysisResult;
    String schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {}
            }
            """;

    public CyclomaticComplexityOfEverythingToolSpec(HlasmCodeAnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("cyclomaticComplexityOfFullCodeBase", "Gets the cyclomatic complexity of the full codebase by labelled section", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> stringObjectMap) {
        return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(analysisResult.complexitiesByLabel().toString())), false);
    }
}
