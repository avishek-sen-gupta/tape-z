package com.mojo.loader;

import com.mojo.loader.code.CodeElement;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CollapseState {
    @Getter private List<CodeElement> elements = new ArrayList<>();
    private boolean continues;

    public void add(CodeElement element) {
        elements.add(element);
    }

    public void reset() {
        continues = false;
    }

    public void continueToNextLine(boolean continueToNextLine) {
        continues = continueToNextLine;
    }

    public void merge(CodeElement element) {
        CodeElement last = elements.getLast();
        elements.removeLast();
        elements.add(last.merge(element));
    }

    public boolean continuesToNextLine() {
        return continues;
    }
}
