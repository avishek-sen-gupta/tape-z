package com.mojo.loader.pass;

import com.mojo.algorithms.id.IdProvider;
import com.mojo.loader.code.*;

public class StructuredMacroSanitisePass {
    private final IdProvider idProvider;

    public StructuredMacroSanitisePass(IdProvider idProvider) {
        this.idProvider = idProvider;
    }

    public CodeElement run(CodeElement element) {
        return element.map(this::conditionalCleanup);
    }

    private CodeElement conditionalCleanup(CodeElement codeElement) {
        if (!(codeElement instanceof RawCodeElement)) return codeElement;
        String line = ((RawCodeElement) codeElement).getLine();
        if (line.trim().startsWith("IF ")) return new IfStatementElement(idProvider.next(), line.trim().substring(3));
        if (line.trim().startsWith("ELSE")) return new ElseStatementElement(idProvider.next());
        if (line.trim().startsWith("ENDIF")) return new EndIfStatementElement(idProvider.next());
        return codeElement;
    }
}
