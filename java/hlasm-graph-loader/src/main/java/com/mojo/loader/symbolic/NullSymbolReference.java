package com.mojo.loader.symbolic;

public class NullSymbolReference extends SymbolReference {

    public NullSymbolReference() {
        super("NULL", null);
    }

    @Override
    public String toString() {
        return "NULL";
    }

    @Override
    public AbstractSymbol resolved() {
        return this;
    }
}
