package fipaan.com.console.style;

import fipaan.com.console.Console;
import fipaan.com.errors.FError;
import fipaan.com.utils.ConsoleUtils;
import fipaan.com.has.HasCode;

public enum ConsoleColor16 implements HasCode<ConsoleColor16> {
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
    @Override public String toString() { return String.format("fipaan.ConsoleColor16{code: %d}", value); }
    @Override public int getCode() { return value; }
    @Override public ConsoleColor16 setCode(int code) { throw FError.New("Can't assign code to ConsoleColor16"); }

    public static ConsoleColor16 getByCode(int code) {
        return Console.getByCode(ConsoleColor16.class, code);
    }
    
    public String colored(String str) {
        ConsoleColor16 defaultColor = isForeground() ? ForeDefault : BackDefault;
        return ConsoleStyle.wrap(valueString(), defaultColor.valueString(), str);
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
            case BackBrightWhite: return false;
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
            default: throw FError.New("Unknown color (%d)", value);
        }
    }
}
