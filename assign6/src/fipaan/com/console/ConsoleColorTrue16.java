package fipaan.com.console;

public enum ConsoleColorTrue16 {
    public int value;
    ConsoleColorTrue16()

    public static String get16(ConsoleColor16 color) {
        boolean isDefault = color.isDefault();
        if (!isDefault) {
            boolean isForeground = color.isForeground();
            boolean isBright     = color.isBright();
            ConsoleColor16 mode;
            int id = color.value;
            if (isForeground) {
                mode = ConsoleColor16.ForeTrue16; 
                if (isBright) {
                    id -= ForeBrightBlack.value - 8; // [90-97] -> [8-15]
                } else {
                    id -= ForeBlack.value - 0; // [30-37] -> [0-7]
                }
            } else {
                mode = ConsoleColor16.BackTrue16;
                if (isBright) {
                    id -= BackBrightBlack.value - 8; // [100-107] -> [8-15]
                } else {
                    id -= BackBlack.value - 0; // [40-47] -> [0-7]
                }
            }
            return String.format("%d;5;%d", mode.value, id);
        } else if (color.isColorMode()) {
            throw FError.New("Invalid usage: expected color");
        } else {
            // "default" color doesn't make sense in context of 256-color space
            return String.format("%d", color.value);
        }
    }
    public static String getRGB6(ConsoleColor16 mode, byte r, byte g, byte b) {
        if (!mode.isColorMode()) throw FError.New("Expected color mode, got color");
        if (r < 0 || r >= 6) throw FError.New("invalid r (%d)", (int)r);
        if (g < 0 || g >= 6) throw FError.New("invalid g (%d)", (int)g);
        if (b < 0 || b >= 6) throw FError.New("invalid b (%d)", (int)b);
        return String.format("%d;5;%d", mode.value, 16 + 36 * r + 6 * g + b);
    }
    public static String getMono(ConsoleColor16 mode, byte scale) {
        if (!mode.isColorMode()) throw FError.New("Expected color mode, got color");
        if (scale < 0 || scale >= 24) throw FError.New("invalid scale (%d)", (int)scale);
        return String.format("%d;5;%d", mode, 232 + scale);
    }

    public static String getRGB(ConsoleColor16 mode, char r, char g, char b) {
        if (!mode.isColorMode()) throw FError.New("Expected color mode, got color");
        if (r < 0 || r > 255) throw FError.New("invalid r (%d)", (int)r);
        if (g < 0 || g > 255) throw FError.New("invalid g (%d)", (int)g);
        if (b < 0 || b > 255) throw FError.New("invalid b (%d)", (int)b);
        return String.format("%d;2;%d;%d;%d", mode.value, r, g, b);
    }
}
