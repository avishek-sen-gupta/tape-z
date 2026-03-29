package com.mojo.loader;

import java.util.List;

public class FileMnemonicsLoader implements MnemonicsLoader {
    public static final String HLASM_INSTRUCTION_FORMAT_PATH = "hlasm-parser/instruction_formats/HLASM Instruction Format.csv";

    private String asMnemonic(HLASMInstructionFormat hlasmInstructionFormat) {
        return hlasmInstructionFormat.opcode();
    }

    @Override
    public List<String> mnemonics() {
        return new HLASMInstructionFormatBuilder().formatFromCSV(HLASM_INSTRUCTION_FORMAT_PATH).map(this::asMnemonic).toList();
    }
}
