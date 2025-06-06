package com.mojo.loader.code;

import com.google.gson.annotations.Expose;
import com.mojo.algorithms.id.IdProvider;
import com.mojo.algorithms.id.UUIDProvider;
import com.mojo.loader.CodeElementVisitor;
import com.mojo.loader.CollapseState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LabelledCodeBlockElement extends CodeElement {
    @Expose
    @Getter
    private final String label;
    @Expose
    @Getter
    private final List<CodeElement> children;
    private final IdProvider idProvider = new UUIDProvider();

    public LabelledCodeBlockElement(String id, String type, String label) {
        this(id, type, label, new ArrayList<>());
    }

    public LabelledCodeBlockElement(String id, String type, String label, List<CodeElement> codeElements) {
        super(id, type);
        this.label = label;
        this.children = codeElements;
    }

    @Override
    public void add(CodeElement codeElement) {
        children.add(codeElement);
    }

    @Override
    public CodeElement map(Function<CodeElement, CodeElement> mapper) {
        return new LabelledCodeBlockElement(idProvider.next(), type, label, children.stream().map(e -> e.map(mapper)).toList());
    }

    // TODO: Refactor this ugly part, move it into respective classes if possible!
    @Override
    public CodeElement reduce() {
        CollapseState result = children.stream().reduce(new CollapseState(), (all, current) -> {
            if (!(current instanceof RawCodeElement)) {
                all.add(current.reduce());
                all.continueToNextLine(false);
                return all;
            }
            CodeElement rawCodeElementWithoutContinuationSymbol = withoutContinuationSymbol(current);
            if (all.continuesToNextLine()) all.merge(rawCodeElementWithoutContinuationSymbol);
            else all.add(rawCodeElementWithoutContinuationSymbol);
            all.continueToNextLine(currentLineContinues(current));
            return all;
        }, (a1, a2) -> a2);
        return new LabelledCodeBlockElement(idProvider.next(), type, label, result.getElements());
    }

    @Override
    public void accept(CodeElementVisitor visitor) {
        visitor.visitStart(this);
        children.forEach(e -> e.accept(visitor));
        visitor.visitEnd(this);
    }

    private CodeElement withoutContinuationSymbol(CodeElement codeElement) {
        if (codeElement.text().length() != 64 && codeElement.text().length() != 72) return codeElement;
        return new RawCodeElement(idProvider.next(), codeElement.text().substring(0, codeElement.text().length() - 1));
    }

    @Override
    public CodeElement merge(CodeElement element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String text() {
        return "";
    }

    private boolean currentLineContinues(CodeElement element) {
        return element.continues();
    }
}
