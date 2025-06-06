package com.mojo.loader.pass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcludeCobolResolutionStrategy implements ExternalCallResolutionStrategy {
    private static final Logger logger = LoggerFactory.getLogger(ExcludeCobolResolutionStrategy.class);
    private final String searchPath;

    public ExcludeCobolResolutionStrategy(String searchPath) {
        this.searchPath = searchPath;
    }

    @Override
    public Optional<String> run(String externalSymbol) {
        Path directory = Paths.get(searchPath);
        try {
            List<Path> resolvedPaths = new ArrayList<>();
            DirectoryStream<Path> paths = Files.newDirectoryStream(directory, externalSymbol + "*.*");
            for (Path path : paths) {
                String filename = path.getFileName().toString();
                if (filename.contains("COBOL") || filename.contains("COBSQL")) continue;
                resolvedPaths.add(path);
            }

            logger.debug(resolvedPaths.toString());
            if (resolvedPaths.isEmpty()) return Optional.empty();
            String actualProgramFilePath = resolvedPaths.getFirst().toString();
            return Optional.of(actualProgramFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
