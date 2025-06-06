package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.algorithms.id.Identifiable;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.function.Function;

@Getter
public abstract class CodeElement implements Identifiable {
    @Expose protected final String id;
    @Expose protected final String type;

    public CodeElement(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public String originalText() {
        return text();
    }

    public void add(CodeElement codeElement) {
        throw new UnsupportedOperationException("There is no default add operation");
    }

    public CodeElement map(Function<CodeElement, CodeElement> mapper) {
        return mapper.apply(this);
    }

    public CodeElement reduce() {
        throw new UnsupportedOperationException("There is no default reduce operation");
    }

    public abstract void accept(CodeElementVisitor visitor);

    public CodeElement merge(CodeElement element) {
        throw new UnsupportedOperationException("There is no default merge operation");
    }

    protected boolean continues() {
        return false;
    }

    public abstract String text();

    @Override
    public String id() {
        return id;
    }

    @Override
    public String label() {
        return text();
    }

    @Override
    public String toString() {
        return text();
    }
}
