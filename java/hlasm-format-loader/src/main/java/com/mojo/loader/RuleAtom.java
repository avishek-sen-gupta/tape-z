package com.mojo.loader;

import static com.mojo.loader.GroupUtil.separated;

public class RuleAtom implements RuleGroup {
    private final String atom;

    public RuleAtom(String atom) {
        this.atom = atom;
    }

    @Override
    public void add(RuleGroup group) {

    }

    @Override
    public String asString(String separator) {
        return separated(separator, atom);
    }
}
