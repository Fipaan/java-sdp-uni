package fipaan.com.console;

import java.util.ArrayList;

public class ConsoleBuffer {
    private StringBuilder sb = new StringBuilder();
    private IConsoleBuffer handler;

    public ConsoleBuffer(IConsoleBuffer h) { handler = h; }
    public ConsoleBuffer(Console console) { this(new ConsoleBufferHandler(console)); }
    
    public String handleKey(String input) { return handleKey(new StringCursor(input)); }
    public String handleKey(StringCursor input) {
        if (input.i >= input.length) return "";
        int code = input.str.codepointAt(input.i);
        ConsoleKey key = ConsoleKey.getByCode(code);
        if (key != null) {
            switch (key) {
                case ConsoleKey.Bell: {
                    handler.ringBell();
                    input.i += 1;
                } break;
                case ConsoleKey.Backspace: {
                    if (handler.getX() > 0) handler.subX(1);
                    input.i += 1;
                } break;
                case ConsoleKey.HorTab: {
                    handler.addX(1);
                    int x     = handler.getX();
                    int width = handler.getWidth();
                    if (x >= width) handler
                       .addY(x / width)
                       .setX(x % width);
                    input.i += 1;
                } break;
                case ConsoleKey.Newline: {
                    handler
                   .onLinuxResetX()
                   .addY(1);
                    input.i += 1;
                } break;
                case ConsoleKey.VerTab: {
                    handler
                   .addY(1);
                    input.i += 1;
                } break;
                case ConsoleKey.Formfeed: {
                    handler
                   .moveToNextPage(1);
                    input.i += 1;
                } break;
                case ConsoleKey.CarriageRet: {
                    handler
                   .setX(0);
                    input.i += 1;
                } break;
                case ConsoleKey.ESC: {
                    input.i += 1;
                    dispatch(ESC_HANDLERS, input);
                    if (input.i < 0) return null;
                } break;
                case ConsoleKey.Delete: {
                    handler.deleteLastAtCurrentLine();
                    input.i += 1;
                } break;
            }
        }
    }
    private static void dispatch(Map<String, Consumer<StringCursor>> map, StringCursor input) {
        for (String prefix : map.keySet()) {
            if (input.startsWith(prefix)) {
                input.i += prefix.length();
                map.get(prefix).apply(input);
                return;
            }
        }
        throw FError.New("No handler found for: " + input);
    }
    private static final Map<String, Consumer<StringCursor>> ESC_HANDLERS = Map.of(
        "[", input -> { input.i += 1; handleCSIKey(input); },
        "M", input -> {
            input.i += 1;
            if (handler.getY() > 0) handler.subY(1);
            else handler.scroll();
        },
        "7", input -> { input.i += 1; handler.saveDEC();             },
        "8", input -> { input.i += 1; handler.restoreDEC();          },
        "c", input -> { input.i += 1; handler.oldClearScreen();      },
        "(", input -> {
            if (input.i == input.str.length()) { input.i = -1; return; }
            switch (input.str.charAt(i)) {
                case '0': { input.i += 2; handler.setLineDrawingMode(true);  return; }
                case 'B': { input.i += 2; handler.setLineDrawingMode(false); return; }
                default: throw FError.New("Unknown Line Drawing Mode sequence (<ESC>(%s)", input.toString());
            }
        }
    );
    private void handleCSIKey(StringCursor input) {
        char ch = input.charAt();
        if (Character.isDigit(ch)) {
            String cmd = input.toString();
            FormattedObj[] fobj1 = Sscanf.sscanf("%d", cmd);
            int read = lastReadLength;
            if (read > 0) {
                input.i += read;
                int num1 = fobj1[0].getInt();
                switch (input.charAt()) {
                    case ';': {
                        FormattedObj[] fobj1 = Sscanf.sscanf("%d;%d;%dm", cmd);
                        int readColor = lastReadLength;
                        if (readColor > 0) {
                            /* color/style */
                            `[#;...;#m`   -> /* color/style seq, doesn't affect buffer */
                            
                            `[38;5;{ID}m` -> /* Set foreground color. (256 table)      */
                            `[48;5;{ID}m` -> /* Set background color. (256 table)      */
                            `[38;2;{ID}m` -> /* Set foreground color. (RGB table)      */
                            `[48;2;{ID}m` -> /* Set background color. (RGB table)      */
                        }
                        /* cursor */
                        `[{r};{c}H` ||
                        `[{r};{c}f`   -> x = c - 1; y = r -1; /* column/row notaion is not convenient */
                        /* other */
                        `[{code};{string};{...}p` -> /* remaps key to specified string, for more information check https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797#keyboard-strings */
                    } break;
                    /* cursor */
                    case 'A': handler.subY(num1);         input.i += 1; return;
                    case 'B': handler.addY(num1);         input.i += 1; return;
                    case 'C': handler.addX(num1);         input.i += 1; return;
                    case 'D': handler.subX(num1);         input.i += 1; return;
                    case 'E': handler.addY(num1).setX(0); input.i += 1; return;
                    case 'F': handler.subY(num1).setX(0); input.i += 1; return;
                    case 'G': handler.setX(num1);         input.i += 1; return;
                    case 'n': {
                        if (num1 != 6) throw FError.New("unknown ANSI <ESC>[%dn", num1);
                        handler.requestPosition();
                        input.i += 1;
                        return;
                    }
                    /* erase */
                    case 'J': {
                        switch num1: {
                            case 0: handler.eraseCursorToEOS(); return;
                            case 1: handler.eraseCursorToBOS(); return;
                            case 2: handler.eraseScreen();      return;
                            case 3: handler.eraseStoredLines(); return;
                            default: throw FError.New("unknown erase code (<ESC>[%dJ)", num1);
                        }
                    }
                    case 'K': {
                        switch num1: {
                            case 0: handler.eraseCursorToEOL(); return;
                            case 1: handler.eraseCursorToBOL(); return;
                            case 2: handler.eraseLine();        return;
                            default: throw FError.New("unknown erase code (<ESC>[%dK)", num1);
                        }
                    }
                    /* cursor shape */
                    case 'q': {
                        CursorShape shape = CursorShape.getByCode(num1);
                        if (shape == null) {
                            throw FError.New("unknown cursor shape code (<ESC>[%dq)", num1);
                        }
                        handler.setCursorShape(shape);
                        return;
                    }
                    /* color/style */
                    case 'm': {
                        ConsoleStyle style = ConsoleStyle.getByCode(num1);
                        if (style != null) {
                            handler.setStyle(style);
                        }
                        ConsoleColor16 color = ConsoleColor16.getByCode(num1);
                        if (color != null) {
                            if (color.isColorMode()) {
                                throw FError.New("colorspace wasn't specified for color mode");
                            }
                            handler.setColor16(color);
                        }
                    }
                }
            }
        }
        switch (ch) {
            /* cursor */
            case 'H': { handler.setLocation(0, 0); } break;
            `[H`          -> x = 0; y = 0;
            `[{r};{c}H` ||
            `[{r};{c}f`   -> x = c - 1; y = r -1; /* column/row notaion is not convenient */
            `[{n}A`       -> y -= n;
            `[{n}B`       -> y += n;
            `[{n}C`       -> x += n;
            `[{n}D`       -> x -= n;
            `[{n}E`       -> x = 0; y += n;
            `[{n}F`       -> x = 0; y -= n;
            `[{n}G`       -> x = n;
            `[6n`         -> /* request cursor position, doesn't affect buffer */
            `[s`          -> sco = pos;
            `[u`          -> pos = sco;
            /* erase */
            `[J`          -> handler.eraseCursorToEOS();
            `[0J`         -> /* erase from cursor until end of screen                 */
            `[1J`         -> /* erase from cursor to beginning of screen              */
            `[2J`         -> /* erase entire screen                                   */
            `[3J`         -> /* erase saved lines (does nothing, we don't store them) */
            `[K`          -> handler.eraseCursorToEOL();
            `[0K`         -> /* erase from cursor to end of line                      */
            `[1K`         -> /* erase start of line to the cursor                     */
            `[2K`         -> /* erase the entire line                                 */
            /* cursor shape */
            `[0q`         -> /* changes cursor shape to steady block       */
            `[1q`         -> /* changes cursor shape to steady block also  */
            `[2q`         -> /* changes cursor shape to blinking block     */
            `[3q`         -> /* changes cursor shape to steady underline   */
            `[4q`         -> /* changes cursor shape to blinking underline */
            `[5q`         -> /* changes cursor shape to steady bar         */
            `[6q`         -> /* changes cursor shape to blinking bar       */
            /* color/style */
            `[#;...;#m`   -> /* color/style seq, doesn't affect buffer */
            `[0m`         -> /* reset all modes (styles and colors)    */
            `[1m`         -> /* set bold mode.                         */
            `[2m`         -> /* set dim/faint mode.                    */
            `[3m`         -> /* set italic mode.                       */
            `[4m`         -> /* set underline mode.                    */
            `[5m`         -> /* set blinking mode                      */
            `[7m`         -> /* set inverse/reverse mode               */
            `[8m`         -> /* set hidden/invisible mode              */
            `[9m`         -> /* set strikethrough mode.                */
            `[38;5;{ID}m` -> /* Set foreground color. (256 table)      */
            `[48;5;{ID}m` -> /* Set background color. (256 table)      */
            `[38;2;{ID}m` -> /* Set foreground color. (RGB table)      */
            `[48;2;{ID}m` -> /* Set background color. (RGB table)      */
            `[{c}m`       -> /* c should be in range of colors, then it sets specified color */
            /* screen */
            `[={value}h`  -> /* Changes the screen width or type to the mode specified by value. */
            `[=0h`        -> /* 40 x 25 monochrome (text)                                        */
            `[=1h`        -> /* 40 x 25 color (text)                                             */
            `[=2h`        -> /* 80 x 25 monochrome (text)                                        */
            `[=3h`        -> /* 80 x 25 color (text)                                             */
            `[=4h`        -> /* 320 x 200 4-color (graphics)                                     */
            `[=5h`        -> /* 320 x 200 monochrome (graphics)                                  */
            `[=6h`        -> /* 640 x 200 monochrome (graphics)                                  */
            `[=7h`        -> /* Enables line wrapping                                            */
            `[=13h`       -> /* 320 x 200 color (graphics)                                       */
            `[=14h`       -> /* 640 x 200 color (16-color graphics)                              */
            `[=15h`       -> /* 640 x 350 monochrome (2-color graphics)                          */
            `[=16h`       -> /* 640 x 350 color (16-color graphics)                              */
            `[=17h`       -> /* 640 x 480 monochrome (2-color graphics)                          */
            `[=18h`       -> /* 640 x 480 color (16-color graphics)                              */
            `[=19h`       -> /* 320 x 200 color (256-color graphics)                             */
            `[={value}l`  -> /* Resets mode specified by `[={value}h                             */
            /* common private modes */
            `[?25l`       -> /* make cursor invisible */
            `[?25h`       -> /* make cursor visible   */
            `[?47l`       -> /* restore screen        */
            `[?47h`       -> /* save screen           */
            /* other */
            `[{code};{string};{...}p` -> /* remaps key to specified string, for more information check https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797#keyboard-strings */
        }
    }
}

