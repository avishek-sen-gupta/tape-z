package com.mojo.loader;

public interface RuleGroup {
    void add(RuleGroup group);
    String asString(String separator);
}
