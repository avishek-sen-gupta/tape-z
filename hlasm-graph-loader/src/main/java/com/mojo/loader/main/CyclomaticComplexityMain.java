package com.mojo.loader.main;

import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.HLASMInstructionFormat;
import com.mojo.loader.HLASMInstructionFormatBuilder;
import com.mojo.loader.pipeline.HlasmCodeAnalysis;
import com.mojo.loader.pipeline.HlasmCodeAnalysisResult;

import java.util.List;

public class CyclomaticComplexityMain {

    public static final String HLASM_INSTRUCTION_FORMAT_PATH = "hlasm-parser/instruction_formats/HLASM Instruction Format.csv";

    public static void main(String[] args) {
//        String filePath = "/Users/asgupta/Documents/example.txt";
        HlasmCodeAnalysisResult analysisResult = new HlasmCodeAnalysis(new UUIDProvider()).structure("/Users/asgupta/code/hlasm/test.txt", "/Users/asgupta/code/asmcode/Assembler Copybook", "/Users/asgupta/code/asmcode/Utilities_Assembler and Cobol");
        analysisResult.complexitiesByLabel().forEach(e -> System.out.println(e.getLeft() + " = " + e.getRight()));
    }

    private static String asMnemonic(HLASMInstructionFormat hlasmInstructionFormat) {
        return hlasmInstructionFormat.opcode();
    }

    public static List<String> mnemonics() {
        return new HLASMInstructionFormatBuilder().formatFromCSV(HLASM_INSTRUCTION_FORMAT_PATH).map(CyclomaticComplexityMain::asMnemonic).toList();
    }
}
