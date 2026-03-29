package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.*;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.RawCodeElement;
import com.mojo.transpiler.hlasm.RawTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementRawBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;

    public CodeElementRawBuilderVisitor(NodeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.RawContext ctx = (HlasmMacroBlockParserParser.RawContext) node;

        CodeElement retrieved = elementFromRegistry(ctx.GUID().getText(), RawCodeElement.class, registry);
        RawCodeElement retrievedRaw = (RawCodeElement) retrieved;
        return new RawTranspilerNode(ctx.GUID().getText(), retrievedRaw.text());
    }
}
