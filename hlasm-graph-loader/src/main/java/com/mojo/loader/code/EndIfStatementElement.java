package com.mojo.loader.code;

import com.mojo.loader.CodeElementVisitor;

import java.util.function.Function;

public class EndIfStatementElement extends CodeElement {
    public EndIfStatementElement(String id) {
        super(id, "ENDIF");
    }

    @Override
    public void add(CodeElement codeElement) {

    }

    @Override
    public CodeElement map(Function<CodeElement, CodeElement> mapper) {
        return mapper.apply(this);
    }

    @Override
    public CodeElement reduce() {
        return this;
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public CodeElement merge(CodeElement element) {
        return this;
    }

    @Override
    protected boolean continues() {
        return false;
    }

    @Override
    public String text() {
        return "ENDIF";
    }
}
