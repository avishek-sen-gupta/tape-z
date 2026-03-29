package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class CodeBlockElement extends CodeElement {
    @Expose
    private final List<CodeElement> children;

    public CodeBlockElement(String id, String type) {
        this(id, type, new ArrayList<>());
    }

    public CodeBlockElement(String id, String type, List<CodeElement> codeElements) {
        super(id, type);
        this.children = codeElements;
    }

    @Override
    public void add(CodeElement codeElement) {
        children.add(codeElement);
    }

    @Override
    public CodeElement map(Function<CodeElement, CodeElement> mapper) {
        return new CodeBlockElement(new UUIDProvider().next(), type, children.stream().map(e -> e.map(mapper)).toList());
    }

    // TODO: Refactor this ugly part, move it into respective classes if possible!
    @Override
    public CodeElement reduce() {
        return this;
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visitStart(this);
        children.forEach(e -> e.accept(visitor));
        visitor.visitEnd(this);
    }

    @Override
    public CodeElement merge(CodeElement element) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean continues() {
        return false;
    }

    @Override
    public String text() {
        return "";
    }

    @Override
    public String originalText() {
        return children.stream()
                .map(CodeElement::originalText)
                .reduce("", (full, instr) -> full + instr + "\n");
    }
}
