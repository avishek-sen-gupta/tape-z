package com.mojo.mcp.tools;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.Map;

public class GetLinesToolSpec {
    private final HlasmCodeAnalysisResult analysisResult;
    String schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
                "fromLineNo" : {
                  "type" : "integer"
                },
                "toLineNo" : {
                  "type" : "integer"
                }
              }
            }
            """;

    public GetLinesToolSpec(HlasmCodeAnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("getLinesOfCode", "Gets all the lines of code in a specified range", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        int fromLineNo = (Integer) params.get("fromLineNo");
        int toLineNo = (Integer) params.get("toLineNo");
        List<TranspilerInstruction> flattened = analysisResult.flattened();
        List<TranspilerInstruction> range = flattened.subList(fromLineNo, toLineNo + 1);
        List<McpSchema.Content> linesAsString = range.stream().map(element -> (McpSchema.Content) new McpSchema.TextContent(element.originalText())).toList();
        return new McpSchema.CallToolResult(linesAsString, false);
    }
}
