import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringUtils {
    public static void printf(String fmt, Object... args) {
        System.out.print(String.format(fmt, args));
    }
    public static void printfn(String fmt, Object... args) {
        System.out.println(String.format(fmt, args));
    }
    public static String bToString(boolean b) { return b ? "true" : "false"; }
    public static String expectStartsAndExtract(String src, String prefix) {
        if (src == null) return src;
        if (!src.startsWith(prefix)) return null;
        return src.substring(prefix.length());
    }
    public static String expectEndsAndExtract(String src, String suffix) {
        if (src == null || suffix.length() == 0) return src;
        if (!src.endsWith(suffix)) return null;
        return src.substring(0, src.length() - suffix.length());
    }
    public static String expectWrapperAndExtract(String src, String prefix, String suffix) {
        return expectEndsAndExtract(expectStartsAndExtract(src, prefix), suffix);
    }
    public static String extractContainer(String src, String prefix, String suffix) {
        int iPrefix = src.indexOf(prefix);
        if (iPrefix == -1) return null;
        iPrefix += prefix.length();
        int iSuffix = src.indexOf(suffix, iPrefix);
        if (iSuffix == -1) return null;
        return src.substring(iPrefix, iSuffix);
    }
    public static int endOfContainer(String src, String prefix, String suffix) {
        if (!src.startsWith(prefix)) return -1;
        String container = extractContainer(src, prefix, suffix);
        if (container == null) return -1;
        return prefix.length() + container.length() + suffix.length();
    }
    public static final String DATE_PATTERN = "dd MMMM yyyy";
    protected static final DateTimeFormatter DATE_PATTERN_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN, Locale.ENGLISH);
    public static String dateToString(LocalDateTime date) {
        return date.format(DATE_PATTERN_FORMATTER);
    }
    public static LocalDateTime dateFromString(String str) {
        return LocalDate.parse(str, DATE_PATTERN_FORMATTER).atStartOfDay();
    }
}
