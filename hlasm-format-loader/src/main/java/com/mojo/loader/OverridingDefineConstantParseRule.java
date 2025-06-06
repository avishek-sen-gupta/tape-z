package com.mojo.loader;

public record OverridingDefineConstantParseRule() implements GeneratedParseRule {
    @Override
    public String toString() {
        return String.format("'DC' %s", "constant (COMMA constant)*").trim();
    }

    @Override
    public HLASMInstructionFormat format() {
        return null;
    }

    @Override
    public String opcode() {
        return "DC";
    }
}
