package fipaan.com.errors;

public class FThrow {
    public static void New()                           { throw FError.New();          }
    public static void New(Object obj)                 { throw FError.New(obj);       }
    public static void New(String fmt, Object... args) { throw FError.New(fmt, args); }

    public static void New(Throwable e)                             { throw FError.New(e);            }
    public static void New(Throwable e, Object obj)                 { throw FError.New(e, obj);       }
    public static void New(Throwable e, String fmt, Object... args) { throw FError.New(e, fmt, args); }

    public static void UNREACHABLE()                           { throw FError.UNREACHABLE();          }
    public static void UNREACHABLE(String msg)                 { throw FError.UNREACHABLE(msg);       }
    public static void UNREACHABLE(Object obj)                 { throw FError.UNREACHABLE(obj);       }
    public static void UNREACHABLE(String fmt, Object... args) { throw FError.UNREACHABLE(fmt, args); }

    public static void throwIfError(RuntimeException err) { if (err != null) throw err; } 

    public static void errorNotSpecified(String name) { throw FError.errorNotSpecified(name); } 
    public static void verifyNotZero(int num, String name)    {  throwIfError(FError.verifyNotZero(num, name)); } 
    public static void verifyNotZero(float num, String name)  {  throwIfError(FError.verifyNotZero(num, name)); } 
    public static void verifyNotNull(Object obj, String name) {  throwIfError(FError.verifyNotNull(obj, name)); }
    
    public static void TODO(String str)                 { throw FError.TODO(str);        }
    public static void TODO(Object obj)                 { throw FError.TODO(obj);        }
    public static void TODO(String fmt, Object... args) { throw FError.TODO(fmt, args); }
    public static void TODO() { throw FError.TODO(); }
}
