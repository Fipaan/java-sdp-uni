package fipaan.com.console.style;

import fipaan.com.console.Console;
import fipaan.com.has.HasCode;
import fipaan.com.errors.FError;

public enum CursorShape implements HasCode<CursorShape> {
    SteadyBlock(0),
    SteadyBlockAlt(1),
    BlinkingBlock(2),
    SteadyUnderline(3),
    BlinkingUnderline(4),
    SteadyBar(5),
    BlinkingBar(6);

    public final int value;
    private CursorShape(int v) { value = v; }

    public String valueString() { return String.valueOf(value); }
    @Override public int getCode() { return value; }
    @Override public CursorShape setCode(int code) { throw FError.New("Can't assign code to CursorShape"); }

    public static CursorShape getByCode(int code) {
        return Console.getByCode(CursorShape.class, code);
    }
    
    @Override public String toString() { return String.format("fipaan.CursorShape{value: %s}", valueString()); }
}
