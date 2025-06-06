package com.mojo.loader.symbolic;

public class ValueSymbol implements AbstractSymbol {

    private final String value;

    public ValueSymbol(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public AbstractSymbol resolved() {
        return this;
    }
}
