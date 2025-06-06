package com.mojo.mcp.tools;

import com.google.common.collect.ImmutableList;
import com.mojo.logic.Hypothesis;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class CreateHypothesisToolSpec {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateHypothesisToolSpec.class);
    private final List<Hypothesis> hypotheses;
    String schema = """
                {
                  "type": "object",
                  "properties": {
                    "hypotheses": {
                      "type": "array",
                      "items": {
                        "type": "object",
                        "properties": {
                          "subject": {
                            "type": "string"
                          },
                          "relation": {
                            "type": "string"
                          },
                          "object": {
                            "type": "string"
                          },
                          "confidence": {
                            "type": "number"
                          }
                        }
                      }
                    }
                  }
                }
            """;

    public CreateHypothesisToolSpec(List<Hypothesis> hypotheses) {
        this.hypotheses = hypotheses;
    }

    public McpServerFeatures.SyncToolSpecification tool() {
        return new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool("createHypothesis", "Creates a hypothesis on a relation between a subject and an object", schema),
                this::run);
    }

    private McpSchema.CallToolResult run(McpSyncServerExchange mcpSyncServerExchange, Map<String, Object> params) {
        LOGGER.info("==================LOGGING SOMETHING==============");
        LOGGER.info("PARAMS: " + params);
        List<Map<String, Object>> hypotheses = (List<Map<String, Object>>) params.get("hypotheses");
        List<Hypothesis> newHypotheses = hypotheses.stream()
                .map(hyp -> new Hypothesis(
                        hyp.get("subject").toString(),
                        hyp.get("relation").toString(),
                        hyp.get("object").toString(),
                        ((Number) hyp.get("confidence")).doubleValue()))
                .toList();

        this.hypotheses.addAll(newHypotheses);
//        String createdHypotheses = new Gson().toJson(newHypotheses);
        return new McpSchema.CallToolResult(ImmutableList.of(new McpSchema.TextContent(String.format("CREATED OK: %s hypotheses", newHypotheses.size()))), false);
    }
}
