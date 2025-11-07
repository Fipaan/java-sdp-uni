package fipaan.com.console;

import fipaan.com.console.style.*;
import fipaan.com.string.*;
import fipaan.com.printf.*;
import fipaan.com.errors.FError;
import fipaan.com.errors.FThrow;
import fipaan.com.utils.*;
import fipaan.com.*;
import java.util.*;
import java.io.OutputStream;
import java.util.function.*;

public class ConsoleBuffer {
    private StringBuilder sb = new StringBuilder();
    private IConsoleBuffer handler;

    public ConsoleBuffer(IConsoleBuffer h) { handler = h; }
    
    public String handleKey(OutputStream out, String input) { return handleKey(out, new StringCursor(input)); }
    public String handleKey(OutputStream out, StringCursor input) {
        if (input.i >= input.length) return "";
        int code = input.str.codePointAt(input.i);
        ConsoleKey key = ConsoleKey.getByCode(code);
        int start = input.i;
        input.i += 1;
        if (key != null) {
            switch (key) {
                case ConsoleKey.Bell: handler.ringBellAction(); break;
                case ConsoleKey.Backspace: handler.removeAtCursorAction(); break;
                case ConsoleKey.HorTab: {
                    handler.addX(1);
                    int x     = handler.getX();
                    int width = handler.getWidth();
                    if (x >= width) handler
                       .addY(x / width)
                       .setX(x % width);
                } break;
                case ConsoleKey.Newline:     handler.onLinuxResetXAction().addY(1); break;
                case ConsoleKey.VerTab:      handler.addY(1); break;
                case ConsoleKey.Formfeed:    handler.moveToNextPageAction(); break;
                case ConsoleKey.CarriageRet: handler.setX(0); break;
                case ConsoleKey.ESC:         dispatch(ESC_HANDLERS, input); break;
                case ConsoleKey.Delete:      handler.deleteLastAtCurrentLineAction(); break;
                default: FThrow.UNREACHABLE("ConsoleKey");
            }
        } else handler.writeCodepointAction(code);
        handler.writeOutAction(out, input.substring(start, input.i));
        return input.toString();
    }
    public void handleKeys(OutputStream out, String input) {
        while(true) {
            input = handleKey(out, input);
            if (input == null || input.length() == 0) break;
        }
    }
    private static void dispatch(Map<String, Consumer<StringCursor>> map, StringCursor input) {
        for (String prefix : map.keySet()) {
            if (input.startsWith(prefix)) {
                input.i += prefix.length();
                map.get(prefix).accept(input);
                return;
            }
        }
        FThrow.New("No handler found for: " + input);
    }
    private final Map<String, Consumer<StringCursor>> ESC_HANDLERS = Map.of(
        "[", input -> handleCSIKey(input),
        "M", input -> {
            if (handler.getY() > 0) handler.subY(1);
            else handler.scrollUpAction();
        },
        "7", input -> handler.saveDECAction(),
        "8", input -> handler.restoreDECAction(),
        "c", input -> handler.oldClearScreenAction(),
        "(", input -> {
            RuntimeException err = FError.New("Unknown Line Drawing Mode sequence (<ESC>(%s)", input.toString());
            if (input.i >= input.str.length()) throw err;
            switch (input.charAt()) {
                case '0': { input.i += 1; handler.setLineDrawingModeAction(true);  return; }
                case 'B': { input.i += 1; handler.setLineDrawingModeAction(false); return; }
                default: throw err;
            }
        }
    );
    private boolean parseConsoleStyleAny(StringCursor input, ArrayList<ConsoleStyleAny> styles) {
        String numStr = new String(sb);
        sb.setLength(0);
        int code = Integer.parseUnsignedInt(numStr);
        ConsoleStyle style = ConsoleStyle.getByCode(code);
        if (style != null) {
            styles.add(new ConsoleStyleAny(style));
            return true;
        }
        ConsoleColor16 color = ConsoleColor16.getByCode(code);
        if (color != null) {
            styles.add(new ConsoleStyleAny(color));
            return true;
        }
        ConsoleColorMode mode = ConsoleColorMode.getByCode(code);
        if (mode != null) {
            FormattedObj[] fobj = Sscanf.sscanf(";2;%d", input.toString());
            int read = Sscanf.lastReadLength;
            if (read > 0) {
                int value = fobj[0].getInt();
                styles.add(new ConsoleStyleAny(new ConsoleColor256(mode, value)));
                return true;
            } else {
                fobj = Sscanf.sscanf(";5;%d;%d;%d", input.toString());
                read = Sscanf.lastReadLength;
                if (read <= 0) return false;
                input.i += read;
                int r = fobj[0].getInt();
                int g = fobj[1].getInt();
                int b = fobj[2].getInt();
                styles.add(new ConsoleStyleAny(new ConsoleColorRGB(mode, r, g, b)));
                return true;
            }
        } else return false;
    }
    private void handleCSIKey(StringCursor input) {
        char ch = input.charAt();
        String cmd = input.toString();
        if (Character.isDigit(ch)) {
            FormattedObj[] fobj1 = Sscanf.sscanf("%d", cmd);
            int read = Sscanf.lastReadLength;
            if (read <= 0) FThrow.UNREACHABLE("Sscanf");
            int newI = input.i + read;
            if (newI >= input.str.length()) {
                FThrow.New("Expected command after number: <ESC>[%s", cmd);
            }
            int num1 = fobj1[0].getInt();
            
            char afterNum = input.charAt(newI);
            if (afterNum != ';') input.i += read;
            switch (afterNum) {
                case ';': {
                    sb.setLength(0);
                    ArrayList<ConsoleStyleAny> styles = new ArrayList<>();
                    String numStr = input.substring(input.i, input.i + read);
                    sb.append(numStr);
                    input.i += read;
                    int oldI = input.i;
                    String cmdColor = "";
                    boolean validStyles = parseConsoleStyleAny(input, styles);
                    while (validStyles) {
                        input.i += 1;
                        char ch2 = input.charAt();
                        if (Character.isDigit(ch2)) { sb.append(ch2); }
                          else if (ch2 == ';') {
                            input.i += 1;
                            if (!parseConsoleStyleAny(input, styles)) {
                                validStyles = false;
                                break;
                            }
                        } else if (ch2 == 'm') {
                            if (sb.length() == 0) FThrow.New("expected number in style list sequence, got nothing (<ESC>[%s;m)", cmdColor);
                            if (!parseConsoleStyleAny(input, styles)) validStyles = false;
                            else input.i += 1;
                            break;
                        } else validStyles = false;
                    }
                    if (validStyles) {
                        sb.setLength(0);
                        handler.applyStylesAction(ArrayUtils.extractArr(ConsoleStyleAny.class, styles));
                        return;
                    } else input.i = oldI; // ;...
                    /* cursor */
                    boolean isMoving = false;
                    FormattedObj[] fobj2 = Sscanf.sscanf("%d;%dH", cmd);
                    int read2 = Sscanf.lastReadLength;
                    if (read2 > 0) isMoving = true;
                    else {
                        fobj2    = Sscanf.sscanf("%d;%df", cmd);
                        read2 = Sscanf.lastReadLength;
                        if (read2 > 0) isMoving = true;
                    }
                    if (isMoving) {
                        input.i += read2;
                        int row = num1;
                        int col = fobj2[1].getInt();
                        handler
                       .setX(col - 1)
                       .setY(row - 1);
                        return;
                    }
                    FThrow.TODO("{code};{string};{...}p");
                    // NOTE: remaps key to specified string
                    // For more information check:
                    // https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797#keyboard-strings
                } return;
                /* cursor */
                case 'A': handler.subY(num1);         input.i += 1; return;
                case 'B': handler.addY(num1);         input.i += 1; return;
                case 'C': handler.addX(num1);         input.i += 1; return;
                case 'D': handler.subX(num1);         input.i += 1; return;
                case 'E': handler.addY(num1).setX(0); input.i += 1; return;
                case 'F': handler.subY(num1).setX(0); input.i += 1; return;
                case 'G': handler.setX(num1);         input.i += 1; return;
                case 'n': {
                    if (num1 != 6) FThrow.New("unknown ANSI <ESC>[%dn", num1);
                    handler.requestPositionAction();
                    input.i += 1;
                    return;
                }
                /* erase */
                case 'J': {
                    input.i += 1;
                    switch (num1) {
                        case 0: handler.eraseCursorToEOSAction(); return;
                        case 1: handler.eraseCursorToBOSAction(); return;
                        case 2: handler.eraseScreenAction();      return;
                        case 3: handler.eraseStoredLinesAction(); return;
                        default: throw FError.New("unknown erase code (<ESC>[%dJ)", num1);
                    }
                }
                case 'K': {
                    input.i += 1;
                    switch (num1) {
                        case 0: handler.eraseCursorToEOLAction(); return;
                        case 1: handler.eraseCursorToBOLAction(); return;
                        case 2: handler.eraseLineAction();        return;
                        default: throw FError.New("unknown erase code (<ESC>[%dK)", num1);
                    }
                }
                /* cursor shape */
                case 'q': {
                    input.i += 1;
                    CursorShape shape = CursorShape.getByCode(num1);
                    if (shape == null) FThrow.New("unknown cursor shape code (<ESC>[%dq)", num1);
                    handler.setCursorShapeAction(shape);
                    return;
                }
                /* color/style */
                case 'm': {
                    input.i += 1;
                    ConsoleStyle style = ConsoleStyle.getByCode(num1);
                    if (style != null) {
                        handler.setStyleAction(style);
                        return;
                    }
                    ConsoleColor16 color = ConsoleColor16.getByCode(num1);
                    if (color != null) {
                        handler.setColor16Action(color);
                        return;
                    }
                    throw FError.New("Unknown mode sequence: <ESC>[%s", cmd);
                }
            }
            throw FError.New("Unknown ansi sequence: <ESC>[%s", cmd);
        }
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
                FormattedObj[] fobj = Sscanf.sscanf("%d", input.toString());
                int fullRead = Sscanf.lastReadLength;
                if (fullRead <= 0) FThrow.New("Unknown screen instruction: <ESC>[%s", cmd);
                input.i += fullRead;
                int code = fobj[0].getInt();
                ConsoleScreenMode screenMode = ConsoleScreenMode.getByCode(code);
                if (screenMode == null) FThrow.New("Unknown screen instruction: <ESC>[%s", cmd);
                if (input.i == input.length) FThrow.New("Unknown screen instruction: <ESC>[%s", cmd);
                char ch2 = input.charAt();
                input.i += 1;
                switch (ch2) {
                    case 'h': handler.setScreenModeAction(screenMode);   return;
                    case 'l': handler.resetScreenModeAction(screenMode); return;
                    default: throw FError.New("Unknown screen instruction: <ESC>[%s", cmd);
                }
            }
            /* common private modes */
            case '?': {
                FormattedObj[] fobj = Sscanf.sscanf("%d", input.toString());
                int fullRead = Sscanf.lastReadLength;
                if (fullRead <= 0) FThrow.New("Unknown private instruction: <ESC>[%s", cmd);
                input.i += fullRead;
                int code = fobj[0].getInt();
                if (input.i == input.length) FThrow.New("Unknown private instruction: <ESC>[%s", cmd);
                char mode = input.charAt();
                input.i += 1;
                switch (code) {
                    case 25: {
                        boolean isVisible;
                        if (mode == 'l') isVisible = false;
                        else if (mode == 'h') isVisible = true;
                        else throw FError.New("Unknown mode for cursor visibility: <ESC>[%s", cmd);
                        handler.setCursorVisibleAction(isVisible);
                    } return;
                    case 47: {
                        if (mode == 'l') handler.restoreScreenAction();
                        else if (mode == 'h') handler.saveScreenAction();
                        else FThrow.New("Unknown mode for storing screen: <ESC>[%s", cmd);
                    } return;
                    default: throw FError.New("Unknown private instruction: <ESC>[%s", cmd);
                }
            }
            default: throw FError.New("Unknown ansi sequence <ESC>[%s", cmd);
        }
    }
}
