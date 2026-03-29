package com.mojo.loader.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;

import java.io.FileWriter;
import java.io.IOException;

public class HLASMParsePipelineMain {

    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        String filePath = "/Users/asgupta/code/hlasm/test.txt";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure(filePath, "/Users/asgupta/code/asmcode/Assembler Copybook", "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter("/Users/asgupta/code/hlasm/output.json")) {
            gson.toJson(analysisResult.hlasmSqlMacroParsedRootNode(), writer);  // directly write to file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("COMPLETE!");
    }
}
