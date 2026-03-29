package com.mojo.loader.pass;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmMacroBlockParserLexer;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.AbstractTextPrinter;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.reconstructor.CodeElementRootBuilderVisitor;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class StructuredMacroParsePass {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public StructuredMacroParsePass(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    public TranspilerNode run(CodeElement element) {
        AbstractTextPrinter visitor = new AbstractTextPrinter();
        element.accept(visitor);
        String output = visitor.getOutput();
        ParseTree parsed = parsed(output);
        CodeElementRootBuilderVisitor codeElementBuilderVisitor = new CodeElementRootBuilderVisitor(registry, idProvider);
        return parsed.accept(codeElementBuilderVisitor);
    }

    private ParseTree parsed(String abstractCode) {
        CharStream charStream = CharStreams.fromString(abstractCode);
        HlasmMacroBlockParserLexer lexer = new HlasmMacroBlockParserLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HlasmMacroBlockParserParser parser = new HlasmMacroBlockParserParser(tokens);
        return parser.startRule();
    }
}
