package com.mojo.loader.pass;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.truncate;

public class DiscardAfter72Pass {
    public List<String> run(List<String> lines) {
        return lines.stream().map(l -> truncate(l, 72)).toList();
    }
}
