package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.loader.analysis.CyclomaticComplexityCalculator;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CyclomaticComplexityOfSectionToolSpec {
    private static final Logger logger = LoggerFactory.getLogger(CyclomaticComplexityOfSectionToolSpec.class);
    private final HlasmCodeAnalysisResult analysisResult;
    String schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
                "labelName" : {
                  "type" : "string"
                }
              }
            }
            """;

    public CyclomaticComplexityOfSectionToolSpec(HlasmCodeAnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("cyclomaticComplexityOfSection", "Gets the cyclomatic complexity of a section", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        String labelName = (String) params.get("labelName");
        logger.info("Requested label: " + labelName);
        List<TranspilerInstruction> flattened = analysisResult.flattened();
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals = new HLASMInstructionNavigator().labelledSections(flattened);
        if (!labelTerminals.containsKey(labelName))
            return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(String.format("No Label with name '%s' found", labelName))), true);
        Pair<TranspilerInstruction, TranspilerInstruction> matchingTerminals = labelTerminals.get(labelName);
        List<TranspilerInstruction> codeElements = new HLASMInstructionNavigator().codeBetween(matchingTerminals.getLeft(), matchingTerminals.getRight(), flattened);
        long complexity = new CyclomaticComplexityCalculator().cyclomaticComplexity(codeElements);
        return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(Long.toString(complexity))), false);
    }
}
