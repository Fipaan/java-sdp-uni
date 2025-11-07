package fipaan.com.print;

import java.io.OutputStream;

public class Info extends APrinter<Info> {
    private String prefix;
    private OutputStream out;
    private OutputStream err;
    
    @Override public String getPrefix()        { return prefix; }
    @Override public Info setPrefix(String p) { prefix = p; return this; }

    @Override public OutputStream getOut() { return out; }
    public Info setOut(OutputStream out) { this.out = out; return this; }

    @Override public OutputStream getErr() { return err; }
    public Info setErr(OutputStream err) { this.err = err; return this; }
                                                                      
    public Info(String prefix, OutputStream out, OutputStream err) { setPrefix(prefix).setOut(out).setErr(err); }
    public Info(String prefix, OutputStream out)                   { this(prefix,   out,        System.err);    }
    public Info(String prefix)                                     { this(prefix,   System.out, System.err);    }
    public Info(OutputStream out, OutputStream err)                { this("INFO:",  out,        err       );    }
    public Info(OutputStream out)                                  { this("INFO:",  out,        System.err);    }
    public Info()                                                  { this("INFO:",  System.out, System.err);    }
}
