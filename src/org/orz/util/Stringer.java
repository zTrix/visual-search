package org.orz.util;

public class Stringer {
    public static String escapeStringForJson(String s) {
        return s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\"");
    }
}