// handleESCKey() {
//     /* cursor */
//     `[H`          -> x = 0; y = 0;
//     `[{r};{c}H` ||
//     `[{r};{c}f`   -> x = c - 1; y = r -1; /* column/row notaion is not convenient */
//     `[{n}A`       -> y -= n;
//     `[{n}B`       -> y += n;
//     `[{n}C`       -> x += n;
//     `[{n}D`       -> x -= n;
//     `[{n}E`       -> x = 0; y += n;
//     `[{n}F`       -> x = 0; y -= n;
//     `[{n}G`       -> x = n;
//     `[6n`         -> /* request cursor position, doesn't affect buffer */
//     `M`           -> if (y > 0) y -= 1; else /* scrolling */
//     `7`           -> dec = pos;
//     `8`           -> pos = dec;
//     `[s`          -> sco = pos;
//     `[u`          -> pos = sco;
//     /* erase */
//     `c`           -> /* clear entire screen (used to)                         */
//     `[J` ||
//     `[0J`         -> /* erase from cursor until end of screen                 */
//     `[1J`         -> /* erase from cursor to beginning of screen              */
//     `[2J`         -> /* erase entire screen                                   */
//     `[3J`         -> /* erase saved lines (does nothing, we don't store them) */
//     `[K` ||
//     `[0K`         -> /* erase from cursor to end of line                      */
//     `[1K`         -> /* erase start of line to the cursor                     */
//     `[2K`         -> /* erase the entire line                                 */
//     /* cursor shape */
//     `[0q`         -> /* changes cursor shape to steady block       */
//     `[1q`         -> /* changes cursor shape to steady block also  */
//     `[2q`         -> /* changes cursor shape to blinking block     */
//     `[3q`         -> /* changes cursor shape to steady underline   */
//     `[4q`         -> /* changes cursor shape to blinking underline */
//     `[5q`         -> /* changes cursor shape to steady bar         */
//     `[6q`         -> /* changes cursor shape to blinking bar       */
//     /* color/style */
//     `[#;...;#m`   -> /* color/style seq, doesn't affect buffer */
//     `[0m`         -> /* reset all modes (styles and colors)    */
//     `[1m`         -> /* set bold mode.                         */
//     `[2m`         -> /* set dim/faint mode.                    */
//     `[3m`         -> /* set italic mode.                       */
//     `[4m`         -> /* set underline mode.                    */
//     `[5m`         -> /* set blinking mode                      */
//     `[7m`         -> /* set inverse/reverse mode               */
//     `[8m`         -> /* set hidden/invisible mode              */
//     `[9m`         -> /* set strikethrough mode.                */
//     `[38;5;{ID}m` -> /* Set foreground color. (256 table)      */
//     `[48;5;{ID}m` -> /* Set background color. (256 table)      */
//     `[38;2;{ID}m` -> /* Set foreground color. (RGB table)      */
//     `[48;2;{ID}m` -> /* Set background color. (RGB table)      */
//     `[{c}m`       -> /* c should be in range of colors, then it sets specified color */
//     /* screen */
//     `[={value}h`  -> /* Changes the screen width or type to the mode specified by value. */
//     `[=0h`        -> /* 40 x 25 monochrome (text)                                        */
//     `[=1h`        -> /* 40 x 25 color (text)                                             */
//     `[=2h`        -> /* 80 x 25 monochrome (text)                                        */
//     `[=3h`        -> /* 80 x 25 color (text)                                             */
//     `[=4h`        -> /* 320 x 200 4-color (graphics)                                     */
//     `[=5h`        -> /* 320 x 200 monochrome (graphics)                                  */
//     `[=6h`        -> /* 640 x 200 monochrome (graphics)                                  */
//     `[=7h`        -> /* Enables line wrapping                                            */
//     `[=13h`       -> /* 320 x 200 color (graphics)                                       */
//     `[=14h`       -> /* 640 x 200 color (16-color graphics)                              */
//     `[=15h`       -> /* 640 x 350 monochrome (2-color graphics)                          */
//     `[=16h`       -> /* 640 x 350 color (16-color graphics)                              */
//     `[=17h`       -> /* 640 x 480 monochrome (2-color graphics)                          */
//     `[=18h`       -> /* 640 x 480 color (16-color graphics)                              */
//     `[=19h`       -> /* 320 x 200 color (256-color graphics)                             */
//     `[={value}l`  -> /* Resets mode specified by `[={value}h                             */
//     /* common private modes */
//     `[?25l`       -> /* make cursor invisible */
//     `[?25h`       -> /* make cursor visible   */
//     `[?47l`       -> /* restore screen        */
//     `[?47h`       -> /* save screen           */
//     /* other */
//     `(0`          -> /* line drawing mode (enable)*/
//     `(B`          -> /* line drawing mode (disable)*/
//     `[{code};{string};{...}p` -> /* remaps key to specified string, for more information check https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797#keyboard-strings */
// }

