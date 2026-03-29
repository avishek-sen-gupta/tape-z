package com.mojo.loader.reconstructor;

import com.mojo.algorithms.domain.TranspilerNode;
import com.mojo.hlasm.HlasmMacroBlockParserBaseVisitor;
import com.mojo.hlasm.HlasmMacroBlockParserParser;
import com.mojo.loader.NodeRegistry;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.SqlCodeElement;
import com.mojo.transpiler.hlasm.SqlTranspilerNode;
import org.antlr.v4.runtime.tree.RuleNode;

import static com.mojo.loader.reconstructor.RegistryUtils.elementFromRegistry;

public class CodeElementSqlBuilderVisitor extends HlasmMacroBlockParserBaseVisitor<TranspilerNode> {
    private final NodeRegistry registry;

    public CodeElementSqlBuilderVisitor(NodeRegistry registry) {
        this.registry = registry;
    }

    @Override
    public TranspilerNode visitChildren(RuleNode node) {
        HlasmMacroBlockParserParser.SqlContext ctx = (HlasmMacroBlockParserParser.SqlContext) node;

        CodeElement retrieved = elementFromRegistry(ctx.GUID().getText(), SqlCodeElement.class, registry);
        SqlCodeElement retrievedSql = (SqlCodeElement) retrieved;
        return new SqlTranspilerNode(ctx.GUID().getText(), retrievedSql.text(), retrievedSql.getDb2Graph(), retrievedSql.getTree());
    }
}
