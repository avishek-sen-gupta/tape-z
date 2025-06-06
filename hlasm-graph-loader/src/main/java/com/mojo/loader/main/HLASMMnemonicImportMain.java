package com.mojo.loader.main;

import com.mojo.loader.HLASMInstructionFormat;
import com.mojo.loader.HLASMInstructionFormatBuilder;

import java.util.stream.Stream;

public class HLASMMnemonicImportMain {
    public static void main(String[] args) {
        String formatFileName = "/Users/asgupta/code/tape-z/hlasm-parser/instruction_formats/HLASM Instruction Format.csv";
        Stream<String> hlasmInstructionMnemonics = new HLASMInstructionFormatBuilder().formatFromCSV(formatFileName).map(HLASMMnemonicImportMain::asMnemonic);
        hlasmInstructionMnemonics.forEach(System.out::println);
    }

    private static String asMnemonic(HLASMInstructionFormat hlasmInstructionFormat) {
        return hlasmInstructionFormat.opcode();
    }
}
