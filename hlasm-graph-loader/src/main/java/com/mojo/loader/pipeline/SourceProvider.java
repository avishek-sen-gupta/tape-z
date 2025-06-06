package com.mojo.loader.pipeline;

import java.util.List;

public interface SourceProvider {
    List<String> getText();

    String sourceName();
}
