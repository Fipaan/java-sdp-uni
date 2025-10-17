package lfs.print;

public interface IFakePrinter {
    /* Signatures:
    static String getPrefix();
    static void setPrefix(String p);

    static void printf(String fmt, Object... args);

    static void printf(Object obj);
    static void printfn(Object obj);
    static void printfn(String fmt, Object... args);
    */

    /* Drop-in implementation:
    protected static String prefix = "";
    
    public static String getPrefix()       { return prefix; }
    public static void setPrefix(String p) { prefix = p;    }
    public static void printf(String prefix, String fmt, Object... args) {
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
     */
}
