grammar DisplacementParser;
import HlasmLexer;

@header {package com.mojo.hlasm;}
options {tokenVocab = HlasmLexer;}

// ONLY FOR TESTING: DO NOT USE
// index part represents both index expressions and length expressions
startRule: displacement;
displacement: explicitOffset | implicitZeroDisplacement | relativeDisplacement;
relativeDisplacement: ASTERISK displacement;
explicitOffset: offsetExpression explicitIndexBase?;
implicitZeroDisplacement: explicitIndexBase;
symbol: IDENTIFIER;
macroSymbol: AMPERSAND symbol;
offsetExpression: expression;
explicitIndexBase: bothIndexBase | onlyIndex | onlyBase;
bothIndexBase: OPEN_PAREN indexExpression COMMA baseExpression CLOSE_PAREN;
onlyIndex: OPEN_PAREN indexExpression CLOSE_PAREN;
onlyBase: OPEN_PAREN COMMA baseExpression CLOSE_PAREN;
indexExpression: expression;
baseExpression: expression;
arguments
    : expression (COMMA expression)*
    ;

register: expression;
registerPair: expression;
indexRegister: expression;
baseRegister: expression;
floatingPointRegister: expression;
floatingPointRegisterPair: expression;
immediateValue: expression;
signedImmediateValue: expression;
lengthField: expression;
maskField: expression;
controlRegister: expression;
accessRegister: expression;
relativeImmediateOperand: relativeDisplacement;
vectorRegister: expression;
vectorRegisterPair: expression;
integer_literal_string: SINGLE_QUOTE INTEGER_LITERAL SINGLE_QUOTE;
halfWord: 'H' integer_literal_string;
fullWord: 'F' integer_literal_string;
hexValue: 'X' SINGLE_QUOTE HEX_LITERAL SINGLE_QUOTE;
charLiteral: LENGTH_SPECIFIED_LITERAL | UNPADDED_LITERAL;
function: IDENTIFIER OPEN_PAREN arguments? CLOSE_PAREN;
functionAddress: EQUALS function;
literalPool: (fullWord | hexValue | halfWord | charLiteral | packedDecimalConstant);
literalAddress: EQUALS (fullWord | hexValue | halfWord | charLiteral | packedDecimalConstant);
packedDecimalConstant: 'P' integer_literal_string;

expression
    : additiveExpression
    ;

additiveExpression
    : multiplicativeExpression
    | additiveExpression '+' multiplicativeExpression
    | additiveExpression '-' multiplicativeExpression
    ;

multiplicativeExpression
    : unaryExpression
    | multiplicativeExpression '*' unaryExpression
    | multiplicativeExpression '/' unaryExpression
    | multiplicativeExpression '%' unaryExpression
    ;

unaryExpression
    : primaryExpression
    | '+' unaryExpression
    | '-' unaryExpression
    ;

primaryExpression
    : INTEGER_LITERAL
    | symbol
    | macroSymbol
    | literalPool
    | literalAddress
    | function
    | functionAddress
    | '(' expression ')'
    ;
