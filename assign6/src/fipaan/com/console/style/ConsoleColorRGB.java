package fipaan.com.console.style;

import fipaan.com.errors.FThrow;

public class ConsoleColorRGB {
    public ConsoleColorMode mode;
    public ConsoleColorSpace getSpace() { return ConsoleColorSpace.RGB; }
    
    private int r, g, b;
    public int getR() { return r; }
    public int getG() { return g; }
    public int getB() { return b; }
    public ConsoleColorRGB setR(int r) { verifyRange("red",   r); this.r = r; return this; }
    public ConsoleColorRGB setG(int g) { verifyRange("green", g); this.g = g; return this; }
    public ConsoleColorRGB setB(int b) { verifyRange("blue",  b); this.b = b; return this; }
    public String valueString() { return String.format("%d;%d;%d;%d;%d", mode.value, getSpace().value, r, g, b); }
    public void verifyRange(String target, int val) { if (val < 0 || val > 255) FThrow.New("Invalid %s for True 16 (%d)", target, val); }

    public ConsoleColorRGB(ConsoleColorMode colorMode, int r, int g, int b) { setR(r).setG(g).setB(b); mode = colorMode; }

    @Override public String toString() {
        return String.format("fipaan.ConsoleColorRGB{mode: %s, rgb: #%02X%02X%02X}", mode.toString(), r, g, b);
    }
}
