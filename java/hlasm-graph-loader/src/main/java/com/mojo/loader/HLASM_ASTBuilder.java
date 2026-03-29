package com.mojo.loader;

import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmParserLexer;
import com.mojo.hlasm.HlasmParserParser;
import com.mojo.loader.code.AsmCodeElement;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.EmptyLineElement;
import com.mojo.loader.code.RawCodeElement;
import com.mojo.loader.graph.JGraphTBuilderHLASMVisitor;
import com.mojo.algorithms.domain.TypedGraphEdge;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.Graph;

public class HLASM_ASTBuilder {
    private final IdProvider idProvider;

    public HLASM_ASTBuilder(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public CodeElement run(RawCodeElement codeElement) {
        ParseTree instructionTree = parsed(codeElement.text());
        ErrorNodeVisitor errorVisitor = new ErrorNodeVisitor();
        instructionTree.accept(errorVisitor);
        if (errorVisitor.isError()) return codeElement;
        if ("".equals(codeElement.text().trim())) return new EmptyLineElement(idProvider.next(), codeElement);
        JGraphTBuilderHLASMVisitor parseTreeVisitor = new JGraphTBuilderHLASMVisitor(idProvider);
        ASTNodeBuilderHLASMVisitor parseTreeVisitor2 = new ASTNodeBuilderHLASMVisitor(idProvider);
        instructionTree.accept(parseTreeVisitor);
        instructionTree.accept(parseTreeVisitor2);
        Graph<TypedGraphVertex, TypedGraphEdge> asmGraph = parseTreeVisitor.getAsmGraph();
        return new AsmCodeElement(idProvider.next(), codeElement, asmGraph, parseTreeVisitor2.getRoot(), instructionTree);
    }

    private ParseTree parsed(String hlasmInstruction) {
        CharStream charStream = CharStreams.fromString(hlasmInstruction);
        HlasmParserLexer lexer = new HlasmParserLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HlasmParserParser parser = new HlasmParserParser(tokens);
        parser.removeErrorListeners();
        lexer.removeErrorListeners();
        return parser.startRule();
    }
}
