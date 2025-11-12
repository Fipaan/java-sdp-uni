package fipaan.com.printf;

import fipaan.com.errors.*;
import fipaan.com.utils.*;
import fipaan.com.generic.*;
import fipaan.com.string.*;
import java.util.ArrayList;

public class Sscanf {
    private static ArrayList<FormattedObj> res = new ArrayList<>();
    private static StringBuilder sb = new StringBuilder();
    public static int lastReadLength = -1;
    public static String lastError = null;
    public static FormattedObj[] sscanf(String fmt, String str) {
        return sscanf(new StringCursor(fmt), new StringCursor(str));
    }
    private static <T> T returnError(String msg) { lastError = msg; return null; }
    public static FormattedObj[] sscanf(StringCursor fmt, StringCursor str) {
        lastReadLength = -1;
        lastError = null;
        res.clear();
        int initI = str.i;
        for (; fmt.i < fmt.length ;) {
            char chFmt = fmt.charAt();
            switch (chFmt) {
                case '%': {
                    fmt.i += 1;
                    FormattedObj obj = parseFormat(fmt, str);
                    if (obj == null) return null;
                    res.add(obj);
                } break;
                default: {
                    if (str.i >= str.length) {
                        return Sscanf.<FormattedObj[]>returnError("str exhausted before fmt");
                    }
                    char ch = str.charAt(str.i);
                    if (ch != chFmt) {
                        return Sscanf.<FormattedObj[]>returnError("str char does not correspond to fmt");
                    }
                    fmt.i += 1;
                    str.i += 1;
                }
            }
        }
        lastReadLength = str.i - initI;
        return ArrayUtils.extractArr(FormattedObj.class, res);
    }
    public static FormattedObj[] sscanfErr(String fmt, String str) {
        FormattedObj[] result = sscanf(fmt, str);
        if (result == null) throw FError.New(lastError);
        return result;
    }
    private static FormattedObj parseFormat(StringCursor fmt, StringCursor str) {
        switch (fmt.charAt()) {
            case 'd': fmt.i += 1; return parseIntWrap(str);
            case 'f': fmt.i += 1; return parseFloatWrap(str);
            case 's': fmt.i += 1; return parseString(fmt, str);
        }
        throw FError.New("unknown format: %s", fmt.toString());
    }
    private static FormattedObj parseIntWrap(StringCursor str) {
        Integer res = parseInt(str);
        if (res == null) return null;
        return new FormattedObj(res);
    }
    public static Integer parseInt(StringCursor str) {
        sb.setLength(0);
        if (str.i >= str.length) {
            return Sscanf.<Integer>returnError("expected int, got eos");
        }
        if (str.charAt() == '-') sb.append('-');
        if (str.i >= str.length) {
            return Sscanf.<Integer>returnError("expected int, got '-'");
        }
        while (Character.isDigit(str.charAt())) {
            sb.append(str.charAt());
            str.i += 1;
            if (str.i == str.length) break;
        }
        String num = new String(sb);
        if (sb.length() == 0 || (sb.length() == 1 && sb.charAt(0) == '-')) {
            return Sscanf.<Integer>returnError("expected int, got something else");
        }
        return Integer.valueOf(num);
    }
    private static FormattedObj parseFloatWrap(StringCursor str) {
        Float res = parseFloat(str);
        if (res == null) return null;
        return new FormattedObj(res);
    }
    public static Float parseFloat(StringCursor str) {
        sb.setLength(0);
        if (str.i == str.length) {
            return Sscanf.<Float>returnError("expected double, got eos");
        }
        if (str.charAt() == '-') sb.append('-');
        if (str.i == str.length) {
            return Sscanf.<Float>returnError("expected double, got '-'");
        }
        boolean floated = false;
        while (true) {
            char ch = str.charAt();
            if (Character.isDigit(ch)) {
                sb.append(str.charAt());
                str.i += 1;
            } else if (ch == '.') {
                if (floated) return null; // invalid float
                floated = true;
            }
            if (str.i == str.length) break;
        }
        if (sb.length() == 0 || (sb.length() == 1 && sb.charAt(0) == '-')) {
            return Sscanf.<Float>returnError("expected double, got something else");
        }
        String num = new String(sb);
        return Float.valueOf(num);
    }
    private static FormattedObj parseString(StringCursor fmt, StringCursor str) {
        if (fmt.i == fmt.length) return new FormattedObj(str.toString());
        char stopAt = fmt.charAt();
        sb.setLength(0);
        while (true) {
            if (str.i >= str.length) {
                return Sscanf.<FormattedObj>returnError("expected char after string, got nothing");
            }
            char ch = str.charAt();
            if (ch == stopAt) break;
            sb.append(ch);
            str.i += 1;
        }
        return new FormattedObj(new String(sb));
    }
}
