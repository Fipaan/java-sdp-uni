package fipaan.com.utils;

import java.util.Arrays;

public class StringUtils {
    public static void memset(char[] arr, char ch) {
        Arrays.fill(arr, ch);
    }
    public static void memset(StringBuilder sb, char ch, int count) {
        char[] arr = new char[count];
        Arrays.fill(arr, ch);
        sb.setLength(0);
        sb.append(arr);
    }
}
