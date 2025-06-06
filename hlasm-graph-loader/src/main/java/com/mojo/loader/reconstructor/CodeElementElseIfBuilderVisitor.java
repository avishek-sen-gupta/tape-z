package com.mojo.loader.reconstructor;

import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.loader.*;
import com.mojo.loader.code.CodeElement;
import org.antlr.v4.runtime.tree.RuleNode;

public class CodeElementElseIfBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<CodeElement> {
    private final NodeRegistry registry;
    private final String ifBlockID;

    public CodeElementElseIfBuilderVisitor(NodeRegistry registry, String ifBlockID) {
        this.registry = registry;
        this.ifBlockID = ifBlockID;
    }

    @Override
    public CodeElement visitChildren(RuleNode node) {
//        HlasmMacroBlockParserParser.ElseIfBlockContext ctx = (HlasmMacroBlockParserParser.ElseIfBlockContext) node;
//        return new ElseIfBlock(new UUIDProvider().uuid(), ctx.ifBlock().accept(new CodeElementIfBlockBuilderVisitor(registry)), ifBlockID);
        return null;
    }
}
