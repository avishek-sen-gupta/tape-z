package com.mojo.loader.pipeline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileSourceProvider implements SourceProvider {

    private final String filePath;

    public FileSourceProvider(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<String> getText() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sourceName() {
        return filePath;
    }
}
