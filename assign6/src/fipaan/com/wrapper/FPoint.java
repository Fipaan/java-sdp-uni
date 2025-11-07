package fipaan.com.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import fipaan.com.has.HasLocation;
import fipaan.com.has.HasSize;
import fipaan.com.has.HasRect;

public class FPoint implements HasRect<FPoint> {
    public int x, y;
    public FPoint(int x, int y)       { this.x = x; this.y = y;                  }
    public FPoint(int t)              { this(t, t);                              }
    public FPoint()                   { this(0, 0);                              }
    public FPoint(Point p)            { this(p.x, p.y);                          }
    public FPoint(HasLocation<?> loc) { this(loc.getX(), loc.getY());            }
    public FPoint(Dimension s)        { this(s.width, s.height);                 }
    public FPoint(HasSize<?>    size) { this(size.getWidth(), size.getHeight()); }

    @Override public int getWidth()  { return x; }
    @Override public int getHeight() { return y; }
    @Override public int getX()      { return x; }
    @Override public int getY()      { return y; }

    @Override public FPoint setWidth(int w)  {      x = w; return this; }
    @Override public FPoint setHeight(int h) {      y = h; return this; }
    @Override public FPoint setX(int x)      { this.x = x; return this; }
    @Override public FPoint setY(int y)      { this.y = y; return this; }

    @Override public String toString() {
        return String.format("fipaan.FPoint{x: %d, y: %d}", x, y);
    }
}
