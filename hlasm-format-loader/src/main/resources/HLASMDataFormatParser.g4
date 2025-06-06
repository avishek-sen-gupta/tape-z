grammar HLASMDataFormatParser;
import HLASMDataFormatLexer;

options {tokenVocab = HLASMDataFormatLexer;}

constantOperand: duplicationFactor? constantType typeExtension? programType? modifier?;
duplicationFactor: INTEGER_LITERAL;
constantType: halfWordFixedPointAddressConstant
| fullWordFixedPointAddressConstant
| halfWordDisplacementBaseAddressConstant
| fullWordExternalSymbolAddressConstant
| fullWordDXDAddressConstant
| fullWordExternalDummySectionOffsetAddressConstant
| fullWordPSECTAddressConstant
| binaryConstant
| hexadecimalConstant
| characterConstant
| decimalConstant
| fixedPointHalfWordConstant
| fixedPointFullWordConstant
| floatingFullWordPointConstant
| floatingDoubleWordPointConstant
| floatingTwoDoubleWordsPointConstant
| packedDecimalConstant
| zonedDecimalConstant
| graphicConstant
;

modifier: lengthModifier
| lengthModifier scaleModifier
| scaleModifier exponentModifier
| lengthModifier exponentModifier
| lengthModifier scaleModifier exponentModifier;

charLiteral: LENGTH_SPECIFIED_LITERAL | UNPADDED_LITERAL;
hexValue: 'X' SINGLE_QUOTE HEX_LITERAL SINGLE_QUOTE;
signedIntegerLiteral: INTEGER_LITERAL | NEGATIVE_INTEGER_LITERAL;

typeExtension: 'A' | 'B' | 'D' | 'E' | 'H' | 'Q' | 'U';
programType: 'P' OPEN_PAREN (INTEGER_LITERAL | hexValue | charLiteral)  CLOSE_PAREN;
halfWordFixedPointAddressConstant: 'Y';
fullWordFixedPointAddressConstant: 'A';
halfWordDisplacementBaseAddressConstant: 'S';
fullWordExternalSymbolAddressConstant: 'V';
fullWordDXDAddressConstant: 'J';
fullWordExternalDummySectionOffsetAddressConstant: 'Q';
fullWordPSECTAddressConstant: 'R';
binaryConstant: 'B';
hexadecimalConstant: 'X';
characterConstant: 'C';
decimalConstant: 'D';
fixedPointHalfWordConstant: 'H';
fixedPointFullWordConstant: 'F';
floatingFullWordPointConstant: 'E';
floatingDoubleWordPointConstant: 'D';
floatingTwoDoubleWordsPointConstant: 'L';
packedDecimalConstant: 'P';
zonedDecimalConstant: 'Z';
graphicConstant: 'G';

//lengthModifier: 'L' '.'? (IDENTIFIER | signedIntegerLiteral | (OPEN_PAREN expression CLOSE_PAREN));
//scaleModifier: 'S' (IDENTIFIER | signedIntegerLiteral | (OPEN_PAREN expression CLOSE_PAREN));
//exponentModifier: 'E' (IDENTIFIER | signedIntegerLiteral | (OPEN_PAREN expression CLOSE_PAREN));
lengthModifier: 'L' '.'? signedIntegerLiteral;
scaleModifier: 'S' signedIntegerLiteral;
exponentModifier: 'E' signedIntegerLiteral;

//nominalValue: STRING_LITERAL | expressions;
//nominalValue: expressions | literals;
//expressions: OPEN_PAREN expression (COMMA expression)* CLOSE_PAREN;
//literals: SINGLE_QUOTE singleLiteral (COMMA singleLiteral)* SINGLE_QUOTE;
//singleLiteral: INTEGER_LITERAL | HEX_LITERAL | FLOATING_POINT_LITERAL;

//expression
//    : expression ('*'|'/') expression
//    | expression ('+'|'-') expression
//    | '(' expression ')'
//    | INTEGER_LITERAL
//    | IDENTIFIER
//    ;
