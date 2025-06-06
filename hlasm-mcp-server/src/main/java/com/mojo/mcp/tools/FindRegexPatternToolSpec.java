package com.mojo.mcp.tools;

import com.google.gson.Gson;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindRegexPatternToolSpec {
    private static final Logger logger = LoggerFactory.getLogger(FindRegexPatternToolSpec.class);
    private final HlasmCodeAnalysisResult analysisResult;
    String schema = """
            {
              "type" : "object",
              "id" : "urn:jsonschema:Operation",
              "properties" : {
                "pattern" : {
                  "type" : "string"
                }
              }
            }
            """;

    public FindRegexPatternToolSpec(HlasmCodeAnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("matchRegexPattern", "Matches a regex pattern against all the lines in the code. This can be used to find usages of specific symbols and variables", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        String patternAsString = (String) params.get("pattern");
        Pattern pattern = Pattern.compile(patternAsString);
        logger.info("Requested label: " + pattern);
        List<TranspilerInstruction> flattened = analysisResult.flattened().stream()
                .filter(l -> !(l.ref() instanceof CommentTranspilerNode))
                .toList();
        List<Pair<String, Integer>> matches = new ArrayList<>();
        for (int i = 0, flattenedSize = flattened.size(); i < flattenedSize; i++) {
            TranspilerInstruction codeElement = flattened.get(i);
            Matcher matcher = pattern.matcher(codeElement.originalText());
            if (!matcher.find()) continue;
            matches.add(ImmutablePair.of(codeElement.originalText(), i));
        }

        Gson gson = new Gson();
        String json = gson.toJson(matches);
        List<McpSchema.Content> patternMatches = matches.stream()
                .map(element -> (McpSchema.Content) new McpSchema.TextContent(json))
                .toList();
        return new McpSchema.CallToolResult(patternMatches, false);
    }
}
