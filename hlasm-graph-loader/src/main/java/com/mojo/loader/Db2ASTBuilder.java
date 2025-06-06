package com.mojo.loader;

import com.mojo.algorithms.domain.TypedGraphVertex;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.db2.Db2SqlLexer;
import com.mojo.db2.Db2SqlParser;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.RawCodeElement;
import com.mojo.loader.code.SqlCodeElement;
import com.mojo.loader.graph.JGraphTBuilderDB2Visitor;
import com.mojo.algorithms.domain.TypedGraphEdge;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jgrapht.Graph;

public class Db2ASTBuilder {
    private final IdProvider idProvider;

    public Db2ASTBuilder(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public CodeElement run(CodeElement element) {
        String line = ((RawCodeElement) element).getLine();
        String experimental = line.replaceFirst("EXEC", "    ").replaceFirst("SQL", "    ");

        ParseTree instructionTree = parsed(experimental);
        ErrorNodeVisitor errorVisitor = new ErrorNodeVisitor();
        instructionTree.accept(errorVisitor);
        if (errorVisitor.isError()) {
            return element;
        }
        JGraphTBuilderDB2Visitor parseTreeVisitor = new JGraphTBuilderDB2Visitor(idProvider);
        ASTNodeBuilderDB2Visitor parseTreeVisitor2 = new ASTNodeBuilderDB2Visitor(idProvider);
        instructionTree.accept(parseTreeVisitor);
        instructionTree.accept(parseTreeVisitor2);
        Graph<TypedGraphVertex, TypedGraphEdge> db2Graph = parseTreeVisitor.getDb2Graph();
        ParseNode db2Tree = parseTreeVisitor2.getRoot();
        return new SqlCodeElement(idProvider.next(), line, db2Graph, db2Tree);
    }

    public ParseTree parsed(String db2String) {
        CharStream charStream = CharStreams.fromString(db2String);
        Db2SqlLexer lexer = new Db2SqlLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Db2SqlParser parser = new Db2SqlParser(tokens);
        return parser.startSqlRule();
    }
}
