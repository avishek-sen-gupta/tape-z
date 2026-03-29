package com.mojo.loader;

public record OverridingDefineStructureParseRule() implements GeneratedParseRule {
    @Override
    public String toString() {
        return String.format("'DS' %s", "typeSpec (COMMA typeSpec)*").trim();
    }

    @Override
    public HLASMInstructionFormat format() {
        return null;
    }

    @Override
    public String opcode() {
        return "DS";
    }
}
