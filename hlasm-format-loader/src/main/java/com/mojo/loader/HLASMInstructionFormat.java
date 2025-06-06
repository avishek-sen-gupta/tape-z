package com.mojo.loader;

import org.antlr.v4.runtime.tree.ParseTree;

public record HLASMInstructionFormat(String opcode, String operandFormat, ParseTree operandFormatTree,
                                     String description) {
}
