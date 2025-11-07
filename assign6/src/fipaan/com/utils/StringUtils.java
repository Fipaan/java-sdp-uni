package fipaan.com.utils;

import java.util.Arrays;

public class StringUtils {
    public static final String NIL = "(null)";

    public static <T> String saveToString(T val) { return val == null ? NIL : val.toString(); }

    public static <T> void memset(T[] arr, T elem) {
        Arrays.fill(arr, elem);
    }
    public static <T> void memset(T[] arr, int from, int to, T elem) {
        Arrays.fill(arr, from, to, elem);
    }
    public static void memset(int[] arr, int num) {
        Arrays.fill(arr, num);
    }
    public static void memset(int[] arr, int from, int to, int num) {
        Arrays.fill(arr, from, to, num);
    }
    public static void memset(char[] arr, char ch) {
        Arrays.fill(arr, ch);
    }
    public static void memset(char[] arr, int from, int to, char ch) {
        Arrays.fill(arr, from, to, ch);
    }
    public static void memset(StringBuilder sb, char ch, int count) {
        char[] arr = new char[count];
        Arrays.fill(arr, ch);
        sb.setLength(0);
        sb.append(arr);
    }
    
    public static String CodepointToString(int code) {
        return new String(Character.toChars(code));
    }
    public static String CodepointToStringSafe(int code) {
        return code == '\0'? "\\0" : new String(Character.toChars(code));
    }
}
