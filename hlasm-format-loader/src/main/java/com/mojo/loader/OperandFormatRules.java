package com.mojo.loader;

public class OperandFormatRules {
    public static String integerLiteralRule(String atom) {
        return String.format("%s: %s", atom, RuleConstants.INTEGER_LITERAL);
    }

    public static String register(String atom) {
        return atom + ": INTEGER_LITERAL | IDENTIFIER";
    }

    public static String immediate(String atom) {
        return atom + ": fullWord | hexValue | halfWord | charLiteral | INTEGER_LITERAL | packedDecimalConstant | constant";
    }

    public static String signedImmediate(String atom) {
        return atom + ": signedIntegerLiteral";
    }

    public static String displacement(String atom) {
        return atom + ": INTEGER_LITERAL | IDENTIFIER";
    }

    public static String relativeImmediate(String atom) {
        return atom + ": (ASTERISK relativeIntegerLiteral) | constant";
    }

    public static String numberOrConstant(String atom) {
        return atom + ": INTEGER_LITERAL | IDENTIFIER";
    }
}
