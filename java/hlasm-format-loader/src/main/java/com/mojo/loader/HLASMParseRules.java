package com.mojo.loader;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class HLASMParseRules {
    public static final List<String> HEADERS = ImmutableList.of(
            "grammar HlasmParser;",
            "import HlasmLexer;",
            "",
            "@header {package com.mojo.hlasm;}",
            "options {tokenVocab = HlasmLexer;}",
            ""
    );

    public static final String START_RULE = "startRule: assemblerInstruction";
    public static final String FULL_WORD = "fullWord: 'F' SINGLE_QUOTE INTEGER_LITERAL SINGLE_QUOTE";
    public static final String HALF_WORD = "halfWord: 'H' SINGLE_QUOTE INTEGER_LITERAL SINGLE_QUOTE";
    public static final String HEX_VALUE = "hexValue: 'X' SINGLE_QUOTE HEX_LITERAL SINGLE_QUOTE";
    public static final String CHAR_LITERAL = "charLiteral: LENGTH_SPECIFIED_LITERAL | UNPADDED_LITERAL";
    public static final String SIGNED_INTEGER_LITERAL = "signedIntegerLiteral: INTEGER_LITERAL | NEGATIVE_INTEGER_LITERAL";
    public static final String RELATIVE_INTEGER_LITERAL = "relativeIntegerLiteral: (PLUS INTEGER_LITERAL) | NEGATIVE_INTEGER_LITERAL";
    public static final String CONSTANT = "constant: literalPool";
    public static final String LITERAL_POOL = "literalPool: EQUALS (fullWord | hexValue | halfWord | charLiteral | packedDecimalConstant)";
    public static final String PACKED_DECIMAL_CONSTANT = "packedDecimalConstant: 'P' INTEGER_LITERAL_STRING";
}
