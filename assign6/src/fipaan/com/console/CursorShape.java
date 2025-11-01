package fipaan.com.console;

public enum CursorShape {
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

    public static CursorShape getByCode(int code) {
        switch (code) {
            case SteadyBlock.value:       return SteadyBlock;
            case SteadyBlockAlt.value:    return SteadyBlockAlt;
            case BlinkingBlock.value:     return BlinkingBlock;
            case SteadyUnderline.value:   return SteadyUnderline;
            case BlinkingUnderline.value: return BlinkingUnderline;
            case SteadyBar.value:         return SteadyBar;
            case BlinkingBar.value:       return BlinkingBar;
        }
        return null;
    }
}
