package com.mojo.loader;

import com.mojo.hlasm.HlasmFormatParserParser;
import com.mojo.hlasm.HlasmFormatParserVisitor;
import lombok.Getter;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HLASMParseRuleBuilderVisitor<T> extends AbstractParseTreeVisitor<T> implements HlasmFormatParserVisitor<T> {
    @Getter
    private final RuleGroup finalRules;
    private int order = 1;
    private final Map<Pair<Integer, String>, String> operands;
    @Getter private final List<String> localOperands =  new ArrayList<>();

    public HLASMParseRuleBuilderVisitor(Map<Pair<Integer, String>, String> operands) {
        this.finalRules = new RuleGroups();
        this.operands = operands;
    }

    @Override
    public T visitStartRule(HlasmFormatParserParser.StartRuleContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitControl_register(HlasmFormatParserParser.Control_registerContext ctx) {
        finalRules.add(new RuleAtom(addOperand("controlRegister")));
        return null;
    }

    private String addOperand(String operandType) {
        Pair<Integer, String> key = ImmutablePair.of(order, operandType);
        if (operands.containsKey(key)) {
            localOperands.add(operands.get(key));
            order++;
            return operands.get(key);
        }
        operands.put(key, String.format("operand_%s_%s", order, operandType));
        localOperands.add(operands.get(key));
        order++;
        return operands.get(key);
    }

    @Override
    public T visitAccess_register(HlasmFormatParserParser.Access_registerContext ctx) {
        finalRules.add(new RuleAtom(addOperand("accessRegister")));
        return null;
    }

    @Override
    public T visitFloating_point_register_pair(HlasmFormatParserParser.Floating_point_register_pairContext ctx) {
        finalRules.add(new RuleAtom(addOperand("floatingPointRegisterPair")));
        return null;
    }

    @Override
    public T visitFloating_point_register(HlasmFormatParserParser.Floating_point_registerContext ctx) {
        finalRules.add(new RuleAtom(addOperand("floatingPointRegister")));
        return null;
    }

    @Override
    public T visitIndex_register(HlasmFormatParserParser.Index_registerContext ctx) {
        finalRules.add(new RuleAtom(addOperand("indexRegister")));
        return null;
    }

    @Override
    public T visitBase_register(HlasmFormatParserParser.Base_registerContext ctx) {
        finalRules.add(new RuleAtom(addOperand("baseRegister")));
        return null;
    }

    @Override
    public T visitRegister_pair(HlasmFormatParserParser.Register_pairContext ctx) {
        finalRules.add(new RuleAtom(addOperand("registerPair")));
        return null;
    }

    @Override
    public T visitRegister_operand(HlasmFormatParserParser.Register_operandContext ctx) {
        finalRules.add(new RuleAtom(addOperand("register")));
        return null;
    }

    @Override
    public T visitImmediate_value(HlasmFormatParserParser.Immediate_valueContext ctx) {
        finalRules.add(new RuleAtom(addOperand("immediateValue")));
        return null;
    }

    @Override
    public T visitSigned_immediate_value(HlasmFormatParserParser.Signed_immediate_valueContext ctx) {
        finalRules.add(new RuleAtom(addOperand("signedImmediateValue")));
        return null;
    }

    @Override
    public T visitLength_field(HlasmFormatParserParser.Length_fieldContext ctx) {
        finalRules.add(new RuleAtom((addOperand("lengthField"))));
        return null;
    }

    @Override
    public T visitMask_field(HlasmFormatParserParser.Mask_fieldContext ctx) {
        finalRules.add(new RuleAtom(addOperand("maskField")));
        return null;
    }

    @Override
    public T visitRelative_immediate_operand(HlasmFormatParserParser.Relative_immediate_operandContext ctx) {
        finalRules.add(new RuleAtom(addOperand("relativeImmediateOperand")));
        return null;
    }

    @Override
    public T visitVector_register_pair(HlasmFormatParserParser.Vector_register_pairContext ctx) {
        finalRules.add(new RuleAtom(addOperand("vectorRegisterPair")));
        return null;
    }

    @Override
    public T visitVector_register(HlasmFormatParserParser.Vector_registerContext ctx) {
        finalRules.add(new RuleAtom(addOperand("vectorRegister")));
        return null;
    }

    @Override
    public T visitDisplacement_offset(HlasmFormatParserParser.Displacement_offsetContext ctx) {
        return null;
    }

    @Override
    public T visitDisplacement_reference(HlasmFormatParserParser.Displacement_referenceContext ctx) {
        return null;
    }

    @Override
    public T visitDisplacement_bits(HlasmFormatParserParser.Displacement_bitsContext ctx) {
        return null;
    }

    // TODO: Break into separate cases to deduce which rules are applicable in which case
    @Override
    public T visitDisplacement(HlasmFormatParserParser.DisplacementContext ctx) {
        finalRules.add(new RuleAtom(addOperand("displacement")));
        return null;
    }

    @Override
    public T visitOptionalMaskField(HlasmFormatParserParser.OptionalMaskFieldContext ctx) {
        finalRules.add(new OptionalRuleAtom(addOperand("maskField")));
        return null;
    }

    @Override
    public T visitOptionalSignedImmediateValue(HlasmFormatParserParser.OptionalSignedImmediateValueContext ctx) {
        finalRules.add(new OptionalRuleAtom(addOperand("signedImmediateValue")));
        return null;
    }

    @Override
    public T visitOptionalRegister(HlasmFormatParserParser.OptionalRegisterContext ctx) {
        finalRules.add(new OptionalRuleAtom(addOperand("register")));
        return null;
    }

    @Override
    public T visitOperands(HlasmFormatParserParser.OperandsContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitOperand(HlasmFormatParserParser.OperandContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public T visitExpression(HlasmFormatParserParser.ExpressionContext ctx) {
        return null;
    }
}
