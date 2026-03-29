package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.function.Function;

public class IfStatementElement extends CodeElement {
    @Expose @Getter private final String condition;

    public IfStatementElement(String id, String condition) {
        super(id, "IF");
        this.condition = condition;
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
        return "IF " + condition;
    }
}
