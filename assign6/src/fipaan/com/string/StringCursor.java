package fipaan.com.string;

public class StringCursor {
    public String str;
    public int i = 0;
    public int length;
    public StringCursor(String s) { str = s; length = str.length(); }
    public boolean startsWith(String prefix) {
        return str.regionMatches(i, prefix, 0, prefix.length());
    }
    public char charAt(int index)      { return str.charAt(index);      }
    public char charAt()               { return str.charAt(i);          }
    public int  codepointAt(int index) { return str.codepointAt(index); }
    public int  codepointAt()          { return str.codepointAt(i);     }
    @Override
    public String toString() {
        return str.substring(i);
    }
}
