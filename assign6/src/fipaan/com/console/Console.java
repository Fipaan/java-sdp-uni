package fipaan.com.console;

import fipaan.com.console.style.*;
import fipaan.com.errors.*;
import fipaan.com.utils.*;
import fipaan.com.print.*;
import fipaan.com.wrapper.*;
import fipaan.com.has.*;
import fipaan.com.array.*;
import fipaan.com.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.util.stream.*;

public class Console extends APrinter<Console> implements IConsoleBuffer<Console> {
    public static <T extends Enum<T> & HasCode<?>> T getByCode(Class<T> enumClass, int code) {
        for (T constant : enumClass.getEnumConstants()) {
            if (constant.getCode() == code)
                return constant;
        }
        return null;
    }

    private ConsoleBuffer buffer;

    private InputStream in;
    private OutputStream out, err;
    private boolean flushing, visible, raw, echo;
    private ConsoleStyle hidden;

    // --- Streams ---

    public InputStream getIn() { return in; }
    public Console setIn(InputStream in) { this.in = in; return this; }

    public OutputStream getOut() { return out; }
    public Console setOut(OutputStream out) { this.out = out; return this; }

    public OutputStream getErr() { return err; }
    public Console setErr(OutputStream err) { this.err = err; return this; }

    // --- State ---

    public boolean isFlushing() { return flushing; }
    public Console setFlushing(boolean isFlushing) { flushing = isFlushing; return this; }
    public Console toggleFlushing() {
        return setFlushing(!isFlushing());
    }
    
    public boolean isCursorVisible() { return visible; }
    public Console toggleCursorVisible() {
        return setCursorVisible(!isCursorVisible());
    }

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
    
    public boolean isHiddenMode() { return hidden == ConsoleStyle.SetHidden; }
    public Console setHiddenMode(boolean isHidden) {
        hidden = isHidden ? ConsoleStyle.SetHidden : ConsoleStyle.ResetHidden;
        return setStyle(hidden);
    }
    public Console toggleHiddenMode() {
        return setHiddenMode(!isHiddenMode());
    }

    // --- Data ---
    private StringBuilder sb;
    private FDimension consoleSizeOld, consoleSize;

    private Array2DInt screen;
    private FPoint cursor, cursorDEC, cursorSCO;

    public Console updateConsoleSize() {
        ConsoleUtils.getConsoleSize(consoleSize);
        setSize(consoleSize);
        return this;
    }
    public Console saveOldSize() { consoleSizeOld.setSize(consoleSize); return this; }
    public boolean isResized()   { return !consoleSizeOld.eqSize(consoleSize); }

    public Array2DInt getScreenArr() { return screen; }

    public int getWidth()    { return screen.getWidth();  }
    public int getHeight()   { return screen.getHeight(); }
    public Console setWidth(int w)  { if (screen.getWidth()  != w) screen.setWidth(w);  return this; }
    public Console setHeight(int h) { if (screen.getHeight() != h) screen.setHeight(h); return this; }
    
    public int getX()   { return cursor.x; }
    public int getY()   { return cursor.y; }
    public Console setX(int x) { cursor.x = x; return this; }
    public Console setY(int y) { cursor.y = y; return this; }

    public Console(InputStream in, OutputStream out, OutputStream err,
                   boolean visible, boolean hidden,
                   boolean raw, boolean echo,
                   boolean flushing)
    {
        sb             = new StringBuilder();
        consoleSizeOld = new FDimension();
        consoleSize    = new FDimension();
        screen         = new Array2DInt(0, 0);
        cursor         = new FPoint();
        cursorDEC      = new FPoint();
        cursorSCO      = new FPoint();

        setIn(in);
        setOut(out);
        setErr(err);
        buffer = new ConsoleBuffer(this);

        setCursorVisible(visible);
        setHiddenMode(hidden);
        setRawMode(raw);
        setEcho(echo);
        setFlushing(flushing);
        flushOut();
    }
    @Override protected Console __printf(OutputStream out, String fmt, Object... args) {
        buffer.handleKeys(out, String.format(fmt, args));
        return this;
    }
    public Console escaped(String fmt, Object... args) {
        buffer.handleKeys(out, ConsoleUtils.escaped(String.format(fmt, args)));
        if (flushing) flushOut();
        return this;
    }
    public Console escaped(Object obj) { return escaped("%s", obj.toString()); }

