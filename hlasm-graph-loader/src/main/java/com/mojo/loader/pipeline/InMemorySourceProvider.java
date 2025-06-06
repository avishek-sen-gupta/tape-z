package com.mojo.loader.pipeline;

import java.util.List;

public class InMemorySourceProvider implements SourceProvider {
    private final String sourceName;
    private final List<String> lines;

    public InMemorySourceProvider(String sourceName, List<String> lines) {
        this.sourceName = sourceName;
        this.lines = lines;
    }

    @Override
    public List<String> getText() {
        return lines;
    }

    @Override
    public String sourceName() {
        return sourceName;
    }
}
