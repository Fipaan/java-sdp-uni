package fipaan.com.console;

import fipaan.com.errors.FError;
import fipaan.com.utils.ConsoleUtils;

public enum ConsoleColor16 {
    ForeTrue16(38), // NOTE: not a real color
    BackTrue16(48), // NOTE: not a real color

    ForeBlack(30),
    ForeRed(31),
    ForeGreen(32),
    ForeYellow(33),
    ForeBlue(34),
    ForeMagenta(35),
    ForeCyan(36),
    ForeWhite(37),
    ForeDefault(39),

    BackBlack(40),
    BackRed(41),
    BackGreen(42),
    BackYellow(43),
    BackBlue(44),
    BackMagenta(45),
    BackCyan(46),
    BackWhite(47),
    BackDefault(49),

    ForeBrightBlack(90),
    ForeBrightRed(91),
    ForeBrightGreen(92),
    ForeBrightYellow(93),
    ForeBrightBlue(94),
    ForeBrightMagenta(95),
    ForeBrightCyan(96),
    ForeBrightWhite(97),

    BackBrightBlack(100),
    BackBrightRed(101),
    BackBrightGreen(102),
    BackBrightYellow(103),
    BackBrightBlue(104),
    BackBrightMagenta(105),
    BackBrightCyan(106),
    BackBrightWhite(107);

    public static final String defaultBackAndFore = ConsoleStyle.pack(BackDefault, ForeDefault);

    public final int value;
    private ConsoleColor16(int v) { value = v; }
    public String valueString() { return String.valueOf(value); }

    public static ConsoleColor16 getByCode(int code) {
        switch (code) {
            case ForeTrue16.value:        return ForeTrue16;
            case BackTrue16.value:        return BackTrue16;
            case ForeBlack.value:         return ForeBlack;
            case ForeRed.value:           return ForeRed;
            case ForeGreen.value:         return ForeGreen;
            case ForeYellow.value:        return ForeYellow;
            case ForeBlue.value:          return ForeBlue;
            case ForeMagenta.value:       return ForeMagenta;
            case ForeCyan.value:          return ForeCyan;
            case ForeWhite.value:         return ForeWhite;
            case ForeDefault.value:       return ForeDefault;
            case BackBlack.value:         return BackBlack;
            case BackRed.value:           return BackRed;
            case BackGreen.value:         return BackGreen;
            case BackYellow.value:        return BackYellow;
            case BackBlue.value:          return BackBlue;
            case BackMagenta.value:       return BackMagenta;
            case BackCyan.value:          return BackCyan;
            case BackWhite.value:         return BackWhite;
            case BackDefault.value:       return BackDefault;
            case ForeBrightBlack.value:   return ForeBrightBlack;
            case ForeBrightRed.value:     return ForeBrightRed;
            case ForeBrightGreen.value:   return ForeBrightGreen;
            case ForeBrightYellow.value:  return ForeBrightYellow;
            case ForeBrightBlue.value:    return ForeBrightBlue;
            case ForeBrightMagenta.value: return ForeBrightMagenta;
            case ForeBrightCyan.value:    return ForeBrightCyan;
            case ForeBrightWhite.value:   return ForeBrightWhite;
            case BackBrightBlack.value:   return BackBrightBlack;
            case BackBrightRed.value:     return BackBrightRed;
            case BackBrightGreen.value:   return BackBrightGreen;
            case BackBrightYellow.value:  return BackBrightYellow;
            case BackBrightBlue.value:    return BackBrightBlue;
            case BackBrightMagenta.value: return BackBrightMagenta;
            case BackBrightCyan.value:    return BackBrightCyan;
            case BackBrightWhite.value:   return BackBrightWhite;
        }
        return null;
    }
    
    public String colored(String str) {
        if (isColorMode()) throw FError.New("Can't draw color mode");
        ConsoleColor16 defaultColor = isForeground() ? ForeDefault : BackDefault;
        return ConsoleStyle.wrap(valueString(), defaultColor.valueString(), str);
    }

