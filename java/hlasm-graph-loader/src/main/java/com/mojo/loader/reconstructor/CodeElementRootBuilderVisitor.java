package com.mojo.loader.reconstructor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.LabelledTranspilerCodeBlockNode;
import com.mojo.algorithms.transpiler.NullTranspilerNode;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import org.antlr.v4.runtime.tree.RuleNode;

import java.util.List;

public class CodeElementRootBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public CodeElementRootBuilderVisitor(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.StartRuleContext ctx = (HlasmMacroBlockParserParser.StartRuleContext) node;

        List<TranspilerNode> typedStatements = ctx.statement().stream().map(this::typedStatement).toList();
        return new TranspilerCodeBlockNode(typedStatements, ImmutableMap.of(), ImmutableList.of(SemanticCategory.CODE_ROOT));
    }

    private TranspilerNode typedStatement(HlasmMacroBlockParserParser.StatementContext stmt) {
        if (stmt.asm() != null) return stmt.asm().accept(new CodeElementAsmBuilderVisitor(registry));
        else if (stmt.raw() != null) return stmt.raw().accept(new CodeElementRawBuilderVisitor(registry));
        else if (stmt.sql() != null) return stmt.sql().accept(new CodeElementSqlBuilderVisitor(registry));
        else if (stmt.comment() != null) return stmt.comment().accept(new CodeElementCommentBuilderVisitor(registry, idProvider));
        else if (stmt.ifBlock() != null) return stmt.ifBlock().accept(new CodeElementIfBlockBuilderVisitor(registry, idProvider));
        else if (stmt.labelStart() != null) return stmt.labelStart().accept(new CodeElementLabelStartBuilderVisitor(registry, idProvider));
        else if (stmt.labelEnd() != null) return stmt.labelEnd().accept(new CodeElementLabelEndBuilderVisitor(registry, idProvider));
        else if (stmt.macro() != null) return stmt.macro().accept(new CodeElementMacroBuilderVisitor(registry, idProvider));
        else if (stmt.call() != null) return stmt.call().accept(new CodeElementCallBuilderVisitor(registry));
        else return new NullTranspilerNode();
    }
}
