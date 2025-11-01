package fipaan.com.console;

import fipaan.com.utils.ConsoleUtils;

public enum ConsoleStyle {
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
    ResetStrikethrough(29);
   
    // aliases
    ResetBold(ResetBoldAndDim),
    ResetDim(ResetBoldAndDim),
    SetFaint(SetDim),
    ResetFaint(ResetBoldAndDim),
    SetReverse(SetInverse),
    ResetReverse(ResetInverse),
    SetInvisible(SetHidden),
    ResetInvisible(ResetHidden);

    public int value;
    private ConsoleStyle(ConsoleStyle cs) { this = cs; }
    private ConsoleStyle(int value) { this.value = value; }
    
    public static ConsoleKey getByCode(int code) {
        switch (code) {
            case ResetAll.value:           return ResetAll;
            case SetBold.value:            return SetBold;
            case SetDim.value:             return SetDim;
            case ResetBoldAndDim.value:    return ResetBoldAndDim;
            case SetItalic.value:          return SetItalic;
            case ResetItalic.value:        return ResetItalic;
            case SetUnderline.value:       return SetUnderline;
            case ResetUnderline.value:     return ResetUnderline;
            case SetBlinking.value:        return SetBlinking;
            case ResetBlinking.value:      return ResetBlinking;
            case SetInverse.value:         return SetInverse;
            case ResetInverse.value:       return ResetInverse;
            case SetHidden.value:          return SetHidden;
            case ResetHidden.value:        return ResetHidden;
            case SetStrikethrough.value:   return SetStrikethrough;
            case ResetStrikethrough.value: return ResetStrikethrough;
        }
        return null;
    }

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
