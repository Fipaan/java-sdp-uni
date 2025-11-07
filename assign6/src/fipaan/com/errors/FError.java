package fipaan.com.errors;

import fipaan.com.utils.ProcessUtils;

public class FError {    
    public static RuntimeException New()                           { return new RuntimeException(""); }
    public static RuntimeException New(Object obj)                 { return new RuntimeException(obj.toString()); }
    public static RuntimeException New(String fmt, Object... args) { return new RuntimeException(String.format(fmt, (Object[])args)); }

    public static RuntimeException New(Throwable e)                             { return new RuntimeException(e); }
    public static RuntimeException New(Throwable e, Object obj)                 { return new RuntimeException(obj.toString()); }
    public static RuntimeException New(Throwable e, String fmt, Object... args) { return new RuntimeException(String.format(fmt, (Object[])args), e); }

    public static RuntimeException UNREACHABLE(String msg)                 { return new RuntimeException(String.format("UNREACHABLE: %s", msg)); }
    public static RuntimeException UNREACHABLE(Object obj)                 { return UNREACHABLE(obj.toString());                                 }
    public static RuntimeException UNREACHABLE(String fmt, Object... args) { return UNREACHABLE(String.format(fmt, (Object[])args));             }
    public static RuntimeException UNREACHABLE()                           { return UNREACHABLE(ProcessUtils.caller());                          }

    public static RuntimeException errorNotSpecified(String name) { return FError.New(name + " is not specified");  } 
    public static RuntimeException verifyNotZero(int num, String name)    { if (num == 0)    return errorNotSpecified(name); return null; } 
    public static RuntimeException verifyNotZero(float num, String name)  { if (num == 0.0f) return errorNotSpecified(name); return null; } 
    public static RuntimeException verifyNotNull(Object obj, String name) { if (obj == null) return errorNotSpecified(name); return null; }
    
    public static RuntimeException TODO(String str)                 { return FError.New("NOT IMPLEMENTED: %s", str);   }
    public static RuntimeException TODO(Object obj)                 { return TODO(obj.toString());                     }
    public static RuntimeException TODO(String fmt, Object... args) { return TODO(String.format(fmt, (Object[])args)); }
    public static RuntimeException TODO()                           { return TODO(ProcessUtils.caller());              }
}
