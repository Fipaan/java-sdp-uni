package fipaan.com.printf;

import fipaan.com.errors.*;
import fipaan.com.utils.*;
import fipaan.com.generic.*;
import java.util.ArrayList;

public class Sscanf {
    private static ArrayList<FormattedObj> res = new ArrayList<>();
    private static StringBuilder sb = new StringBuilder();
    public static lastReadLength = -1;
    public static FormattedObj[] sscanf(String fmt, String str) {
        return sscanf(new StringCursor(fmt), new StringCursor(str));
    }
    public static FormattedObj[] sscanf(StringCursor fmt, StringCursor str) {
        lastReadLength = -1;
        res.clear();
        for (; fmt.i < fmt.length; ++fmt.i) {
            char chFmt = fmt.charAt();
            switch (chFmt) {
                case '%': {
                    fmt.i += 1;
                    Triple<FormattedObj, Integer, Integer> is = parseFormat(fmt, str);
                    if (is == null) return null;
                    throw FError.TODO();
                } /* break; */
                default: {
                    if (str.i >= str.length) return null; // str is empty, fmt is not
                    char ch = str.charAt(str.i);
                    if (ch != chFmt) return null; // incorrect
                }
            }
        }
        lastReadLength = str.i;
        return ArrayList.extractArr(res);
    }
    private static Triple<FormattedObj, Integer, Integer> parseFormat(StringCursor fmt, StringCursor str) {
        switch (fmt.charAt()) {
            case 'd': return parseInt(fmt, str);
            case 'f': return parseFloat(fmt, str);
            case 's': return parseString(fmt, str);
        }
        throw FError.New("unknown format: %s", fmt.toString());
    }
    private static Triple<FormattedObj, Integer, Integer> parseInt(StringCursor fmt, StringCursor str) {
        sb.setLength(0);
        for (int j = i; j < len; ++j) {
        }
        throw FError.TODO();
    }
    private static Triple<FormattedObj, Integer, Integer> parseFloat(StringCursor fmt, StringCursor str) {
        throw FError.TODO();
    }
    private static Triple<FormattedObj, Integer, Integer> parseString(StringCursor fmt, StringCursor str) {
        throw FError.TODO();
    }
}
