package fipaan.com.console;

import fipaan.com.errors.*;
import fipaan.com.utils.*;
import fipaan.com.print.*;
import fipaan.com.geom.wrapper.*;
import fipaan.com.*;
import java.io.*;
import java.lang.*;
import java.awt.*;

public class Console implements IPrinter<Console> {
    private InputStream in;
    public InputStream getIn() { return in; }
    public Console setIn(InputStream in) { this.in = in; return this; }
/* TODO:
ReadableByteChannel channel = Channels.newChannel(socket.getInputStream());
ByteBuffer buffer = ByteBuffer.allocate(1024);

channel.configureBlocking(false);  // enable non-blocking mode

int bytesRead = channel.read(buffer);
if (bytesRead > 0) {
    buffer.flip();
    // process data
} else if (bytesRead == 0) {
    // no data available now
} else {
    // end of stream
}
*/

    private OutputStream out;
    public OutputStream getOut() { return out; }
    public Console setOut(OutputStream out) { this.out = out; return this; }

    private OutputStream err;
    public OutputStream getErr() { return err; }
    public Console setErr(OutputStream err) { this.err = err; return this; }

    private boolean flushing;
    public boolean isFlushing() { return flushing; }
    public Console setFlushing(boolean isFlushing) { flushing = isFlushing; return this; }
    public Console toggleFlushing() {
        return setFlushing(!isFlushing());
    }
    
    private boolean visible;
    public boolean isCursorVisible() { return visible; }
    public Console setCursorVisible(boolean isVisible) {
        visible = isVisible;
        return escaped("[?25%s", visible ? "h" : "l");
    }
    public Console toggleCursorVisible() {
        return setCursorVisible(!isCursorVisible());
    }
    
    private boolean hidden;
    public boolean isHiddenMode() { return hidden; }
    public Console setHiddenMode(boolean isHidden) {
        hidden = isHidden;
        return escaped("[%s8m", hidden ? "" : "2");
    }
    public Console toggleHiddenMode() {
        return setHiddenMode(!isHiddenMode());
    }

    private boolean raw;
    public boolean isRawMode() { return raw; }
    public Console setRawMode(boolean isRaw) {
        raw = isRaw;
        try {
            sh_call("stty raw %secho < /dev/tty", raw ? "-" : "");
        } catch (IOException e) {
            throw FError.New(e, "Could not %s raw mode", raw ? "enable" : "disable");
        }
        return this;
    }
    public Console toggleRawMode() {
        return setRawMode(!isRawMode());
    }

    private boolean echo;
    public boolean isEcho() { return echo; }
    public Console setEcho(boolean isEcho) {
        echo = isEcho;
        try {
            sh_call("stty %secho < /dev/tty", echo ? "" : "-");
        } catch (IOException e) {
            throw FError.New(e, "Could not %s echo", echo ? "restore" : "disable");
        }
        return this;
    }
    public Console toggleEcho() {
        return setEcho(!isEcho());
    }

    private StringBuilder sb;
    public StringBuilder getStringBuilder() { return sb; }
    public Console setStringBuilder(StringBuilder sb) { this.sb = sb; return this; }

    private FDimension consoleSizeOld;
    private FDimension consoleSize;
    public Console updateConsoleSize() {
        ConsoleUtils.getConsoleSize(consoleSize);
        return this;
    }
    public Console saveOldSize() { consoleSizeOld.setSize(consoleSize); return this; }
    public boolean isResized() { return !consoleSizeOld.eqSize(consoleSize); }

