package com.mojo.loader.code;

import com.mojo.loader.CodeElementVisitor;
import lombok.Getter;

@Getter
public class IfBodyElement extends ContextualCodeElement {
    private final CodeBlockElement element;

    public IfBodyElement(String id, CodeBlockElement element, String ifBlockID) {
        super(id, "IF_BODY", ifBlockID);
        this.element = element;
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visitStart(this);
        element.accept(visitor);
        visitor.visitEnd(this);
    }

    @Override
    public String text() {
        return "IF_BODY " + element.text();
    }

    @Override
    public String originalText() {
        return "\n" + element.originalText();
    }
}
