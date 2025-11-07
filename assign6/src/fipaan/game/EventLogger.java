package fipaan.game;

import fipaan.com.print.APrinter;
import java.io.OutputStream;

public class EventLogger extends APrinter<EventLogger> {
    private String prefix;
    private OutputStream out;
    private OutputStream err;
    
    @Override public String getPrefix()        { return prefix; }
    @Override public EventLogger setPrefix(String p) { prefix = p; return this; }

    @Override public OutputStream getOut() { return out; }
    public EventLogger setOut(OutputStream out) { this.out = out; return this; }

    @Override public OutputStream getErr() { return err; }
    public EventLogger setErr(OutputStream err) { this.err = err; return this; }
                                                                      
    public EventLogger(String prefix, OutputStream out, OutputStream err) { setPrefix(prefix).setOut(out).setErr(err); }
    public EventLogger(String prefix, OutputStream out)                   { this(prefix,   out,        System.err);    }
    public EventLogger(String prefix)                                     { this(prefix,   System.out, System.err);    }
    public EventLogger(OutputStream out, OutputStream err)                { this("EVENT:",  out,        err       );    }
    public EventLogger(OutputStream out)                                  { this("EVENT:",  out,        System.err);    }
    public EventLogger()                                                  { this("EVENT:",  System.out, System.err);    }
}
