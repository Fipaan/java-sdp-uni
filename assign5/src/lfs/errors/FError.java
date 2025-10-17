package lfs.errors;

public class FError {
    public static RuntimeException New() { return new RuntimeException(""); }
    public static RuntimeException New(String msg) { return new RuntimeException(msg); }
}
