package fipaan.com.errors;

public class FError {
    public static RuntimeException New()                           { return new RuntimeException(""); }
    public static RuntimeException New(Object obj)                 { return new RuntimeException(obj.toString()); }
    public static RuntimeException New(String fmt, Object... args) { return new RuntimeException(String.format(fmt, (Object[])args)); }

    public static RuntimeException New(Throwable e)                             { return new RuntimeException(e); }
    public static RuntimeException New(Throwable e, Object obj)                 { return new RuntimeException(obj.toString()); }
    public static RuntimeException New(Throwable e, String fmt, Object... args) { return new RuntimeException(String.format(fmt, (Object[])args), e); }

    public static RuntimeException UNREACHABLE()                           { return new RuntimeException("UNREACHABLE"); }
    public static RuntimeException UNREACHABLE(String msg)                 { return new RuntimeException(String.format("UNREACHABLE: %s", msg)); }
    public static RuntimeException UNREACHABLE(Object obj)                 { return FError.UNREACHABLE(obj.toString()); }
    public static RuntimeException UNREACHABLE(String fmt, Object... args) { return FError.UNREACHABLE(String.format(fmt, (Object[])args)); }

    public static RuntimeException errorNotSpecified(String name) { return FError.New(name + " is not specified");  } 
    public static void verifyNotZero(int num, String name)        { if (num == 0)    throw errorNotSpecified(name); } 
    public static void verifyNotZero(float num, String name)      { if (num == 0.0f) throw errorNotSpecified(name); } 
    public static void verifyNotNull(Object obj, String name)     { if (obj == null) throw errorNotSpecified(name); }
    
    public static RuntimeException TODO(String str)                 { return FError.New("NOT IMPLEMENTED: %s", str); }
    public static RuntimeException TODO(Object obj)                 { return TODO(obj.toString()); }
    public static RuntimeException TODO(String fmt, Object... args) { return TODO(String.format(fmt, (Object[])args)); }
    public static RuntimeException TODO() {
        String callerName = Thread.currentThread().getStackTrace()[2].getMethodName();
        return TODO(callerName);
    }
}
