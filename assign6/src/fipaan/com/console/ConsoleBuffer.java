package fipaan.com.console;

import fipaan.com.console.style.*;
import fipaan.com.string.StringCursor;
import fipaan.com.errors.FError;
import fipaan.com.errors.FThrow;
import fipaan.com.utils.ArrayUtils;
import java.util.*;
import java.io.OutputStream;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ConsoleBuffer {
    private StringBuilder sb = new StringBuilder();
    private IConsoleBuffer<?> handler;

    private static final boolean DEBUG = false;
    private BufferedWriter writer;

    private void log(String fmt, Object... args) {
        if (DEBUG) {
            try {
                writer.write(String.format(fmt, args));
                writer.flush();
            } catch (IOException e) { FThrow.New(e, "log failed"); }
        }
    }

    public ConsoleBuffer(IConsoleBuffer<?> h) {
        if (DEBUG) {
            try {
                writer = new BufferedWriter(new FileWriter("log.txt", false));
            } catch (IOException e) { FThrow.New(e, "open failed"); }
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (writer != null) writer.close();
                } catch (IOException e) { FThrow.New(e, "close failed"); }
            }));
        }
        handler = h;
    }

    public String handleKey(OutputStream out, String input) {
        return handleKey(out, new StringCursor(input)).toString();
    }
    private boolean collectPrintable(StringCursor input) {
        while (!input.isExhausted()) {
            int code = input.codePointAt();
            if (ConsoleKey.getByCode(code) != null) break;
            sb.appendCodePoint(code);
            input.i += 1;
        }
        return sb.length() > 1;
    }
    private void handlePrintable(OutputStream out, StringCursor input, int start, int first) {
        sb.setLength(0);
        sb.appendCodePoint(first);
        boolean collected = collectPrintable(input);
        handler.writeOutAction(out, input.toI(start));
        log("handlePrintable: %s\r\n", new String(sb));
        if (collected) {
            handler.writeInBufferAction(sb.codePoints().toArray());
        } handler.writeInBufferAction(first);
    }
    private ConsoleBufferError getErrorHandler(String cmd) {
        return (e, fmt, args) -> {
            fmt += " (<ESC>" + cmd + ")";
            if (e == null) return FError.New(fmt, args);
            else return FError.New(e, fmt, args);
        };
    }
    private void handleNonPrintable(OutputStream out, StringCursor input, int start, ConsoleKey key) {
        switch (key) {
            case ConsoleKey.CarriageRet: handler.resetLineAction(); break;
            case ConsoleKey.Newline:     handler.newLineAction();   break;
            case ConsoleKey.ESC: {
                if (input.isExhausted()) FThrow.New("Incomplete ANSI sequence");
                ConsoleBufferError error = getErrorHandler(input.toString());
                log("handleNonPrintable: cmd: %s\r\n", input.toString());
                try {
                switch (input.getCodeIAdvance()) {
                    case '[': handleCSIKey(input, error);  break;
                    case 'M': handler.moveUpAction();      break; // scroll if needed
                    case '7': handler.saveDECAction();     break;
                    case '8': handler.restoreDECAction();  break;
                    case 'c': handler.clearScreenAction(); break;
                    case '(': {
                        if (input.isExhausted()) throw error.lineDrawing("incomplete, expected enable(0)/disable(B)");
                        switch (input.getCodeIAdvance()) {
                            case '0': handler.setLineDrawingModeAction(true); break;
                            case 'B': handler.setLineDrawingModeAction(false); break;
                            default: throw error.lineDrawing("invalid, expected enable(0)/disable(B)");
                        }
                    } break;
                    default: throw error.get();
                }
                } catch (RuntimeException e) {
                    if (DEBUG) e.printStackTrace(new PrintWriter(writer, true));
                    throw e;
                }
            } break;
            case ConsoleKey.HorTab:      handler.horTabAction();    break;
            case ConsoleKey.Backspace:   handler.backspaceAction(); break;
            case ConsoleKey.Bell:        handler.bellAction();      break;
            case ConsoleKey.VerTab:      handler.verTabAction();    break;
            case ConsoleKey.Formfeed:    handler.formFeedAction();  break;
            case ConsoleKey.Delete:      handler.deleteAction();    break;
            default: throw FError.UNREACHABLE("ConsoleKey");
        }
        handler.writeOutAction(out, input.toI(start));
    }
    public StringCursor handleKey(OutputStream out, StringCursor input) {
        if (input.isExhausted()) return null;
        int start = input.i;
        int code = input.getCodeIAdvance();
        ConsoleKey key = ConsoleKey.getByCode(code);

        if (key == null) handlePrintable(out, input, start, code);
        else handleNonPrintable(out, input, start, key);

        return input;
    }
    public void handleKeys(OutputStream out, String input) {
        StringCursor sc = new StringCursor(input);
        while (!sc.isExhausted()) handleKey(out, sc);
    }
    private void parseConsoleStyleAny(StringCursor input, ConsoleBufferError error, ConsoleStyleAny style, int code) {
        ConsoleStyle styleSimple = ConsoleStyle.getByCode(code);
        if (styleSimple != null) {
            style.add(styleSimple);
            return;
        }
        ConsoleColor16 color16 = ConsoleColor16.getByCode(code);
        if (color16 != null) {
            style.add(color16);
            return;
        }
        ConsoleColorMode mode = ConsoleColorMode.getByCode(code);
        if (mode == null) throw error.numbers("incomplete sequence, expected color mode ForeTrue16(38) or BackTrue16(38)");
        {
            if (!input.expectCode(';')) throw error.numbers("incomplete sequence, expected colorspace Color256(;2) or RGB(;5)");
            Integer spaceInt = input.getInt();
            if (spaceInt == null) throw error.numbers("incomplete sequence, expected colorspace Color256(2) or RGB(5)");
            ConsoleColorSpace space = ConsoleColorSpace.getByCode(spaceInt.intValue());
            if (space == null) throw error.numbers("unknown colorspace, expected Color256(2) or RGB(5)"); 
            switch (space) {
                case ConsoleColorSpace.Color256: {
                    if (!input.expectCode(';')) throw error.numbers("unknown sequence, expected ID(;<num: 0..255>)");
                    Integer id = input.getInt();
                    if (id == null) throw error.numbers("unknown sequence, expected ID(<num: 0..255>)");
                    try {
                        style.add(new ConsoleColor256(mode, id.intValue()));
                    } catch (RuntimeException e) { throw error.get(e, "unknown sequence"); }
                } return;
                case ConsoleColorSpace.RGB: {
                    if (!input.expectCode(';')) throw error.numbers("unknown sequence, expected red(;<num: 0..255>)");
                    Integer r = input.getInt();
                    if (r == null) throw error.numbers("unknown sequence, expected red(<num: 0..255>)");
                    if (!input.expectCode(';')) throw error.numbers("unknown sequence, expected green(;<num: 0..255>)");
                    Integer g = input.getInt();
                    if (g == null) throw error.numbers("unknown sequence, expected green(<num: 0..255>)");
                    if (!input.expectCode(';')) throw error.numbers("unknown sequence, expected blue(;<num: 0..255>)");
                    Integer b = input.getInt();
                    if (b == null) throw error.numbers("unknown sequence, expected blue(<num: 0..255>)");
                    try {
                        style.add(new ConsoleColorRGB(mode, r.intValue(), g.intValue(), b.intValue()));
                    } catch (RuntimeException e) { throw error.get(e, "unknown sequence"); }
                } return;
                default: throw error.numbers("unknown colorspace, expected Color256(2) or RGB(5)");
            }
        }
    }
    private void handleCSIKey(StringCursor input, ConsoleBufferError error) {
        char ch = input.charAt();
        if (Character.isDigit(ch)) handleCSINumKey(input, error);
        else handleCSICharKey(input, error, ch);
    }
    private void handleCSINumKey(StringCursor input, ConsoleBufferError error) {
        int seqI = input.i;
        int arg1 = input.getInt().intValue();
        if (input.isExhausted()) throw error.get("Expected command after number");

        switch (input.getCodeIAdvance()) {
            case ';': {
                Integer arg2Wrap = input.getInt();
                if (arg2Wrap == null) throw error.get("expected number after ';'");
                if (input.isExhausted()) {
                    throw error.get("incomplete sequence, expected move instruction(H/f) or styles (#;...;#m)");
                }
                int arg2 = arg2Wrap.intValue();

                int code = input.codePointAt();
                if (!Character.isDigit(code)) {
                    input.i += 1;
                    switch (code) {
                        case 'H': // arg1: row, arg2: column
                        case 'f': handler.setX(arg2 - 1).setY(arg1 - 1); return;
                        case 'm': break;
                        default: throw error.numbers("unknown sequence, expected move instruction(H/f) or styles (#;...;#m)");
                    }
                }
                input.i = seqI; // #;...;#m
                boolean finished = false;
                ConsoleStyleAny style = new ConsoleStyleAny();
                while (!finished) {
                    Integer styleInt = input.getInt();
                    if (styleInt == null) throw error.numbers("expected number in style sequence");
                    parseConsoleStyleAny(input, error, style, styleInt.intValue());
                    if (input.isExhausted()) throw error.numbers("incomplete sequence, expected next style separator(;) or end of sequence(m)");
                    switch (input.getCodeIAdvance()) {
                        case ';': break; // parse next style
                        case 'm': finished = true; break;
                        default: throw error.numbers("unknown sequence, expected next style separator(;) or end of sequence(m)");
                    }
                }
                handler.applyStyleSeqAction(style);
                // NOTE: "{code};{string};{...}p" is not implemented,
                // because most of modern terminals does not support
                // it for safety reasons.
                // Action: remaps key to specified string
                // For more information check:
                // https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797#keyboard-strings
            } return;
            /* cursor */
            case 'A': handler.subY(arg1).clampY();         return;
            case 'D': handler.subX(arg1).clampX();         return;
            case 'F': handler.subY(arg1).clampY().setX(0); return;
            case 'B': handler.addY(arg1).clampY();         return;
            case 'C': handler.addX(arg1).clampX();         return;
            case 'E': handler.addY(arg1).clampY().setX(0); return;
            case 'G': handler.setX(arg1).clampX();         return;
            case 'n': {
                if (arg1 != 6) throw error.get("Unknown ANSI sequence");
                handler.requestPositionAction();
                return;
            }
            /* erase */
            case 'J': {
                switch (arg1) {
                    case 0: handler.eraseCursorToEOSAction(); return;
                    case 1: handler.eraseCursorToBOSAction(); return;
                    case 2: handler.eraseScreenAction();      return;
                    case 3: handler.eraseStoredLinesAction(); return;
                    default: throw error.get("Unknown Erase Screen Code");
                }
            }
            case 'K': {
                switch (arg1) {
                    case 0: handler.eraseCursorToEOLAction(); return;
                    case 1: handler.eraseCursorToBOLAction(); return;
                    case 2: handler.eraseLineAction();        return;
                    default: throw error.get("Unknown Erase Line Code");
                }
            }
            /* cursor shape */
            case 'q': {
                CursorShape shape = CursorShape.getByCode(arg1);
                if (shape == null) FThrow.New("Unknown Cursor Shape code");
                handler.setCursorShapeAction(shape);
                return;
            }
            /* color/style */
            case 'm': {
                ConsoleStyle style = ConsoleStyle.getByCode(arg1);
                if (style != null) {
                    handler.setStyleAction(style);
                    return;
                }
                ConsoleColor16 color = ConsoleColor16.getByCode(arg1);
                if (color != null) {
                    handler.setColor16Action(color);
                    return;
                }
                throw error.get("Unknown Graphics Mode sequence");
            }
        }
        throw error.get();
    }
    private void handleCSICharKey(StringCursor input, ConsoleBufferError error, char ch) {
        input.i += 1;
        switch (ch) {
            /* cursor */
            case 'H': handler.setLocation(0, 0);  return;
            case 's': handler.saveSCOAction();    return;
            case 'u': handler.restoreSCOAction(); return;
            /* erase */
            case 'J': handler.eraseCursorToEOSAction(); return;
            case 'K': handler.eraseCursorToEOLAction(); return;
            /* screen */
            case '=': {
                Integer num = input.getInt();
                if (num == null) throw error.screen("expected number");
                if (input.isExhausted()) throw error.screen("incomplete, expected set(h)/reset(l)");

                ConsoleScreenMode mode = ConsoleScreenMode.getByCode(num.intValue());
                if (mode == null) throw error.screen("unknown screen mode");

                ConsoleSeqToggle toggle = ConsoleSeqToggle.getByCode(input.getCodeIAdvance());
                if (toggle == null) throw error.screen("invalid, expected set(h)/reset(l)");

                handler.screenModeAction(mode, toggle.toBool());
            } return;
            /* common private modes */
            case '?': {
                Integer num = input.getInt();
                if (num == null) throw error.privateM("expected number");
                
                ConsolePrivateMode mode = ConsolePrivateMode.getByCode(num.intValue());
                if (mode == null) throw error.privateM("unknown private mode");

                switch (mode) {
                    case ConsolePrivateMode.CursorVisibility: {
                        if (input.isExhausted()) {
                            throw error.privateM("incomplete, expected cursor visible(h)/invisible(l)");
                        }
                        ConsoleSeqToggle toggle = ConsoleSeqToggle.getByCode(input.getCodeIAdvance());
                        if (toggle == null) throw error.privateM("invalid, expected cursor visible(h)/invisible(l)");
                        handler.cursorVisibilityAction(toggle.toBool());
                    } return;
                    case ConsolePrivateMode.StoreScreenContext: {
                        if (input.isExhausted()) {
                            throw error.privateM("incomplete, expected screen store(h)/restore(l)");
                        }
                        ConsoleSeqToggle toggle = ConsoleSeqToggle.getByCode(input.getCodeIAdvance());
                        if (toggle == null) throw error.privateM("invalid, expected screen store(h)/restore(l)");
                        
                        handler.storeScreenAction(toggle.toBool());
                    } return;
                    default: throw error.privateM();
                }
            }
            default: throw error.get();
        }
    }
}
