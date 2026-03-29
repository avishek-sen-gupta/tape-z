grammar LiteralTypeParser;
import LiteralTypeLexer;
@header {package com.mojo.hlasm;}

options {tokenVocab = LiteralTypeLexer;}

startRule: storageSpec;
storageSpec: repeat? typeSpecifier (LENGTH_MODIFER unitSize)?;
repeat: INTEGER_LITERAL;
unitSize: INTEGER_LITERAL;

typeSpecifier: characterLiteral | hexLiteral | binaryLiteral | halfWordLiteral | fullWordLiteral
              | packedDecimalLiteral | addressLiteral | shortFloatingPoint | longFloatingPoint;
characterLiteral: CHAR_LITERAL;
hexLiteral: HEX_LITERAL;
binaryLiteral: BINARY_LITERAL;
fullWordLiteral: FULL_WORD_LITERAL;
halfWordLiteral: HALF_WORD_LITERAL;
packedDecimalLiteral: PACKED_DECIMAL_LITERAL;
addressLiteral: ADDRESS_LITERAL;
shortFloatingPoint: SHORT_FLOATING_POINT;
longFloatingPoint: LONG_FLOATING_POINT;
