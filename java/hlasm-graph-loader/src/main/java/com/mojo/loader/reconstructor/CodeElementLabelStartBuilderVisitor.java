package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.LabelledCodeBlockElement;
import org.antlr.v4.runtime.tree.RuleNode;
import com.mojo.transpiler.hlasm.SectionStartTranspilerNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementLabelStartBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;
    private final IdProvider idProvider;

    public CodeElementLabelStartBuilderVisitor(NodeRegistry registry, IdProvider idProvider) {
        this.registry = registry;
        this.idProvider = idProvider;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.LabelStartContext ctx = (HlasmMacroBlockParserParser.LabelStartContext) node;
        LabelledCodeBlockElement block = (LabelledCodeBlockElement) elementFromRegistry(ctx.GUID().getText(), LabelledCodeBlockElement.class, registry);
        return new SectionStartTranspilerNode(idProvider.next(), block.getLabel(), block);
//        return new LabelStartElement(idProvider.next(), block.getLabel(), block.getId());
    }
}
