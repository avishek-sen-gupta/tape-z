package com.mojo.loader.code;

import com.mojo.loader.CodeElementVisitor;

public class EmptyLineElement extends CodeElement {
    private final RawCodeElement codeElement;

    public EmptyLineElement(String id, RawCodeElement codeElement) {
        super(id, "EMPTY_LINE");
        this.codeElement = codeElement;
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String text() {
        return "";
    }
}
