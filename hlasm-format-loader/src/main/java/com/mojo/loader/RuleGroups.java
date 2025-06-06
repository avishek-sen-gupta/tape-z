package com.mojo.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.mojo.loader.GroupUtil.separated;

public class RuleGroups implements RuleGroup {
    private final List<RuleGroup> groups = new ArrayList<>();

    @Override
    public void add(RuleGroup group) {
        groups.add(group);
    }

    @Override
    public String asString(String separator) {
        if (groups.isEmpty()) return "";
        if (groups.size() == 1) return groups.get(0).asString("");
        List<String> list = Stream.concat(Stream.of(groups.getFirst().asString("")), groups.subList(1, groups.size()).stream().map(g -> g.asString(RuleConstants.COMMA))).toList();
        return separated(separator, String.join(" ", list));
    }
}
