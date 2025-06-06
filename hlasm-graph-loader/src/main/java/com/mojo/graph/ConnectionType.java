package com.mojo.graph;

public enum ConnectionType {
    FLOWS_AFTER_SUBROUTINE("FLOWS_AFTER_SUBROUTINE"),
    FLOWS_TO_IF_FALSE("FLOWS_TO_IF_FALSE"),
    FLOWS_TO_IF_TRUE("FLOWS_TO_IF_TRUE"),
    FLOWS_TO_SYNTAX_ONLY("FLOWS_TO_SYNTAX_ONLY"),
    CALLS_PROC("CALLS_PROC"),
    FLOWS_TO("FLOWS_TO"),
    RETURNS_FROM_PROC("RETURNS_FROM_PROC");

    private final String name;

    ConnectionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String value() {
        return name;
    }

    public static boolean isReachableConnection(String value) {
        ConnectionType connectionType = ConnectionType.valueOf(value.toUpperCase());
        return connectionType == FLOWS_TO ||
                connectionType == FLOWS_TO_IF_TRUE ||
                connectionType == FLOWS_TO_IF_FALSE ||
                connectionType == CALLS_PROC ||
                connectionType == RETURNS_FROM_PROC;
    }

    public static boolean isReachableConnectionInBasicBlock(String value) {
        ConnectionType connectionType = ConnectionType.valueOf(value.toUpperCase());
        return connectionType == FLOWS_TO ||
                connectionType == FLOWS_TO_IF_TRUE ||
                connectionType == FLOWS_TO_IF_FALSE;
    }
}
