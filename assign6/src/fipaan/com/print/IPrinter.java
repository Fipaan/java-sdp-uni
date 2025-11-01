package fipaan.com.print;

import fipaan.com.errors.*;
import fipaan.com.utils.*;
import java.io.*;

public interface IPrinter<Self extends IPrinter<Self>> {
    default String getPrefix()       { throw FError.UNREACHABLE("no prefix present"); }
    default Self setPrefix(String p) { throw FError.UNREACHABLE("no prefix present"); }

    default OutputStream getOut() { return System.out; }
    default Self flushOut() {
        try {
            getOut().flush();
        } catch (IOException e) { FError.New(e, "flush failed"); }
        return (Self)this;
    }

    default OutputStream getErr() { return System.err; }
    default Self flushErr() {
        try {
            getErr().flush();
        } catch (IOException e) { FError.New(e, "flush failed"); }
        return (Self)this;
    }
    
    default Self flush() { return flushOut().flushErr(); }

    default Self printf(String fmt, Object... args) {
        fmt = String.format(String.format("%%-%ds%%s", PrinterConfig.INDENT), getPrefix(), fmt);
        ConsoleUtils.outWrite(getOut(), String.format(fmt, args));
        return (Self)this;
    }

    default Self printf(Object obj) {
        return printf("%s", obj.toString());
    }

    default Self printfn(Object obj) {
        return printf("%s\r\n", obj.toString());
    }

    default Self printfn(String fmt, Object... args) {
        return printf(fmt + "\r\n", args);
    }
}

