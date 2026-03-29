package com.mojo.loader.symbolic;

public class SymbolReference implements AbstractSymbol {
    private final String name;
    private final AbstractSymbol value;

    public SymbolReference(String name, AbstractSymbol value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " -> " + value.toString();
    }

    @Override
    public AbstractSymbol resolved() {
        return value.resolved();
    }
}
