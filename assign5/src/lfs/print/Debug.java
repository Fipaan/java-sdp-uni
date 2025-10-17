package lfs.print;

public class Debug implements IFakePrinter {
    public static void breakpoint() {
        Thread t = Thread.currentThread();
        Debug.printfn("Breakpoint hit in %s", t.getName());
    }
    
    protected static String prefix = "DEBUG:";
    
    public static String getPrefix()       { return prefix; }
    public static void setPrefix(String p) { prefix = p;    }
    public static void foo(String fmt, String bar, Object... args) {
        System.out.print(String.format(fmt, bar, (Object[])args));
    }
    public static void printf(String fmt, Object... args) {
        fmt = String.format(String.format("%%-%ds%%s", PrinterConfig.INDENT), prefix, fmt);
        System.out.print(String.format(fmt, (Object[])args));
    }
    public static void printf(Object obj) {
        printf("%s", obj.toString());
    }
    public static void printfn(Object obj) {
        printf("%s\n", obj.toString());
    }
    public static void printfn(String fmt, Object... args) {
        printf(fmt + "\n", (Object[])args);
    }
}
