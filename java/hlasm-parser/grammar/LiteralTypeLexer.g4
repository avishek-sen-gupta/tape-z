lexer grammar LiteralTypeLexer;


// =========================================================
// WHITESPACE AND COMMENTS (skipped during parsing)
// =========================================================

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
EQUALS: '=';
PLUS: '+';
MINUS: '-';
ASTERISK: '*';
COMMA: ',';
OPEN_PAREN: '(';
CLOSE_PAREN: ')';

// =========================================================
// IDENTIFIERS AND LITERALS
// =========================================================

AMPERSAND: '&';
SINGLE_QUOTE: '\'';

// Integer literal: sequence of digits
INTEGER_LITERAL
    : [0-9]+
    ;

CHAR_LITERAL: C;
HEX_LITERAL: X;
BINARY_LITERAL: B;
FULL_WORD_LITERAL: F;
HALF_WORD_LITERAL: H;
PACKED_DECIMAL_LITERAL: P;
ADDRESS_LITERAL: P;
SHORT_FLOATING_POINT: E;
LONG_FLOATING_POINT: D;

LENGTH_MODIFER: L;
