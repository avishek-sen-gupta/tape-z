package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.MacroElement;
import com.mojo.loader.NodeRegistry;
import com.mojo.transpiler.hlasm.MacroTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementMacroBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public CodeElementMacroBuilderVisitor(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.MacroContext ctx = (HlasmMacroBlockParserParser.MacroContext) node;
        CodeElement retrieved = elementFromRegistry(ctx.GUID().getText(), MacroElement.class, registry);
        MacroElement retrievedRaw = (MacroElement) retrieved;
        return new MacroTranspilerNode(ctx.GUID().getText(), retrievedRaw.text());
//        return new MacroElement(ctx.GUID().getText(), retrievedRaw.text());
    }
}
