package com.mojo.loader.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.analysis.SingleLineExplanation;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import com.mojo.woof.llm.Advisor;
import com.mojo.woof.llm.AzureOpenAIAdvisor;
import com.mojo.woof.llm.OpenAICredentials;
import org.jgrapht.Graph;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BuildExplanationDictionaryMain {

    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        String filePath = "/Users/asgupta/code/hlasm/test.txt";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure(filePath, "/Users/asgupta/code/asmcode/Assembler Copybook", "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
        cfg.vertexSet().forEach(instr -> System.out.println(instr.originalText()));
        Advisor advisor = new AzureOpenAIAdvisor(OpenAICredentials.fromEnv());
        List<SingleLineExplanation> llmExplanations = cfg.vertexSet().stream()
                .map(instr -> new SingleLineExplanation(instr.originalText(), explanation(instr, advisor))).toList();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("/Users/asgupta/code/hlasm/explanations.json")) {
            gson.toJson(llmExplanations, writer);  // directly write to file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("COMPLETE!");
    }

    private static String explanation(TranspilerInstruction v, Advisor advisor) {
        return String.join("\n", advisor.advise(String.format("This is a line of HLASM. Explain it in a single line, but do not omit any details. ```%s```", v.originalText())));
    }
}
