package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.AsmCodeElement;
import com.mojo.loader.code.CodeElement;
import com.mojo.transpiler.hlasm.AsmTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementAsmBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;

    public CodeElementAsmBuilderVisitor(NodeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.AsmContext ctx = (HlasmMacroBlockParserParser.AsmContext) node;
        CodeElement retrieved = elementFromRegistry(ctx.GUID().getText(), AsmCodeElement.class, registry);
        AsmCodeElement retrievedAsm = (AsmCodeElement) retrieved;

        return new AsmTranspilerNode(ctx.GUID().getText(), retrievedAsm.getOriginalCodeElement(), retrievedAsm.getAsmGraph(), retrievedAsm.getTree(), retrievedAsm.getInstructionTree());
    }
}
