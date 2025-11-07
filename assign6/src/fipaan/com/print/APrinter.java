package fipaan.com.print;

import fipaan.com.errors.FError;
import fipaan.com.errors.FThrow;
import fipaan.com.is.IsReflector;
import fipaan.com.utils.ConsoleUtils;
import java.io.OutputStream;
import java.io.IOException;

public abstract class APrinter<Self extends APrinter<Self>> implements IsReflector<Self> {
    // customizable
    public String getPrefix()       { throw FError.TODO("add prefix or change default printfTo()"); }
    public Self setPrefix(String p) { throw FError.TODO("add prefix or change default printfTo()"); }
    
    protected Self __printf(OutputStream out, String fmt, Object... args) {
        fmt = String.format(String.format("%%-%ds%%s", PrinterConfig.INDENT), getPrefix(), fmt);
        ConsoleUtils.outWrite(out, String.format(fmt, args));
        return self();
    }
    public OutputStream getOut() { return System.out; }
    public OutputStream getErr() { return System.err; }

    // Doesn't make much sense to customize

    public Self flushOut() {
        try {
            getOut().flush();
        } catch (IOException e) { FThrow.New(e, "flush (out) failed"); }
        return self();
    }
    public Self flushErr() {
        try {
            getErr().flush();
        } catch (IOException e) { FThrow.New(e, "flush (err) failed"); }
        return self();
    }
    public Self flush() { return flushOut().flushErr(); }
    
    public Self printf(String fmt, Object... args)   { return __printf(getOut(), fmt, args);                }
    public Self printf(Object obj)                   { return __printf(getOut(), "%s", obj.toString());     }
    public Self printfn(Object obj)                  { return __printf(getOut(), "%s\r\n", obj.toString()); }
    public Self printfn(String fmt, Object... args)  { return __printf(getOut(), fmt + "\r\n", args);       }

    public Self eprintf(String fmt, Object... args)  { return __printf(getErr(), fmt, args);                }
    public Self eprintf(Object obj)                  { return __printf(getErr(), "%s", obj.toString());     }
    public Self eprintfn(Object obj)                 { return __printf(getErr(), "%s\r\n", obj.toString()); }
    public Self eprintfn(String fmt, Object... args) { return __printf(getErr(), fmt + "\r\n", args);       }
}

