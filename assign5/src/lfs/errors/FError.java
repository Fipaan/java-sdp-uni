package lfs.errors;

public class FError {
    public static RuntimeException New() { return new RuntimeException(""); }
    public static RuntimeException New(Object obj) { return new RuntimeException(obj.toString()); }
    public static RuntimeException New(String fmt, Object... args) { return new RuntimeException(String.format(fmt, (Object[])args)); }
    public static RuntimeException New(Throwable e) { return new RuntimeException(e); }
    public static RuntimeException New(Throwable e, Object obj) { return new RuntimeException(obj.toString()); }
    public static RuntimeException New(Throwable e, String fmt, Object... args) { return new RuntimeException(String.format(fmt, (Object[])args), e); }

    public static void verifyNotZero(int num, String name)    { if (num == 0)    throw FError.New(name + " is not specified"); } 
    public static void verifyNotZero(float num, String name)  { if (num == 0.0f) throw FError.New(name + " is not specified"); } 
    public static void verifyNotNull(Object obj, String name) { if (obj == null) throw FError.New(name + " is not specified"); }
}
