package fipaan.com.console.style;

import fipaan.com.errors.FThrow;

public class ConsoleColor256 {
    public ConsoleColorMode mode;
    public ConsoleColorSpace getSpace() { return ConsoleColorSpace.Color256; }
    
    private int value;
    public int getValue() { return value; }
    public ConsoleColor256 setValue(int val) {
        if (val < 0 || val > 255) FThrow.New("Invalid value for True 16 (%d)", val);
        value = val;
        return this;
    }
    public String valueString() { return String.format("%d;%d;%d", mode.value, getSpace().value, value); }

    public ConsoleColor256(ConsoleColorMode colorMode, int val) {
        setValue(val);
        mode  = colorMode;
    }

    @Override public String toString() {
        return String.format("fipaan.ConsoleColor256{mode: %s, val: %d}", mode.toString(), value);
    }
    
    public static ConsoleColor256 c16to256(ConsoleColor16 color) {
        // "default" color doesn't make sense in context of 256-color space
        if (color.isDefault()) return null;
        boolean isForeground = color.isForeground();
        boolean isBright     = color.isBright();
        ConsoleColorMode mode;
        int id = color.value;
        if (isForeground) {
            mode = ConsoleColorMode.ForeTrue16; 
            if (isBright) {
                id -= ConsoleColor16.ForeBrightBlack.value - 8; // [90-97] -> [8-15]
            } else {
                id -= ConsoleColor16.ForeBlack.value - 0; // [30-37] -> [0-7]
            }
        } else {
            mode = ConsoleColorMode.BackTrue16;
            if (isBright) {
                id -= ConsoleColor16.BackBrightBlack.value - 8; // [100-107] -> [8-15]
            } else {
                id -= ConsoleColor16.BackBlack.value - 0; // [40-47] -> [0-7]
            }
        }
        return new ConsoleColor256(mode, id);
    }
    public static ConsoleColor256 getMono(ConsoleColorMode mode, int scale) {
        if (scale < 0 || scale >= 24) FThrow.New("invalid scale (%d)", (int)scale);
        return new ConsoleColor256(mode, 232 + scale);
    }
}
