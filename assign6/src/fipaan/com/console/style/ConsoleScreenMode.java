package fipaan.com.console.style;

import fipaan.com.console.Console;
import fipaan.com.errors.FError;
import fipaan.com.has.HasCode;

public enum ConsoleScreenMode implements HasCode<ConsoleScreenMode> {
    Mono_40x25Text(0),
    Color_40x25Text(1),
    Mono_80x25Text(2),
    Color_80x25Text(3),
    Color4_320x200Graphics(4),
    Mono_320x200Graphics(5),
    Mono_640x200Graphics(6),
    LineWrapping(7),
    Color_320x200Graphics(13),
    Color16_640x200Graphics(14),
    Mono2_640x350Graphics(15),
    Color16_640x350Graphics(16),
    Mono2_640x480Graphics(17),
    Color16_640x480Graphics(18),
    Color256_320x200Graphics(19);

    public final int value;
    private ConsoleScreenMode(int v) { value = v; }

    public String valueString() { return String.valueOf(value); }
    @Override public int getCode() { return value; }
    @Override public ConsoleScreenMode setCode(int code) { throw FError.New("Can't assign code to ConsoleScreenMode"); }

    public static ConsoleScreenMode getByCode(int code) {
        return Console.getByCode(ConsoleScreenMode.class, code);
    }

    @Override public String toString() { return String.format("fipaan.ConsoleScreenMode{code: %d}", value); }
}
