package com.mojo.loader;

import java.util.stream.Stream;

public class RuleConstants {
    public static final String COMMA = "COMMA";
    public static final String OPEN_PAREN = "OPEN_PAREN";
    public static final String CLOSE_PAREN = "CLOSE_PAREN";
    public static final String INTEGER_LITERAL = "INTEGER_LITERAL";
    public static final String REGISTER = "register";
    public static final String REGISTER_PAIR = "registerPair";
    public static final String INDEX_REGISTER = "indexRegister";
    public static final String BASE_REGISTER = "baseRegister";
    public static final String FLOATING_POINT_REGISTER = "floatingPointRegister";
    public static final String FLOATING_POINT_REGISTER_PAIR = "floatingPointRegisterPair";
    public static final String DISPLACEMENT = "displacement";
    public static final String IMMEDIATE_VALUE = "immediateValue";
    public static final String SIGNED_IMMEDIATE_VALUE = "signedImmediateValue";
    public static final String LENGTH_FIELD = "lengthField";
    public static final String MASK_FIELD = "maskField";
    public static final String CONTROL_REGISTER = "controlRegister";
    public static final String ACCESS_REGISTER = "accessRegister";
    public static final String RELATIVE_IMMEDIATE_OPERAND = "relativeImmediateOperand";
    public static final String VECTOR_REGISTER = "vectorRegister";
    public static final String VECTOR_EGISTER_PAIR = "vectorRegisterPair";

//    public static Stream<String> dataRules() {
//        return Stream.of("defineConstant: IDENTIFIER addressConstant | binaryConstant | characterConstant | decimalConstant | fixedPointConstant | floatingPointConstant | graphicConstant | hexadecimalConstant | zonedConstant",
//                "")
//    }
}
