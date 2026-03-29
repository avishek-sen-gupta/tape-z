package com.mojo.loader;

public class OptionalRuleAtom extends RuleAtom {
    public OptionalRuleAtom(String atom) {
        super(atom);
    }

    @Override
    public String asString(String separator) {
        return super.asString(separator) + "?";
    }
}
