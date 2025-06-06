package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.List;
import java.util.function.Function;

public class CallExternalCustomElement extends CodeElement {
    @Expose private final String line;
    @Getter private final String destination;
    @Getter private final List<String> params;

    public CallExternalCustomElement(String id, String destination, List<String> params, String line) {
        super(id, "MACRO");
        this.destination = destination;
        this.params = params;
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
