package fipaan.com.generic;

import fipaan.com.utils.StringUtils;

public class Pair<U, V> {
    public U first;
    public V second;
    public Pair(U f, V s) { first = f; second = s; }
    public Pair(Pair<U, V> pair) { this(pair.first, pair.second); }
    public Pair() { this(null, null); }

    @Override public String toString() {
        String f = StringUtils.saveToString(first);
        String s = StringUtils.saveToString(second);
        return String.format("Pair{f: %s, s: %s}", f, s);
    }
}
