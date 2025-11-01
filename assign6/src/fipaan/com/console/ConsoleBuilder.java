package fipaan.com.console;

import java.io.*;

public class ConsoleBuilder {
    private InputStream in;
    private OutputStream out, err;
    private boolean visible, hidden;
    private boolean raw, echo;
    private boolean flushing;
    public ConsoleBuilder() {
        in  = System.in;
        out = System.out;
        err = System.err;
        visible  = true;
        hidden   = false;
        raw      = false;
        echo     = true;
        flushing = false;
    }
    public ConsoleBuilder setIn(InputStream in)               { this.in  = in;         return this; }
    public ConsoleBuilder setOut(OutputStream out)            { this.out = out;        return this; }
    public ConsoleBuilder setErr(OutputStream err)            { this.err = err;        return this; }
    public ConsoleBuilder setCursorVisible(boolean isVisible) { visible  = isVisible;  return this; }
    public ConsoleBuilder setHiddenMode(boolean isHidden)     { hidden   = isHidden;   return this; }
    public ConsoleBuilder setRawMode(boolean isRaw)           { raw      = isRaw;      return this; }
    public ConsoleBuilder setEcho(boolean isEcho)             { echo     = isEcho;     return this; }
    public ConsoleBuilder setFlushing(boolean isFlushing)     { flushing = isFlushing; return this; }
    public Console build() {
        return new Console(in, out, err,
                   visible, hidden,
                   raw, echo,
                   flushing);
    }
}
