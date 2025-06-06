package com.mojo.loader.reconstructor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.NullTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;

public class CodeElementBodyBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;
    private final SemanticCategory blockIdentity;

    public CodeElementBodyBuilderVisitor(NodeRegistry registry, IdProvider idProvider, SemanticCategory blockIdentity) {
        this.registry = registry;
        this.idProvider = idProvider;
        this.blockIdentity = blockIdentity;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        List<TranspilerNode> bodyStatements = ((HlasmMacroBlockParserParser.BodyContext) node).statement().stream().map(this::typedStatement).toList();
        return new TranspilerCodeBlockNode(bodyStatements, ImmutableMap.of(), ImmutableList.of(blockIdentity));
//        return new CodeBlockElement(idProvider.next(), "BLOCK", bodyStatements);
    }

    private TranspilerNode typedStatement(HlasmMacroBlockParserParser.StatementContext stmt) {
        if (stmt.asm() != null) return stmt.asm().accept(new CodeElementAsmBuilderVisitor(registry));
        else if (stmt.raw() != null) return stmt.raw().accept(new CodeElementRawBuilderVisitor(registry));
        else if (stmt.sql() != null) return stmt.sql().accept(new CodeElementSqlBuilderVisitor(registry));
        else if (stmt.comment() != null) return stmt.comment().accept(new CodeElementCommentBuilderVisitor(registry, idProvider));
        else if (stmt.ifBlock() != null) return stmt.ifBlock().accept(new CodeElementIfBlockBuilderVisitor(registry, idProvider));
        else if (stmt.labelStart() != null) return stmt.labelStart().accept(new CodeElementLabelStartBuilderVisitor(registry, idProvider));
        else if (stmt.labelEnd() != null) return stmt.labelEnd().accept(new CodeElementLabelEndBuilderVisitor(registry, idProvider));
        else return new NullTranspilerNode();
//        else return new NullCodeElement(idProvider.next());
    }
}
