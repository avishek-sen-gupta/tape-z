package com.mojo.loader.pass;

import com.mojo.loader.code.CodeElement;

public class LineContinuationCollapsePass {
    public CodeElement run(CodeElement element) {
        return element.reduce();
    }
}