    public boolean isColorMode() {
        switch (this) {
            case ForeTrue16:
            case BackTrue16: return true;
            default: return false;
        }
    }

    public boolean isBlack() {
        switch (this) {
            case ForeBlack:
            case ForeBrightBlack:
            case BackBlack:
            case BackBrightBlack: return true;
            default: return false;
        }
    }

    public boolean isWhite() {
        switch (this) {
            case ForeWhite:
            case ForeBrightWhite:
            case BackWhite:
            case BackBrightWhite: return true;
            default: return false;
        }
    }

    public boolean isDefault() {
        switch (this) {
            case ForeDefault:
            case BackDefault: return true;
            default: return false;
        }
    }

    public boolean isForeground() {
        switch (this) {
            case ForeBlack:
            case ForeRed:
            case ForeGreen:
            case ForeYellow:
            case ForeBlue:
            case ForeMagenta:
            case ForeCyan:
            case ForeWhite:
            case ForeDefault:
            case ForeBrightBlack:
            case ForeBrightRed:
            case ForeBrightGreen:
            case ForeBrightYellow:
            case ForeBrightBlue:
            case ForeBrightMagenta:
            case ForeBrightCyan:
            case ForeBrightWhite: return true;
            case BackBlack:
            case BackRed:
            case BackGreen:
            case BackYellow:
            case BackBlue:
            case BackMagenta:
            case BackCyan:
            case BackWhite:
            case BackDefault:
            case BackBrightBlack:
            case BackBrightRed:
            case BackBrightGreen:
            case BackBrightYellow:
            case BackBrightBlue:
            case BackBrightMagenta:
            case BackBrightCyan:
            case BackBrightWhite:
            case ForeTrue16:
            case BackTrue16: return false;
            default: throw FError.New("Unknown color (%d)", value);
        }
    }

    public boolean isBackground() {
        switch (this) {
            case ForeBlack:
            case ForeRed:
            case ForeGreen:
            case ForeYellow:
            case ForeBlue:
            case ForeMagenta:
            case ForeCyan:
            case ForeWhite:
            case ForeDefault:
            case ForeBrightBlack:
            case ForeBrightRed:
            case ForeBrightGreen:
            case ForeBrightYellow:
            case ForeBrightBlue:
            case ForeBrightMagenta:
            case ForeBrightCyan:
            case ForeBrightWhite: return false;
            case BackBlack:
            case BackRed:
            case BackGreen:
            case BackYellow:
            case BackBlue:
            case BackMagenta:
            case BackCyan:
            case BackWhite:
            case BackDefault:
            case BackBrightBlack:
            case BackBrightRed:
            case BackBrightGreen:
            case BackBrightYellow:
            case BackBrightBlue:
            case BackBrightMagenta:
            case BackBrightCyan:
            case BackBrightWhite: return true;
            case ForeTrue16:
            case BackTrue16: return false;
            default: throw FError.New("Unknown color (%d)", value);
        }
    }

    public boolean isBright() {
        switch (this) {
            case ForeBlack:
            case ForeRed:
            case ForeGreen:
            case ForeYellow:
            case ForeBlue:
            case ForeMagenta:
            case ForeCyan:
            case ForeWhite:
            case ForeDefault:
            case BackBlack:
            case BackRed:
            case BackGreen:
            case BackYellow:
            case BackBlue:
            case BackMagenta:
            case BackCyan:
            case BackWhite:
            case BackDefault: return false;
            case ForeBrightBlack:
            case ForeBrightRed:
            case ForeBrightGreen:
            case ForeBrightYellow:
            case ForeBrightBlue:
            case ForeBrightMagenta:
            case ForeBrightCyan:
            case ForeBrightWhite:
            case BackBrightBlack:
            case BackBrightRed:
            case BackBrightGreen:
            case BackBrightYellow:
            case BackBrightBlue:
            case BackBrightMagenta:
            case BackBrightCyan:
            case BackBrightWhite: return true;
            case ForeTrue16:
            case BackTrue16: return false;
            default: throw FError.New("Unknown color (%d)", value);
        }
    }
}
