package com.mojo.loader.main;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.TypedGraphEdge;
import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.hlasm.HlasmParserLexer;
import com.mojo.hlasm.HlasmParserParser;
import com.mojo.loader.graph.JGraphTBuilderHLASMVisitor;
import com.mojo.woof.GraphSDK;
import com.mojo.woof.Neo4JDriverBuilder;
import com.mojo.woof.WoofEdge;
import com.mojo.woof.WoofNode;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GrammarDemoMain {
    public static void main(String[] args) {

        String filePath = "/Users/asgupta/Documents/example.txt";
        List<Graph<TypedGraphVertex, TypedGraphEdge>> blockGraph = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;


            while ((line = reader.readLine()) != null) {
                ParseTree instructionTree = parsed(line);
                JGraphTBuilderHLASMVisitor parseTreeVisitor = new JGraphTBuilderHLASMVisitor(new UUIDProvider());
                instructionTree.accept(parseTreeVisitor);
                Graph<TypedGraphVertex, TypedGraphEdge> asmGraph = parseTreeVisitor.getAsmGraph();
                blockGraph.add(asmGraph);

                System.out.println("Parsed..." + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WoofNode rootNode = new WoofNode(ImmutableMap.of("uuid", "0", "type", "ROOT", "text", "HLASM_ROOT"), ImmutableList.of());

        List<WoofNode> allNodes = new ArrayList<>();
        allNodes.add(rootNode);
        List<WoofEdge> allEdges = new ArrayList<>();
        blockGraph.forEach(instructionGraph -> {
            List<WoofNode> graphNodes = instructionGraph.vertexSet().stream()
                    .map(v -> new WoofNode(ImmutableMap.of("uuid", v.id(), "text", v.text(), "type", v.type()), ImmutableList.of())).toList();

            List<WoofEdge> graphEdges = instructionGraph.edgeSet().stream()
                    .map(v -> new WoofEdge(
                            new WoofNode(ImmutableMap.of("uuid", instructionGraph.getEdgeSource(v).id()), ImmutableList.of()),
                            new WoofNode(ImmutableMap.of("uuid", instructionGraph.getEdgeTarget(v).id()), ImmutableList.of()),
                            ImmutableMap.of(),
                            ImmutableList.of(v.getRelationshipType())
                    )).toList();
            allNodes.addAll(graphNodes);
            allEdges.addAll(graphEdges);
            Optional<WoofNode> instructionRoot = graphNodes.stream().filter(gn -> gn.getProperties().get("type").equals("StartRuleContext")).findFirst();
            allEdges.add(new WoofEdge(rootNode, instructionRoot.get(), ImmutableMap.of(), ImmutableList.of("CONTAINS")));
        });

        System.out.println("COMPLETE");

        try (GraphSDK graphSDK = new GraphSDK(new Neo4JDriverBuilder().fromEnv())) {
            graphSDK.addGraph(allNodes, allEdges);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ParseTree parsed(String hlasmInstruction) {
        CharStream charStream = CharStreams.fromString(hlasmInstruction);
        HlasmParserLexer lexer = new HlasmParserLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HlasmParserParser parser = new HlasmParserParser(tokens);
        return parser.startRule();
    }
}
