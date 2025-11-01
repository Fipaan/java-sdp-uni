package fipaan.com.print;

public class Info implements IPrinter<Info> {
    private String prefix;
    public Info(String prefix) { this.prefix = prefix; }
    public Info() { this("Info:"); }
    
    public String getPrefix()       { return prefix; }
    public Info setPrefix(String p) { prefix = p; return this; }
}
