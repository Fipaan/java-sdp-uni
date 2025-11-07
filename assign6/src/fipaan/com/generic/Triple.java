package fipaan.com.generic;

public class Triple<X, Y, Z> {
    public X first;
    public Y second;
    public Z third;
    public Triple(X v1, Y v2, Z v3) { first = v1; second = v2; third = v3; }
    public Triple(Triple<X, Y, Z> trio) { this(trio.first, trio.second, trio.third); }
    public Triple() { this(null, null, null); }

    @Override public String toString() {
        String f = StringUtils.saveToString(first);
        String s = StringUtils.saveToString(second);
        String t = StringUtils.saveToString(third);
        return String.format("Triple{f: %s, s: %s, t: %s}", f, s, t);
    }
}
