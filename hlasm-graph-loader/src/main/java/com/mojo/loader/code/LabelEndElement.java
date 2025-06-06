package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

import java.util.function.Function;

public class LabelEndElement extends CodeElement {
    @Expose @Getter private final String label;
    @Expose private final String blockGUID;

    public LabelEndElement(String id, String label, String blockGUID) {
        super(id, "LABEL_END");
        this.label = label;
        this.blockGUID = blockGUID;
    }

    @Override
    public String originalText() {
        return label + ": END";
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
        return "LABEL_END: " + label;
    }
}
