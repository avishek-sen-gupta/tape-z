package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.function.Function;

public class MacroElement extends CodeElement {
    @Expose @Getter private final String line;

    public MacroElement(String id, String line) {
        super(id, "MACRO");
        this.line = line;
    }

    @Override
    public String originalText() {
        return line;
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
        return line;
    }
}
