package com.mojo.loader.pass;

import com.mojo.loader.code.CodeBlockElement;
import com.mojo.loader.code.CodeElement;
import com.mojo.loader.code.LabelledCodeBlockElement;

public class SectionFilterPass {
    public CodeElement run(CodeElement root) {
        return root;
//        if (!(root instanceof LabelledCodeBlockElement k)) return root;
//        return k.getChildren().stream()
//                .filter(c -> c instanceof LabelledCodeBlockElement lcb && lcb.getLabel().equals("GETADMIN"))
//                .findFirst().orElse(root);
    }
}
