package fipaan.com.console.style;

import fipaan.com.console.*;
import fipaan.com.utils.ConsoleUtils;
import fipaan.com.has.*;
import fipaan.com.errors.*;

public enum ConsoleStyle implements HasCode<ConsoleStyle> {
    ResetAll(0),
    SetBold(1),
    SetDim(2),
    ResetBoldAndDim(22),
    SetItalic(3),
    ResetItalic(23),
    SetUnderline(4),
    ResetUnderline(24),
    SetBlinking(5),
    ResetBlinking(25),
    SetInverse(7),
    ResetInverse(27),
    SetHidden(8),
    ResetHidden(28),
    SetStrikethrough(9),
    ResetStrikethrough(29),
    // aliases
    ResetBold(ResetBoldAndDim.value),
    ResetDim(ResetBoldAndDim.value),
    SetFaint(SetDim.value),
    ResetFaint(ResetBoldAndDim.value),
    SetReverse(SetInverse.value),
    ResetReverse(ResetInverse.value),
    SetInvisible(SetHidden.value),
    ResetInvisible(ResetHidden.value);

    public final int value;
    private ConsoleStyle(int v) { value = v; }

    public String valueString() { return String.valueOf(value); }
    @Override public int getCode() { return value; }
    @Override public ConsoleStyle setCode(int code) { throw FError.New("Can't assign code to ConsoleStyle"); }

    public static ConsoleStyle getByCode(int code) {
        return Console.getByCode(ConsoleStyle.class, code);
    }

    @Override public String toString() { return String.format("fipaan.ConsoleStyle{value: %s}", valueString()); }

    private static final String wrapFormat = ConsoleUtils.ESC + "[%sm" + "%s"
                                           + ConsoleUtils.ESC + "[%sm";
    public static String wrap(String in, String out, String fmt, Object... args) {
        return String.format(wrapFormat, in, String.format(fmt, args), out);
    }
    public static String wrap(String in, String out, Object obj) {
        return String.format(wrapFormat, in, obj.toString(), out);
    }
    public static String pack(Object... styles) {
        if (styles == null || styles.length == 0) return "";

        return java.util.Arrays.stream(styles)
            .map(style -> {
                if (style == null) return "null";
                if (style instanceof String) return (String) style;
                if (style instanceof ConsoleColor16) return ((ConsoleColor16) style).valueString();
                return style.toString();
            })
            .collect(java.util.stream.Collectors.joining(";"));
    }    
}
