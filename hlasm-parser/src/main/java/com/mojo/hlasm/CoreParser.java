package com.mojo.hlasm;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.ATN;

public abstract class CoreParser extends Parser {
    public CoreParser(TokenStream input) {
        super(input);
    }

    public abstract String[] getTokenNames();

    public abstract String[] getRuleNames();

    public abstract String getGrammarFileName();

    public abstract ATN getATN();
}
