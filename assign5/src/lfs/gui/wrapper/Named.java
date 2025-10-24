package lfs.gui.wrapper;

public class Named<T> {
    public T obj;
    public String name;
    public Named(T obj, String name) { this.obj = obj; this.name = name; }
    public Named(String name) { this(null, name); }
}
