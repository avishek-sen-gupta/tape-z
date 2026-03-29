package com.mojo.loader.symbolic;

import com.mojo.algorithms.domain.TranspilerInstruction;
import lombok.Getter;

public class AddressSymbol implements AbstractSymbol {

    @Getter private final TranspilerInstruction element;

    public AddressSymbol(TranspilerInstruction element) {
        this.element = element;
    }

    @Override
    public String toString() {
        return String.format("A(%s) ", element.originalText());
    }

    @Override
    public AbstractSymbol resolved() {
        return this;
    }
}
