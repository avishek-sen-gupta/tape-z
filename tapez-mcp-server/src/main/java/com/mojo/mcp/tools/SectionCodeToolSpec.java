package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.mojo.algorithms.domain.TranspilerInstruction;
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

public class SectionCodeToolSpec {
    private static final Logger logger = LoggerFactory.getLogger(SectionCodeToolSpec.class);
    private final HlasmCodeAnalysisResult analysisResult;
    private static final Logger LOGGER = LoggerFactory.getLogger(SectionCodeToolSpec.class);
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

    public SectionCodeToolSpec(HlasmCodeAnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("sectionCode", "Gets all the contents of a labelled section", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        LOGGER.info("CALLED SECTION CODE TOOL SPEC");
        String labelName = (String) params.get("labelName");
        logger.info("Requested label: " + labelName);
        List<TranspilerInstruction> flattened = analysisResult.flattened();
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals = new HLASMInstructionNavigator().labelledSections(flattened);
        if (!labelTerminals.containsKey(labelName))
            return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(String.format("No Label with name '%s' found", labelName))), true);
        Pair<TranspilerInstruction, TranspilerInstruction> matchingTerminals = labelTerminals.get(labelName);
        int start = flattened.indexOf(matchingTerminals.getLeft());
        int end = flattened.indexOf(matchingTerminals.getRight());
        List<McpSchema.Content> linesAsString = flattened.subList(start, end + 1).stream().map(element -> (McpSchema.Content) new McpSchema.TextContent(element.originalText())).toList();
        return new McpSchema.CallToolResult(linesAsString, false);
    }
}
