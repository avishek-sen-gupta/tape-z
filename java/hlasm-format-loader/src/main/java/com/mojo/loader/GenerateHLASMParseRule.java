package com.mojo.loader;

import java.util.List;

public record GenerateHLASMParseRule(HLASMInstructionFormat format, RuleGroup rules, List<String> localOperands) implements GeneratedParseRule {
    @Override
    public String toString() {
        return String.format("'%s' %s", format.opcode(), rules.asString("")).trim();
    }

    @Override
    public String opcode() {
        return format.opcode();
    }
}
