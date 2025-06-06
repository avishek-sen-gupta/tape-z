grammar HlasmFormatParser;
import HlasmFormatLexer;
@header {package com.mojo.hlasm;}

options {tokenVocab = HlasmFormatLexer;}

// Parser Rules
startRule: operands;

control_register: CONTROL_REGISTER;
access_register: ACCESS_REGISTER;
floating_point_register_pair: FLOATING_POINT_REGISTER_PAIR;
floating_point_register: FLOATING_POINT_REGISTER;
index_register: INDEX_REGISTER;
base_register: BASE_REGISTER;
register_pair: REGISTER_PAIR;
register_operand: REGISTER_OPERAND;
immediate_value: IMMEDIATE_VALUE_PREFIX INTEGER_LITERAL;
signed_immediate_value: SIGNED_IMMEDIATE_VALUE INTEGER_LITERAL;
length_field: LENGTH_FIELD_PREFIX INTEGER_LITERAL;
mask_field: MASK_FIELD_PREFIX INTEGER_LITERAL;
relative_immediate_operand: RELATIVE_IMMEDIATE_OPERAND_PREFIX INTEGER_LITERAL;
vector_register_pair: VECTOR_REGISTER_PAIR;
vector_register: VECTOR_REGISTER;
displacement_offset: register_operand | index_register | length_field | vector_register;
displacement_reference: OPEN_PAREN (displacement_offset COMMA)? base_register CLOSE_PAREN;
displacement_bits: INTEGER_LITERAL;
displacement: DISPLACEMENT_PREFIX displacement_bits displacement_reference;
optionalMaskField: OPTIONAL_OPEN_PAREN COMMA mask_field OPTIONAL_CLOSE_PAREN;
optionalSignedImmediateValue: OPTIONAL_OPEN_PAREN COMMA signed_immediate_value OPTIONAL_CLOSE_PAREN;
optionalRegister: OPTIONAL_OPEN_PAREN COMMA register_operand OPTIONAL_CLOSE_PAREN;
operands: ((operand COMMA)* operand)*;
operand: displacement | floating_point_register_pair | floating_point_register | index_register
    | base_register | register_pair | register_operand |  control_register | immediate_value | signed_immediate_value
    | length_field | mask_field | relative_immediate_operand | vector_register_pair | vector_register
    | optionalMaskField | optionalSignedImmediateValue | optionalRegister | access_register;
expression
    : expression ('*'|'/') expression
    | expression ('+'|'-') expression
    | expression ('=='|'!='|'<'|'>'|'<='|'>=') expression
    | expression ('&&'|'||') expression
    | '(' expression ')'
    | INTEGER_LITERAL
    | STRING_LITERAL
    ;
