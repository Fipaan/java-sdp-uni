package fipaan.com.utils;

public class TypeUtils {
    public static <In, Out> Out cast(In in) {
        @SuppressWarnings("unchecked")
        Out out = (Out)in;
        return out;
    }
}
