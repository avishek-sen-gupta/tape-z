package com.mojo.mcp.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.logic.Hypothesis;
import com.mojo.mcp.tools.*;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.ArrayList;
import java.util.List;

public class HlasmMCPToolServer {
    public static void main(String[] args) {
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure("/Users/asgupta/code/hlasm/simple.txt", "/Users/asgupta/code/asmcode/Assembler Copybook", "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");
        List<Hypothesis> hypotheses = new ArrayList<>();
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        McpSyncServer syncServer = McpServer.sync(transportProvider)
                .serverInfo("my-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder()
                        .resources(false, false)     // Enable resource support
                        .tools(true)         // Enable tool support
                        .prompts(false)       // Enable prompt support
                        .logging()           // Enable logging support
                        .build())
                .build();

        syncServer.addTool(new DummyToolSpec().tool());
        syncServer.addTool(new CyclomaticComplexityOfEverythingToolSpec(analysisResult).tool());
        syncServer.addTool(new SectionCodeToolSpec(analysisResult).tool());
        syncServer.addTool(new CyclomaticComplexityOfSectionToolSpec(analysisResult).tool());
        syncServer.addTool(new GetLinesToolSpec(analysisResult).tool());
        syncServer.addTool(new FindRegexPatternToolSpec(analysisResult).tool());
        syncServer.addTool(new ListSectionsToolSpec(analysisResult).tool());
//        syncServer.addTool(new CreateHypothesisToolSpec(hypotheses).tool());
//        syncServer.addTool(new UpdateHypothesisToolSpec(hypotheses).tool());
//        syncServer.addTool(new DeleteHypothesisToolSpec(hypotheses).tool());
//        syncServer.addTool(new ListHypothesesToolSpec(hypotheses).tool());

//        syncServer.close();
    }
}