    public Console(InputStream in, OutputStream out, OutputStream err,
                   boolean visible, boolean hidden,
                   boolean raw, boolean echo,
                   boolean flushing)
    {
        setIn(in);
        setOut(out);
        setErr(err);
        setCursorVisible(visible);
        setHiddenMode(hidden);
        setRawMode(raw);
        setEcho(echo);
        setFlushing(flushing);
        flushOut();
        sb = new StringBuilder();
        consoleSizeOld = new FDimension();
        consoleSize    = new FDimension();
    }
    public Console printf(String fmt, Object... args) {
        // TODO: preserve state
        ConsoleUtils.outWrite(out, String.format(fmt, args));
        return this;
    }
    public Console escaped(String fmt, Object... args) {
        // TODO: preserve state
        ConsoleUtils.escaped(out, flushing, fmt, args);
        return this;
    }
    public Console escaped(Object obj) {
        ConsoleUtils.escaped(out, flushing, "%s", obj.toString());
        return this;
    }
    public Console setMode(Object... modes) {
        if (modes == null || modes.length == 0) return this;
        return escaped("[%sm", ConsoleStyle.pack(modes));
    }

    public Console moveCursor(int x, int y) {
        return escaped("[%d;%df", y + 1, x + 1);
    }
    public Console scrollUp() { return escaped("M"); }
    public Console scrollDown() { return escaped("D"); }
    public Console saveCursor() { return escaped("7"); }
    public Console restoreCursor() { return escaped("8"); }
    public Console eraseScreen() { return escaped("[2J"); }
    public Console eraseSavedLines() { return escaped("[3J"); }
    public Console clearScreen() { return escaped("c"); }

    private static void sh_call(String fmt, Object... args) throws IOException {
        try {
            new ProcessBuilder("sh", "-c", String.format(fmt, args)).inheritIO().start().waitFor();
        } catch (InterruptedException e) { /* do nothing on interrupt */ }
          catch (IOException e) { throw e; /* handle IOException where it was called */ }
    }
    private static void sh_call(Object obj) throws IOException {
        sh_call("%s", obj.toString());
    }
    public Console fillRect(char ch) {
        StringUtils.memset(sb, ch, consoleSize.width);
        String line = new String(sb);
        moveCursor(0, 0);
        for (int i = 1; i < consoleSize.height; ++i) {
            printfn(line);
        }
        return this;
    }
    public Console putChar(char ch, int x, int y) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        moveCursor(x, y);
        printf(String.valueOf(ch));
        return this;
    }
    public Console putStr(String str, int x, int y) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        moveCursor(x, y);
        printf(str);
        return this;
    }
    public Console fillLineHor(char ch, int y, int startX, int endX) {
        if (y      < 0) y      += consoleSize.height;
        if (startX < 0) startX += consoleSize.width;
        if (endX   < 0) endX   += consoleSize.width;
        moveCursor(startX, y);
        StringUtils.memset(sb, ch, endX - startX);
        String line = new String(sb);
        return printf(line);
    }
    public Console fillLineHor(char ch, int y, int margin) {
        return fillLineHor(ch, y, margin, consoleSize.width - margin);
    }
    public Console fillLineHor(char ch, int y) {
        return fillLineHor(ch, y, 0, consoleSize.width);
    }
    public Console fillLineVer(char ch, int x, int startY, int endY) {
        if (x      < 0) x      += consoleSize.width;
        if (startY < 0) startY += consoleSize.height;
        if (endY   < 0) endY   += consoleSize.height;
        for (int h = startY; h < endY; ++h) {
            moveCursor(x, h);
            printf(String.valueOf(ch));
        }
        return this;
    }
    public Console fillLineVer(char ch, int x, int margin) {
        return fillLineVer(ch, x, margin, consoleSize.height - margin);
    }
    public Console fillLineVer(char ch, int x) {
        return fillLineVer(ch, x, 0, consoleSize.height);
    }
    public Console fillBorders(char ch, int x, int y, int w, int h) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        if (w < 0) w += consoleSize.width  + 1;
        if (h < 0) h += consoleSize.height + 1;
        fillLineHor(ch, y,     x, x + w);
        fillLineHor(ch, y + h, x, x + w);
        fillLineVer(ch, x,     y, y + h);
        fillLineVer(ch, x + w, y, y + h);
        return this;
    }
    public Console fillBorders(char ch) {
        return fillBorders(ch, 0, 0, -1, -1);
    }
}
