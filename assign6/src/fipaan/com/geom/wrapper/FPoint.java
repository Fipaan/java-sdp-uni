package fipaan.com.geom.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import fipaan.com.geom.has.HasLocation;
import fipaan.com.geom.has.HasSize;

public class FPoint implements HasLocation<FPoint>, HasSize<FPoint> {
    public int x, y;
    public FPoint(int x, int y)       { this.x = x; this.y = y;                  }
    public FPoint(int t)              { this(t, t);                              }
    public FPoint()                   { this(0, 0);                              }
    public FPoint(Point p)            { this(p.x, p.y);                          }
    public FPoint(HasLocation<?> loc) { this(loc.getX(), loc.getY());            }
    public FPoint(Dimension s)        { this(s.width, s.height);                 }
    public FPoint(HasSize<?>    size) { this(size.getWidth(), size.getHeight()); }

    public int getWidth()  { return x; }
    public int getHeight() { return y; }
    public int getX()      { return x; }
    public int getY()      { return y; }

    public FPoint setWidth(int w)  { x = w; return this; }
    public FPoint setHeight(int h) { y = h; return this; }
    public FPoint setX(int x)      { x = x; return this; }
    public FPoint setY(int y)      { y = y; return this; }
}
