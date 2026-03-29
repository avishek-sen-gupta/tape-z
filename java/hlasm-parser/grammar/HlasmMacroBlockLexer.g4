lexer grammar HlasmMacroBlockLexer;


// =========================================================
// WHITESPACE AND COMMENTS (skipped during parsing)
// =========================================================

// Whitespace: spaces, tabs, carriage returns, and line feeds
WHITESPACE
    : [ \t\r\n]+ -> skip
    ;

fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');
fragment ZERO: '0';
fragment ONE: '1';
fragment TWO: '2';
fragment PLUS: '+';

// =========================================================
// IDENTIFIERS AND LITERALS
// =========================================================

// Identifier: starts with letter or underscore, followed by letters, digits, or underscores
//IDENTIFIER
//    : [a-zA-Z_][a-zA-Z_0-9]*
//    ;

// Integer literal: sequence of digits
//INTEGER_LITERAL
//    : [0-9]+
//    ;

// String literal: text enclosed in double quotes, with proper escape handling
//STRING_LITERAL
//    : '"' ( ~["\r\n\\] | '\\' . )* '"'
//    ;
UNDERSCORE: '_';
fragment AT: '@';
fragment HEX_CHAR: [0-9A-Fa-f];
fragment GUID_8: HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR;
fragment GUID_4: HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR;
fragment GUID_12: HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR HEX_CHAR;
GUID: GUID_8 DASH GUID_4 DASH GUID_4 DASH GUID_4 DASH GUID_12;
fragment DASH: '-';
COMMENT: AT C O M M E N T;
ASM: AT A S M;
SQL: AT S Q L;
IF: AT I F;
ELSE: AT E L S E;
ENDIF: AT E N D I F;
RAW: AT R A W;
LABELSTART: AT L A B E L S T A R T;
LABELEND: AT L A B E L E N D;
MACRO: AT M A C R O;
CALL: AT C A L L;
