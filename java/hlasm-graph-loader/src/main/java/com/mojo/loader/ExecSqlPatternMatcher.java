package com.mojo.loader;

import java.util.regex.Pattern;

public class ExecSqlPatternMatcher {
    public boolean matches(String text) {
        Pattern pattern = Pattern.compile("EXEC\\s+SQL");
        return pattern.matcher(text).find();
    }
}
