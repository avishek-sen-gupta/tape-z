package com.mojo.loader;

public record GenerateHLASMParseRule(HLASMInstructionFormat format, RuleGroup rules, java.util.List<String> localOperands) implements GeneratedParseRule {
    @Override
    public String toString() {
        return String.format("'%s' %s", format.opcode(), rules.asString("")).trim();
    }

    @Override
    public String opcode() {
        return format.opcode();
    }
}
