package com.mojo.loader.reconstructor;

import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.CodeElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistryUtils {
    private static final Logger logger = LoggerFactory.getLogger(RegistryUtils.class);
    public static CodeElement elementFromRegistry(String id, Class expectedType, NodeRegistry registry) {
        CodeElement codeElement = registry.get(id);
        if (!expectedType.getSimpleName().equals(codeElement.getClass().getSimpleName())) {
            logger.warn(String.format("WARNING: Types do not match, expected: %s, actual: %s...%n", expectedType.getSimpleName(), codeElement.getClass().getSimpleName()));
            logger.debug(String.format("WARNING: Retrieved text: %s and ID: %s ...%n", codeElement.text(), codeElement.getId()));
        }
        return codeElement;
    }
}
