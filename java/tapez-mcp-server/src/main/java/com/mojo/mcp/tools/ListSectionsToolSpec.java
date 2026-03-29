package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.loader.navigator.HLASMInstructionNavigator;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class ListSectionsToolSpec {
    private final HlasmCodeAnalysisResult analysisResult;
    String schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {}
            }
            """;

    public ListSectionsToolSpec(HlasmCodeAnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("listSections", "Lists all the sections present in the code", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        Map<String, Pair<TranspilerInstruction, TranspilerInstruction>> labelTerminals = new HLASMInstructionNavigator().labelledSections(analysisResult.flattened());
        List<LabelSize> labelsWithSizes = labelTerminals.entrySet().stream().map(e -> new LabelSize(e.getKey(), sectionSize(e.getValue()))).toList();
        Gson gson = new Gson();
        String json = gson.toJson(labelsWithSizes);
        return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(json)), false);
    }

    private int sectionSize(Pair<TranspilerInstruction, TranspilerInstruction> section) {
        List<TranspilerInstruction> flattened = analysisResult.flattened();
        int startIndex = flattened.indexOf(section.getLeft());
        int endIndex = flattened.indexOf(section.getRight());
        return endIndex - startIndex + 1;
    }

    record LabelSize(String labelName, int size) {
    }
}
