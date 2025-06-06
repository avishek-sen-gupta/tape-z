package com.mojo.loader.main;

import com.mojo.algorithms.domain.TranspilerInstruction;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.main.trace.HLASMTracer;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;
import org.jgrapht.Graph;

import java.util.List;

public class TraceCFGMain {

    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        String filePath = "/Users/asgupta/code/asmcode/Root module/Root module_PSUNA02.txt";
//        String filePath = "/Users/asgupta/code/hlasm/test.txt";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure(filePath, "/Users/asgupta/code/asmcode/Assembler Copybook", "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");
        Graph<TranspilerInstruction, TypedGraphEdge> cfg = analysisResult.controlFlowGraph();
//        cfg.vertexSet().forEach(instr -> System.out.println(instr.text()));
        HLASMTracer tracer = new HLASMTracer();
        List<TranspilerInstruction> trace = tracer.run(cfg);

//        Advisor advisor = new Advisor(OpenAICredentials.fromEnv());
//        String fullTrace = trace.stream().map(CodeElement::text).collect(Collectors.joining("\n"));
//        List<String> response = advisor.advise(String.format("Assume you are a senior mainframe engineer working to understand a legacy system. The following is a trace of an HLASM program which ran. Explain in detail what happened? ```%s```", fullTrace));
//        response.forEach(System.out::println);
//        trace.forEach(e -> {
//            List<String> response = advisor.advise(String.format("This is the current instruction in a trace: ```%s```. The history of what has happened is also listed. Update your theory about what is happening at an abstract level. The history is: ```%s```", e.text(),
//                    String.join("\n", memory)));
//            String responseAsString = String.join("\n", response);
//            System.out.printf("Response: %s%n", responseAsString);
//            memory.add(responseAsString);
//        });
        System.out.println("COMPLETE!");
    }
}
