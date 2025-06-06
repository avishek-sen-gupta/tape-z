package com.mojo.loader.code;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.CodeElementVisitor;
import com.mojo.woof.UUIDProvider;
import lombok.Getter;

@Getter
public class TerminalElement extends CodeElement {
    private final TerminalType terminalType;
    private final CodeElement element;

    public static TerminalElement START(CodeElement element, IdProvider idProvider) {
        return new TerminalElement(idProvider.next(), TerminalType.START, element);
    }

    public static TerminalElement END(CodeElement element, IdProvider idProvider) {
        return new TerminalElement(idProvider.next(), TerminalType.END, element);
    }

    public TerminalElement(String id, TerminalType terminalType, CodeElement element) {
        super(id, "TERMINAL_" + terminalType.name());
        this.terminalType = terminalType;
        this.element = element;
    }

    @Override
    public String originalText() {
        return "TERMINAL_" + terminalType.name() +  "(" + element.text() + ")";
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String text() {
        return "TERMINAL_" + terminalType.name() + ": " + element.text();
    }
}
