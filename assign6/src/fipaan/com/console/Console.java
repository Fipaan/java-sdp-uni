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

// NOTE(1): expect to have cursor in actual rect
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
    public Console setHiddenMode(boolean hidden) {
        return setStyle(hidden ? ConsoleStyle.SetHidden : ConsoleStyle.ResetHidden);
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

    @Override public int getWidth()    { return screen.getWidth();  }
    @Override public int getHeight()   { return screen.getHeight(); }
    @Override public Console setWidth(int w)  { if (screen.getWidth()  != w) screen.setWidth(w);  return this; }
    @Override public Console setHeight(int h) { if (screen.getHeight() != h) screen.setHeight(h); return this; }
    
    @Override public int getX()   { return cursor.x; }
    @Override public int getY()   { return cursor.y; }
    @Override public Console setX(int x) { cursor.x = x; return this; }
    @Override public Console setY(int y) { cursor.y = y; return this; }

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
        if (code != '\0') printf("%s", StringUtils.CodepointToString(code));
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

    public Console consoleKey(ConsoleKey key) { return printf(key.valueString()); }
    public Console privateM(ConsolePrivateMode mode, ConsoleSeqToggle toggle) {
        return escaped("[?%s%s", mode.valueString(), toggle.valueString());
    }

    /* buffer / output */
    @Override public Console writeInBufferAction(int code) {
        int width  = getWidth();
        int height = getHeight();
        // NOTE(1)
        screen.set(code, cursor.x, cursor.y);
        if (cursor.x >= width - 1) {
            int dy = cursor.y - (height - 2);
            if (dy > 0) screen.shiftY(dy);
            moveCursor(0, dy > 0 ? height - 1 : cursor.y + 1);
        } else cursor.x += 1;
        return this;
    }

    @Override public Console writeInBufferAction(int[] codes) {
        int width  = getWidth();
        int height = getHeight();
        // NOTE(1)
        for (int i = 0; i < codes.length; ++i) {
            writeInBufferAction(codes[i]);
        }
        return this;
    }

    @Override public Console writeOutAction(OutputStream out, String str) {
        ConsoleUtils.outWrite(out, str);    
        return this;
    }

    /* simple controls */
    public Console resetLine() { return consoleKey(ConsoleKey.CarriageRet); }
    @Override public Console resetLineAction() {
        cursor.x = 0;
        return this;
    }

    public Console newLine() { return consoleKey(ConsoleKey.Newline); }
    @Override public Console newLineAction() {
        // NOTE(1)
        int height = getHeight();
        int dy = cursor.y - (height - 2);
        if (dy > 0) {
            screen.shiftY(dy);
            cursor.y = height - 1;
        } else cursor.y += 1;
        return this;
    }

    public Console horTab() { return consoleKey(ConsoleKey.HorTab); }
    @Override public Console horTabAction() {
        // NOTE(1)
        // NOTE: assume width at least PrinterConfig.TAB
        int width  = getWidth();
        int height = getHeight();
        cursor.x += PrinterConfig.TAB;
        cursor.x -= cursor.x % PrinterConfig.TAB;
        if (cursor.x >= width) {
            int dy = cursor.y - (height - 1);
            if (dy > 0) screen.shiftY(dy);
            moveCursor(cursor.x - width, dy >= 0 ? height - 1 : cursor.y + 1);
        }
        return this;
    }

    public Console verTab() { return consoleKey(ConsoleKey.VerTab); }
    @Override public Console verTabAction() {
        return newLineAction(); // NOTE: Console has similar behavior with newline
    }

    public Console formFeed() { return consoleKey(ConsoleKey.Formfeed); }
    @Override public Console formFeedAction() {
        return newLineAction(); // NOTE: Console has similar behavior with newline
    }

    public Console backspace() { return consoleKey(ConsoleKey.Backspace); }
    @Override public Console backspaceAction() {
        if (cursor.x > 0) cursor.x -= 1;
        return this;
    }

    public Console delete() { return consoleKey(ConsoleKey.Delete); }
    @Override public Console deleteAction() {
        return this; // NOTE: literally does nothing in console
    }

    public Console bell() { return consoleKey(ConsoleKey.Bell); }
    @Override public Console bellAction() {
        return this; // NOTE: doesn't affect buffer nor cursor
    }

    /* screen / DEC/SCO */
    public Console clearScreen() { return escaped("c"); }
    @Override public Console clearScreenAction() {
        screen.clear();
        return this;
    }

    public Console moveUp() { return escaped("M"); }
    @Override public Console moveUpAction() {
        if (cursor.y <= 0) FThrow.TODO("maintain stored lines");
        cursor.y -= 1;
        return this;
    }

    public Console saveDEC() { return escaped("7"); }
    @Override public Console saveDECAction() {
        cursorDEC.setLocation(cursor);
        return this;
    }

    public Console restoreDEC() { return escaped("8"); }
    @Override public Console restoreDECAction() {
        cursor.setLocation(cursorDEC);
        return this;
    }

    public Console saveSCO() { return escaped("[s"); }
    @Override public Console saveSCOAction() {
        cursorSCO.setLocation(cursor);
        return this;
    }

    public Console restoreSCO() { return escaped("[u"); }
    @Override public Console restoreSCOAction() {
        cursor.setLocation(cursorSCO);
        return this;
    }

    public Console storeScreen(boolean store) {
        return privateM(ConsolePrivateMode.StoreScreenContext,
                        ConsoleSeqToggle.fromBool(store));
    }
    @Override public Console storeScreenAction(boolean store) {
        FThrow.TODO("maintain stored screen");
        return this;
    }

    public Console requestPosition() { return escaped("[6n"); }
    @Override public Console requestPositionAction() {
        FThrow.TODO();
        return this;
    }

    /* erase */
    public Console eraseCursorToEOS() { return escaped("[0J"); }
    @Override public Console eraseCursorToEOSAction() {
        screen.eraseToEOS(cursor.x, cursor.y);
        return this;
    }

    public Console eraseCursorToBOS() { return escaped("[1J"); }
    @Override public Console eraseCursorToBOSAction() {
        screen.eraseToBOS(cursor.x, cursor.y);
        return this;
    }

    public Console eraseScreen() { return escaped("[2J"); }
    @Override public Console eraseScreenAction() {
        screen.erase();
        return this;
    }

    public Console eraseStoredLines() { return escaped("[3J"); }
    @Override public Console eraseStoredLinesAction() {
        // TODO: store lines
        return this;
    }

    public Console eraseCursorToEOL() { return escaped("[0K"); }
    @Override public Console eraseCursorToEOLAction() {
        screen.eraseToEOL(cursor.x, cursor.y);
        return this;
    }

    public Console eraseCursorToBOL() { return escaped("[1K"); }
    @Override public Console eraseCursorToBOLAction() {
        screen.eraseToBOL(cursor.x, cursor.y);
        return this;
    }

    public Console eraseLine() { return escaped("[2K"); }
    @Override public Console eraseLineAction() {
        screen.eraseLine(cursor.y);
        return this;
    }


    /* styles / colors / cursor shape */
    public Console setLineDrawingMode(boolean enable) { return escaped("(%s", enable ? "0" : "B"); }
    @Override public Console setLineDrawingModeAction(boolean enable) {
        FThrow.TODO();
        return this;
    }

    public Console setStyle(ConsoleStyle style) { return escaped("[%sm", style.valueString()); }
    @Override public Console setStyleAction(ConsoleStyle style) {
        switch (style) {
            case ConsoleStyle.SetHidden: {
                hidden = ConsoleStyle.SetHidden;
            } break;
            case ConsoleStyle.ResetHidden:
            case ConsoleStyle.ResetAll: {
                hidden = ConsoleStyle.ResetHidden;
            } break;
        }
        return this;
    }

    public Console setColor16(ConsoleColor16 color) { return escaped("[%sm", color.valueString()); }
    @Override public Console setColor16Action(ConsoleColor16 color) {
        return this;
    }
    
    public Console setColor256(ConsoleColor256 color) { return escaped("[%sm", color.valueString()); }
    public Console setColor256Action(ConsoleColor256 color) {
        return this;
    }

    public Console setColorRGB(ConsoleColorRGB color) { return escaped("[%sm", color.valueString()); }
    public Console setColorRGBAction(ConsoleColorRGB color) {
        return this;
    }

    public Console applyStyleSeq(ConsoleStyleAny style) { return escaped("[%sm", style.valueString()); }
    @Override public Console applyStyleSeqAction(ConsoleStyleAny fullStyle) {
        Deque<ConsoleStyleAny> dq = new LinkedList<>();
        dq.add(fullStyle);
        while (!dq.isEmpty()) {
            if (dq.peek().type == ConsoleStyleAnyType.Compound) {
                ConsoleStyleAny pair = dq.poll();
                dq.addFirst(pair.tail());
                dq.addFirst(pair.head());
                continue;
            }
            ConsoleStyleAny style = dq.poll();
            switch (style.type) {
                case ConsoleStyleAnyType.Style:    setStyleAction(style.getStyle()); break;
                case ConsoleStyleAnyType.Color16:  setColor16Action(style.getColor16()); break;
                case ConsoleStyleAnyType.Color256: setColor256Action(style.getColor256()); break;
                case ConsoleStyleAnyType.ColorRGB: setColorRGBAction(style.getColorRGB()); break;
                case ConsoleStyleAnyType.Compound: throw FError.UNREACHABLE("dq.poll()");
                default: throw FError.UNREACHABLE("ConsoleStyleAnyType");
            }
        }
        return this;
    }

    public Console setCursorShape(CursorShape shape) { return escaped("[%sq", shape.valueString()); }
    @Override public Console setCursorShapeAction(CursorShape shape) {
        return this;
    }


    /* screen/private modes */
    public Console screenMode(ConsoleScreenMode mode, boolean enable) {
        return escaped("[=%s%s", mode.valueString(),
                       ConsoleSeqToggle.fromBool(enable).valueString());
    }
    @Override public Console screenModeAction(ConsoleScreenMode mode, boolean enable) {
        return this;
    }

    public Console setCursorVisible(boolean visible) {
        return privateM(ConsolePrivateMode.CursorVisibility,
                        ConsoleSeqToggle.fromBool(visible));
    }
    @Override public Console cursorVisibilityAction(boolean isVisible) {
        visible = isVisible;
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
