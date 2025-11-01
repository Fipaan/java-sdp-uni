package fipaan.com.print;

public class Debug implements IPrinter<Debug> {
    public void breakpoint() {
        Thread t = Thread.currentThread();
        printfn("Breakpoint hit in %s", t.getName());
    }
    
    private String prefix;
    public Debug(String prefix) { this.prefix = prefix; }
    public Debug() { this("DEBUG:"); }
    
    public String getPrefix()        { return prefix; }
    public Debug setPrefix(String p) { prefix = p; return this; }
}
