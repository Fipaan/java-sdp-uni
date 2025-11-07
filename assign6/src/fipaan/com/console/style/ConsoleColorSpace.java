package fipaan.com.console.style;

import fipaan.com.errors.FError;
import fipaan.com.console.Console;
import fipaan.com.has.HasCode;

public enum ConsoleColorSpace implements HasCode<ConsoleColorSpace> {
    Color256(2),
    RGB(5);

    public final int value;
    private ConsoleColorSpace(int v) { value = v; }

    public String valueString() { return String.valueOf(value); }
    @Override public String toString() { return String.format("fipaan.ConsoleColorSpace{code: %d}", value); }
    @Override public int getCode() { return value; }
    @Override public ConsoleColorSpace setCode(int code) { throw FError.New("Can't assign code to ConsoleColorSpace"); }

    public static ConsoleColorSpace getByCode(int code) {
        return Console.getByCode(ConsoleColorSpace.class, code);
    }
}
