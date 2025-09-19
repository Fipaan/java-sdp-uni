public class Color {
    char r, g, b, a;
    Color(char r, char g, char b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }
    Color(char r, char g, char b, char a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    static char CheckComponent(int c) {
        if (c < 0x00 || c > 0xFF) throw new Error(String.format("Color component is out of range (%d)", c));
        return (char)c;
    }
    Color(int r, int g, int b, int a) {
        this.r = Color.CheckComponent(r);
        this.g = Color.CheckComponent(g);
        this.b = Color.CheckComponent(b);
        this.a = Color.CheckComponent(a);
    }
    @Override
    public String toString() {
        return String.format("#%02X%02X%02X%02X", (int)r, (int)g, (int)b, (int)a);
    }
    static Color WHITE() { return new Color(0xFF, 0xFF, 0xFF, 0xFF); }
    static Color BLACK() { return new Color(0x00, 0x00, 0x00, 0xFF); }
    static Color RED()   { return new Color(0xFF, 0x00, 0x00, 0xFF); }
    static Color GREEN() { return new Color(0x00, 0xFF, 0x00, 0xFF); }
}
