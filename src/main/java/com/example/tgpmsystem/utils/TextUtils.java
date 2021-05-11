package com.example.tgpmsystem.utils;

public class TextUtils {

    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNull(Object object) {
        return object == null || "".equals(object);
    }
}
