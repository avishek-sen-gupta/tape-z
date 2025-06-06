package com.mojo.loader;

import com.mojo.hlasm.HlasmParserLexer;
import com.mojo.hlasm.HlasmParserParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class HLASMParserMain {

    public static void main(String[] args) {
        ParseTree parsed = parsed("MVC BFIRST+20(10),NULL ");
        System.out.println("DONE");
    }

    private static ParseTree parsed(String hlasmInstruction) {

        CharStream charStream = CharStreams.fromString(hlasmInstruction);
        HlasmParserLexer lexer = new HlasmParserLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HlasmParserParser parser = new HlasmParserParser(tokens);
        return parser.startRule();
    }

}
