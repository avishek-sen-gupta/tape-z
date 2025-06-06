package com.mojo.loader;

import com.mojo.loader.code.CodeElement;

import java.util.Map;

public class NodeRegistry {
    private final Map<String, CodeElement> nodeMap;

    public NodeRegistry(Map<String, CodeElement> nodeMap) {
        this.nodeMap = nodeMap;
    }

    public static NodeRegistry build(CodeElement element) {
        NodeRegistryBuilderVisitor visitor = new NodeRegistryBuilderVisitor();
        element.accept(visitor);
        return new NodeRegistry(visitor.getNodeMap());
    }

    public CodeElement get(String id) {
        return nodeMap.get(id);
    }
}
