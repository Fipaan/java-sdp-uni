package lfs.utils;

public class StringUtils {
    public static String list(String elems, String elem) {
        return String.format("%s%s %s",
                elems,
                elems.contains("with") ? ","  : " with:",
                elem
        );
    }
}
