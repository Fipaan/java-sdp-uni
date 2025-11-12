package fipaan.com.console;

import fipaan.com.errors.*;
import fipaan.com.has.*;
import fipaan.com.utils.StringUtils;

public enum ConsoleKey implements HasCode<ConsoleKey> {
    Bell(0x08),
    Backspace(0x08),
    HorTab(0x09),
    Newline(0x0A),
    VerTab(0x0B),
    Formfeed(0x0C),
    CarriageRet(0x0D),
    ESC(0x1B),
    Delete(0x7F);

    public final int value;
    private ConsoleKey(int v) { value = v; }

    public String valueString() { return StringUtils.CodepointToString(value); }
    @Override public int getCode() { return value; }
    @Override public ConsoleKey setCode(int code) { throw FError.New("Can't assign code to ConsoleKey"); }

    @Override public String toString() { return String.format("fipaan.ConsoleKey{code: %d}", value); }

    public static ConsoleKey getByCode(int code) {
        return Console.getByCode(ConsoleKey.class, code);
    }
}
