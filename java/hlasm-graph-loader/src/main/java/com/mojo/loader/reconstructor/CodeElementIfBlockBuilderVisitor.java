package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.transpiler.IfTranspilerNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.IfStatementElement;
import com.mojo.transpiler.hlasm.RawTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementIfBlockBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public CodeElementIfBlockBuilderVisitor(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.IfBlockContext ctx = (HlasmMacroBlockParserParser.IfBlockContext) node;

        IfStatementElement sourceIf = (IfStatementElement) elementFromRegistry(ctx.if_().GUID().getText(), IfStatementElement.class, registry);
        CodeElementIfThenBodyBuilderVisitor ifThenBodyBuilderVisitor = new CodeElementIfThenBodyBuilderVisitor(registry, idProvider);
        CodeElementElseBodyBuilderVisitor elseBodyBuilderVisitor = new CodeElementElseBodyBuilderVisitor(registry, idProvider);

        TranspilerNode ifBody = ctx.body() != null ? ctx.body().accept(ifThenBodyBuilderVisitor) : ifThenBodyBuilderVisitor.empty();
        TranspilerNode elseBody = ctx.elseBlock() != null ? ctx.elseBlock().accept(elseBodyBuilderVisitor) : elseBodyBuilderVisitor.empty();

        return new IfTranspilerNode(new RawTranspilerNode(idProvider.next(), sourceIf.getCondition()), ifBody, elseBody);
//        return new IfBlockElement(ifBlockID, sourceIf.getCondition(), (IfBodyElement) ifBody, elseIfBodies, (ElseBlock) elseBody);
    }
}
