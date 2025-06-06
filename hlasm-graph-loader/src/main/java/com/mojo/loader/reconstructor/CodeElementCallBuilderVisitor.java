package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.transpiler.CallTranspilerNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.CallExternalCustomElement;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.MacroElement;
import com.mojo.transpiler.hlasm.CallParamsTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementCallBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;

    public CodeElementCallBuilderVisitor(NodeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.CallContext ctx = (HlasmMacroBlockParserParser.CallContext) node;
        CodeElement retrieved = elementFromRegistry(ctx.GUID().getText(), CallExternalCustomElement.class, registry);
        CallExternalCustomElement retrievedRaw = (CallExternalCustomElement) retrieved;
        return new CallTranspilerNode(new CallParamsTranspilerNode(retrievedRaw.getDestination(), retrievedRaw.getParams(), retrievedRaw.text()));
//        return new CallExternalCustomElement(ctx.GUID().getText(), retrievedRaw.getDestination(), retrievedRaw.getParams(), retrievedRaw.text());
    }
}
