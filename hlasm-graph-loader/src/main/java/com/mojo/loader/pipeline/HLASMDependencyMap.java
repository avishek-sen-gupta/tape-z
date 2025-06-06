package com.mojo.loader.pipeline;

import com.mojo.algorithms.domain.TypedGraphEdge;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public class HLASMDependencyMap {
    private final Map<String, HlasmCodeAnalysisResult> resolvedDependencies = new HashMap<>();
    private final Graph<String, TypedGraphEdge> dependencyGraph = new DirectedPseudograph<>(TypedGraphEdge.class);

    public void addCallDependency(String src, String dest) {
        if (!dependencyGraph.vertexSet().contains(src))
            dependencyGraph.addVertex(src);
        if (!dependencyGraph.vertexSet().contains(dest))
            dependencyGraph.addVertex(dest);
        dependencyGraph.addEdge(src, dest, new TypedGraphEdge("DEPENDS_ON", "NONE"));
    }

    public void put(String programPath, HlasmCodeAnalysisResult analysisReswult) {
        resolvedDependencies.put(programPath, analysisReswult);
    }

    public boolean containsKey(String programPath) {
        return  resolvedDependencies.containsKey(programPath);
    }

    public Set<String> dependencySymbols() {
        return resolvedDependencies.keySet();
    }
}
