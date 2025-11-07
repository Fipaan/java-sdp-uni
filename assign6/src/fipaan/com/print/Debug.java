package fipaan.com.print;

import java.lang.Thread;
import java.io.OutputStream;

public class Debug extends APrinter<Debug> {
    public void breakpoint() {
        Thread t = Thread.currentThread();
        printfn("Breakpoint hit in %s", t.getName());
    }
    
    private String prefix;
    private OutputStream out;
    private OutputStream err;
    
    @Override public String getPrefix()        { return prefix; }
    @Override public Debug setPrefix(String p) { prefix = p; return this; }

    @Override public OutputStream getOut() { return out; }
    public Debug setOut(OutputStream out) { this.out = out; return this; }

    @Override public OutputStream getErr() { return err; }
    public Debug setErr(OutputStream err) { this.err = err; return this; }
                                                                      
    public Debug(String prefix, OutputStream out, OutputStream err) { setPrefix(prefix).setOut(out).setErr(err); }
    public Debug(String prefix, OutputStream out)                   { this(prefix,   out,        System.err);    }
    public Debug(String prefix)                                     { this(prefix,   System.out, System.err);    }
    public Debug(OutputStream out, OutputStream err)                { this("DEBUG:", out,        err       );    }
    public Debug(OutputStream out)                                  { this("DEBUG:", out,        System.err);    }
    public Debug()                                                  { this("DEBUG:", System.out, System.err);    }
}
