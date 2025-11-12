package fipaan.com.string;

import fipaan.com.printf.Sscanf;

public class StringCursor {
    public String str;
    public int i = 0;
    public int length;
    
    public StringCursor(String s, int index, int len) { str = s; i = index; length = len; }
    public StringCursor(String s, int len)            { this(s,      0,    len       );   }
    public StringCursor(String s)                     { this(s,      0,    s.length());   }
    public StringCursor(StringCursor sc)              { this(sc.str, sc.i, sc.length );   }
    
    public boolean isExhausted() { return i >= length; }
    public boolean startsWith(String prefix) {
        return str.regionMatches(i, prefix, 0, prefix.length());
    }
    public char charAt(int index)      { return str.charAt(index);      }
    public char charAt()               { return str.charAt(i);          }
    public int  codePointAt(int index) { return str.codePointAt(index); }
    public int  codePointAt()          { return str.codePointAt(i);     }
    public String toI(int start) {
        return substring(start, i);
    }
    public boolean expectCode(int code) {
        if (isExhausted()) return false;
        if (codePointAt() != code) return false;
        i += 1;
        return true;
    }
    public String substring() {
        if (i > length) return null;
        return str.substring(i);
    }
    public String substring(int start) {
        if (start < 0)      return null;
        if (start > length) return null;
        return str.substring(start, length);
    }
    public String substring(int start, int end) {
        if (start > end) return null;
        if (end > length) return null;
        if (start < 0)    return null;
        return str.substring(start, end);
    }
    public Integer getInt() {
        int i = this.i;
        Integer res = Sscanf.parseInt(this);
        if (res == null) this.i = i;
        return res;
    }
    public Float getFloat() {
        int i = this.i;
        Float res = Sscanf.parseFloat(this);
        if (res == null) this.i = i;
        return res;
    }
    public int getCodeIAdvance() {
        int code = codePointAt();
        i += 1;
        return code;
    }
    @Override public String toString() { return substring(); }
}
