package com.mojo.loader;

import com.google.gson.annotations.Expose;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParseNode {
    @Expose private final String id;
    @Expose private final String name;
    @Expose private final String type;
    @Expose private final String text;
    @Expose private final String namespace;
    @Expose private final List<ParseNode> children = new ArrayList<>();

    public ParseNode(String id, String name, String type, String text, String namespace) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.text = text;
        this.namespace = namespace;
    }

    public void addChild(ParseNode child) {
        children.add(child);
    }
}
