package com.mojo.loader.main;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.loader.pass.MarkerInstruction;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.jgrapht.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.mojo.transpiler.type.TranspilerInstructionTypeGetter.instructionType;

public class HLASMCFGToJSONTask {
    private static final Logger logger = LoggerFactory.getLogger(HLASMCFGToJSONTask.class);
    private final IdProvider idProvider;

    public HLASMCFGToJSONTask(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public void run(String filePath, String copybookPath, String outputPath, String externalProgramsSearchPath) {
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(idProvider, false).structure(filePath, copybookPath, externalProgramsSearchPath);
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();

        List<WoofNode> graphNodes = cfg.vertexSet().stream()
                .map(v -> new WoofNode(
                        selectedProperties(v),
                        ImmutableList.of())).toList();

        List<WoofEdge> graphEdges = cfg.edgeSet().stream()
                .map(v -> new WoofEdge(
                        new WoofNode(ImmutableMap.of("uuid", cfg.getEdgeSource(v).id()), ImmutableList.of()),
                        new WoofNode(ImmutableMap.of("uuid", cfg.getEdgeTarget(v).id()), ImmutableList.of()),
                        ImmutableMap.of(),
                        ImmutableList.of(v.getRelationshipType())

                )).toList();

        Map<String, List<?>> nodesEdges = ImmutableMap.of("nodes", graphNodes, "edges", graphEdges);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        logger.info("Writing to: " + outputPath);
        try (FileWriter writer = new FileWriter(outputPath)) {
            gson.toJson(nodesEdges, writer);  // directly write to file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Writing to: " + outputPath);
        logger.info("COMPLETE!");
    }

    private static Map<String, Object> selectedProperties(TranspilerInstruction v) {
//        ImmutableMap<String, Object> commonProperties = ImmutableMap.of("uuid", v.id(), "text", v.originalText(), "type", instructionType(v));
        if (v instanceof MarkerInstruction mi) {
            if (v.ref() instanceof TranspilerCodeBlockNode block) return ImmutableMap.of(
                    "uuid", v.id(),
                    "text", v.originalText(),
                    "type", v.getClass().getSimpleName(),
                    "elementType", mi.ref().getClass().getSimpleName(),
                    "element_text", mi.ref().shortDescription(),
                    "categories", block.getCategories().stream()
                            .map(Enum::name)
                            .toList());

            return ImmutableMap.of(
                    "uuid", mi.id(),
                    "text", mi.originalText(),
                    "type", mi.getClass().getSimpleName(),
                    "elementType", mi.ref().getClass().getSimpleName(),
                    "element_text", mi.ref().shortDescription(),
                    "sentinelType", mi.sentinel().name()
            );
        }
        return ImmutableMap.of("uuid", v.id(), "text", v.originalText(), "type", instructionType(v));
    }
}
