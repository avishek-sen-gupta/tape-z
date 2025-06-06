package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.*;
import com.mojo.loader.code.CommentElement;
import com.mojo.loader.transpiler.hlasm.CommentTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementCommentBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public CodeElementCommentBuilderVisitor(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.CommentContext ctx = (HlasmMacroBlockParserParser.CommentContext) node;

        CommentElement comment = (CommentElement) elementFromRegistry(ctx.GUID().getText(), CommentElement.class, registry);
        return new CommentTranspilerNode(idProvider.next(), comment.text());
    }
}
