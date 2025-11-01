package fipaan.com.console;

public enum ConsoleKey {
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
    public String valueString() { return String.valueOf(value); }
    
    public static ConsoleKey getByCode(int code) {
        switch (code) {
            case Bell.value:        return Bell;
            case Backspace.value:   return Backspace;
            case HorTab.value:      return HorTab;
            case Newline.value:     return Newline;
            case VerTab.value:      return VerTab;
            case Formfeed.value:    return Formfeed;
            case CarriageRet.value: return CarriageRet;
            case ESC.value:         return ESC;
            case Delete.value:      return Delete;
        }
        return null;
    }
}
