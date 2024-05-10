package com.assets.demo.utils;

import java.util.StringJoiner;

public class GenericUtils {
    public static String generateID(String... args) {
        StringJoiner joiner = new StringJoiner(":");
        for (String arg : args) {
            joiner.add(arg.toLowerCase().replace(" ", "_").trim());
        }
        return joiner.toString();
    }
}
