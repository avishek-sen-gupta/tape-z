package com.mojo.loader;

public class GroupUtil {
    public static String separated(String separator, String atomFragment) {
        return "(" + separator + (separator.isEmpty() ? "" : " ") + atomFragment + ")";
    }
}
