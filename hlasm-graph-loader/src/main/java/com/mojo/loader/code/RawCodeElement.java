package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.function.Function;

public class RawCodeElement extends CodeElement {
    @Expose
    @Getter private final String line;

    public RawCodeElement(String uuid, String line) {
        super(uuid, "RAW");
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
        return null;
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public CodeElement merge(CodeElement element) {
        if (!(element instanceof RawCodeElement other))
            throw new UnsupportedOperationException("Cannot merge non-raw element with a raw element. Continuation cannot be a code block.");
        return new RawCodeElement(new UUIDProvider().next(), line + " " + other.getLine());
    }

    @Override
    protected boolean continues() {
        return line.endsWith("X") || line.endsWith("-");
    }

    @Override
    public String text() {
        return line;
    }
}
