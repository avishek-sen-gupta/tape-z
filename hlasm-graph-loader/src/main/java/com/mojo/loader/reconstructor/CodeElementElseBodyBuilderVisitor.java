package com.mojo.loader.reconstructor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojo.algorithms.domain.SemanticCategory;
import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.TranspilerCodeBlockNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import org.antlr.v4.runtime.tree.RuleNode;

public class CodeElementElseBodyBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public CodeElementElseBodyBuilderVisitor(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.ElseBlockContext ctx = (HlasmMacroBlockParserParser.ElseBlockContext) node;
        return ctx.body().accept(new CodeElementBodyBuilderVisitor(registry, idProvider, SemanticCategory.IF_ELSE_BODY));
//        return new ElseBlock(idProvider.next(), (CodeBlockElement) ctx.body().accept(new CodeElementBodyBuilderVisitor(registry, idProvider)), ifBlockID);
    }

    public TranspilerNode empty() {
        return new TranspilerCodeBlockNode(ImmutableList.of(), ImmutableMap.of(), ImmutableList.of(SemanticCategory.IF_ELSE_BODY));
    }
}