    public Console moveCursor(HasLocation<?> pos) { return moveCursor(pos.getX(), pos.getY()); }
    public Console moveCursor(int x, int y) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        return escaped("[%d;%dH", y + 1, x + 1);
    }
    public int getAt(int x, int y) {
        return screen.get(x, y);
    }

    public int getCodepoint(HasLocation<?> pos) { return getCodepoint(pos.getX(), pos.getY()); }
    public int getCodepoint(int x, int y) {
        int code = screen.get(x, y);
        if (code == '\0') return ' ';
        return code;
    }

    public Console stylePos(HasLocation<?> pos, String styleIn, String styleOut) {
        return stylePos(pos.getX(), pos.getY(), styleIn, styleOut);
    }
    public Console stylePos(int x, int y, String styleIn, String styleOut) {
        moveCursor(x, y);
        escaped("[%sm", styleIn);
        printf("%s", new String(Character.toChars(getCodepoint(x, y))));
        escaped("[%sm", styleOut);
        return this;
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
    public Console putChar(char ch, HasLocation<?> pos) {  return putChar(ch, pos.getX(), pos.getY()); }
    public Console putChar(char ch, int x, int y) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        moveCursor(x, y);
        printf(String.valueOf(ch));
        return this;
    }
    public Console putCodepoint(int code, HasLocation<?> pos) {  return putCodepoint(code, pos.getX(), pos.getY()); }
    public Console putCodepoint(int code, int x, int y) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        moveCursor(x, y);
        if (code != '\0') printf("%s", new String(Character.toChars(code)));
        else printf(" ");
        return this;
    }
    public Console putStr(String str, HasLocation<?> pos) {  return putStr(str, pos.getX(), pos.getY()); }
    public Console putStr(String str, int x, int y) {
        if (x < 0) x += consoleSize.width;
        if (y < 0) y += consoleSize.height;
        moveCursor(x, y);
        printf(str);
        return this;
    }

    public Console displayArea(Area area) {
        area.displayArea(this);
        return this;
    }
    public Console applyBack(Area area) {
        area.applyBack(this);
        return this;
    }
    public Console restoreBack(Area area) {
        area.restoreBack(this);
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
        fillLineHor(ch, y,         x, x + w);
        fillLineHor(ch, y + h - 1, x, x + w);
        fillLineVer(ch, x,         y, y + h);
        fillLineVer(ch, x + w - 1, y, y + h);
        return this;
    }
    public Console fillBorders(char ch) {
        return fillBorders(ch, 0, 0, -1, -1);
    }
    
    // --- Audio / Alerts ---
    public Console ringBell() { return printf("\u0007"); }
    public Console ringBellAction() {
        return this;
    }

    // --- Screen Modes ---
    public Console setScreenMode(ConsoleScreenMode mode) { return escaped("[=%sh", mode.valueString()); }
    public Console setScreenModeAction(ConsoleScreenMode mode) {
        throw FError.TODO();
    }
    
    public Console resetScreenMode(ConsoleScreenMode mode) { return escaped("[=%sl", mode.valueString()); }
    public Console resetScreenModeAction(ConsoleScreenMode mode) {
        throw FError.TODO();
    }
    
    public Console onLinuxResetXAction() {
        // assuming we always send "\r\n", so it handled anyway there
        return this;
    }

    // --- Screen Management ---
    public Console oldClearScreen() { return escaped("c"); }
    public Console oldClearScreenAction() {
        return this;
    }

    public Console eraseScreen() { return escaped("[2J"); }
    public Console eraseScreenAction() {
        screen.clear();
        return this;
    }
    
    public Console eraseStoredLines() { return escaped("[3J"); }
    public Console eraseStoredLinesAction() {
        return this;
    }
    
    public Console saveScreen() { return escaped("[?47h"); }
    public Console saveScreenAction() {
        return this;
    }

    public Console restoreScreen() { return escaped("[?47l"); }
    public Console restoreScreenAction() {
        throw FError.New("Can't restore, because not stored");
    }
    
    public Console scrollUpAction() { // scroll up
        throw FError.New("Don't store lines above");
    }    

    public Console moveToNextPage() { return printf("\u000c"); }
    public Console moveToNextPageAction() {
        // NOTE: doesn't make much sense in console, usually it just moves one line down
        addY(1);
        return this;
    }

    // --- Erase Operations ---
    public Console eraseCursorToEOS() { return escaped("[0J"); }
    public Console eraseCursorToEOSAction() {
        screen.eraseToEOS(cursor.x, cursor.y);
        return this;
    }

    public Console eraseCursorToBOS() { return escaped("[1J"); }
    public Console eraseCursorToBOSAction() {
        screen.eraseToBOS(cursor.x, cursor.y);
        return this;
    }

    public Console eraseCursorToEOL() { return escaped("[0K"); }
    public Console eraseCursorToEOLAction() {
        screen.eraseToEOL(cursor.x, cursor.y);
        return this;
    }

    public Console eraseCursorToBOL() { return escaped("[1K"); }
    public Console eraseCursorToBOLAction() {
        screen.eraseToBOL(cursor.x, cursor.y);
        return this;
    }

    public Console eraseLine() { return escaped("[2K"); }
    public Console eraseLineAction() {
        screen.eraseLine(cursor.y);
        return this;
    }
    
    public Console removeAtCursor() { throw FError.TODO(); }
    public Console removeAtCursorAction() {
        if (cursor.x <= 0 || cursor.y <= 0 || (cursor.x == 0 && cursor.y == 0)) return this;
        screen.set(' ', cursor.x, cursor.y);
        cursor.x -= 1;
        if (cursor.x < 0) { cursor.x = 0; cursor.y -= 1; }
        return this;
    }

    // --- Cursor Control ---
    public Console setCursorVisible(boolean isVisible) {
        return escaped("[?25%c", isVisible ? 'h' : 'l');
    }
    public Console setCursorVisibleAction(boolean isVisible) {
        visible = isVisible;
        return this;
    }

    public Console setCursorShape(CursorShape shape) { return escaped("[%sq", shape.valueString()); }
    public Console setCursorShapeAction(CursorShape shape) {
        return this;
    }

    public Console requestPosition() { return escaped("[6n"); }
    public Console requestPositionAction() {
        // TODO: kinda complicated
        throw FError.TODO();
    }

    public Console deleteLastAtCurrentLine() { return printf("\u007f"); }
    public Console deleteLastAtCurrentLineAction() {
        screen.eraseChar(cursor.x, cursor.y);
        return this;
    }

    // --- State Save / Restore ---
    public Console saveDEC() { return escaped("7"); }
    public Console saveDECAction() {
        cursorDEC.setLocation(cursor);
        return this;
    }

    public Console restoreDEC() { return escaped("8"); }
    public Console restoreDECAction() {
        cursor.setLocation(cursorDEC);
        return this;
    }

    public Console saveSCO() { return escaped("[s"); }
    public Console saveSCOAction() {
        cursorSCO.setLocation(cursor);
        return this;
    }

    public Console restoreSCO() { return escaped("[u"); }
    public Console restoreSCOAction() {
        cursor.setLocation(cursorSCO);
        return this;
    }

    // --- Drawing / Styles ---
    public Console setLineDrawingMode(boolean isDrawing) {
        return escaped("(%c", isDrawing ? '0' : 'B');
    }
    public Console setLineDrawingModeAction(boolean isDrawing) {
        return this;
    }

    public Console setStyle(ConsoleStyle style) { return escaped("[%sm", style.valueString()); }
    public Console setStyleAction(ConsoleStyle style) {
        switch (style) {
            case ConsoleStyle.SetHidden:
            case ConsoleStyle.ResetHidden: hidden = style; break;
            default: break; // do nothing
        }
        return this;
    }

    public Console applyStyles(Object... styles) {
        if (styles == null || styles.length == 0) return this;
        return escaped("[%sm", ConsoleStyle.pack(styles));
    }
    public Console applyStyles(ConsoleStyleAny[] styles) {
        if (styles.length == 0) return this;
        String stylesStr = Arrays.stream(styles)
            .map(Object::toString)
            .collect(Collectors.joining(";"));
        return escaped("[%sm", stylesStr);
    }
    public Console applyStylesAction(ConsoleStyleAny[] styles) {
        for (ConsoleStyleAny style : styles) {
            switch (style.type) {
                case ConsoleStyleAnyType.Style:    setStyleAction(style.getStyle());       break;
                case ConsoleStyleAnyType.Color16:  setColor16Action(style.getColor16());   break;
                case ConsoleStyleAnyType.Color256: setColor256Action(style.getColor256()); break;
                default: throw FError.UNREACHABLE("ConsoleStyleAny");
            }
        }
        return this;
    }

    public Console setColor16(ConsoleColor16 color) { return escaped("[%sm", color.valueString()); }
    public Console setColor16Action(ConsoleColor16 color) {
        return this;
    }
    public Console setColor256(ConsoleColor256 color) { return escaped("[%sm", color.valueString()); }
    public Console setColor256Action(ConsoleColor256 color) {
        return this;
    }

    // --- Input / Output ---
    public Console writeCodepoint(int key) { return printf(new String(Character.toChars(key))); }
    public Console writeCodepointAction(int key) {
        int width = screen.getWidth();
        int height = screen.getHeight();

        if (cursor.y >= height) return this;
        if (cursor.x >  width)  return this;
        
        screen.set(key, cursor.x, cursor.y);
        if (cursor.x == width) moveCursor(0, cursor.y + 1);
        return this;
    }

    public Console writeOutAction(OutputStream out, String text) {
        if (text != null) ConsoleUtils.outWrite(out, text);
        return this;
    }

    // -- Other --
    private static void sh_call(String fmt, Object... args) throws IOException {
        try {
            new ProcessBuilder("sh", "-c", String.format(fmt, args)).inheritIO().start().waitFor();
        } catch (InterruptedException e) { /* do nothing on interrupt */ }
          catch (IOException e) { throw e; /* handle IOException where it was called */ }
    }
    private static void sh_call(Object obj) throws IOException {
        sh_call("%s", obj.toString());
    }
}
