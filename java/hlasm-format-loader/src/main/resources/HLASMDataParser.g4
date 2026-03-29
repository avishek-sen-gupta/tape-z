grammar HLASMDataParser;
import HLASMDataLexer;

options {tokenVocab = HLASMDataLexer;}

defineConstant: IDENTIFIER? 'DC' constantOperand;
constantOperand: optionalParams nominalValue;


nominalValue: STRING_LITERAL | BRACKETED_LITERAL;
//expressions: OPEN_PAREN expression (COMMA expression)* CLOSE_PAREN;
//literals: SINGLE_QUOTE singleLiteral (COMMA singleLiteral)* SINGLE_QUOTE;
//singleLiteral: IDENTIFIER;
optionalParams: IDENTIFIER;

expression
    : expression ('*'|'/') expression
    | expression ('+'|'-') expression
    | '(' expression ')'
    | IDENTIFIER
    ;
