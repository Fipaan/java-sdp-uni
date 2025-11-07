package fipaan.com.console.style;

import fipaan.com.errors.FError;
import fipaan.com.console.Console;
import fipaan.com.has.HasCode;

public enum ConsoleColorMode implements HasCode<ConsoleColorMode> {
    ForeTrue16(38),
    BackTrue16(48);

    public final int value;
    private ConsoleColorMode(int v) { value = v; }

    public String valueString() { return String.valueOf(value); }
    @Override public String toString() { return String.format("fipaan.ConsoleColorMode{code: %d}", value); }
    @Override public int getCode() { return value; }
    @Override public ConsoleColorMode setCode(int code) { throw FError.New("Can't assign code to ConsoleColorMode"); }

    public static ConsoleColorMode getByCode(int code) {
        return Console.getByCode(ConsoleColorMode.class, code);
    }

    public boolean isForeground() {
        return this == ForeTrue16;
    }
    public boolean isBackground() {
        return this == BackTrue16;
    }
}
