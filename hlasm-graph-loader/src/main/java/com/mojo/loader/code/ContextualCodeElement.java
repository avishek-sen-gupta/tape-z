package com.mojo.loader.code;

import lombok.Getter;

@Getter
public abstract class ContextualCodeElement extends CodeElement {
    protected final String parentID;

    public ContextualCodeElement(String id, String type, String parentID) {
        super(id, type);
        this.parentID = parentID;
    }
}
