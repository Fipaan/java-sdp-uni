package fipaan.com.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import fipaan.com.has.HasLocation;
import fipaan.com.has.HasSize;
import fipaan.com.has.HasRect;

public class FDimension implements HasRect<FDimension> {
    public int width, height;
    public FDimension(int w, int h)       { width = w; height = h;                   }
    public FDimension(int s)              { this(s, s);                              }
    public FDimension()                   { this(0, 0);                              }
    public FDimension(Dimension s)        { this(s.width, s.height);                 }
    public FDimension(HasSize<?> size)    { this(size.getWidth(), size.getHeight()); }
    public FDimension(Point p)            { this(p.x, p.y);                          }
    public FDimension(HasLocation<?> loc) { this(loc.getX(), loc.getY());            }

    @Override public int getWidth()  { return width;  }
    @Override public int getHeight() { return height; }
    @Override public int getX()      { return width;  }
    @Override public int getY()      { return height; }

    @Override public FDimension setWidth(int w)  { width  = w; return this; }
    @Override public FDimension setHeight(int h) { height = h; return this; }
    @Override public FDimension setX(int x)      { width  = x; return this; }
    @Override public FDimension setY(int y)      { height = y; return this; }

    @Override public String toString() {
        return String.format("fipaan.FDimension{width: %d, height: %d}", width, height);
    }
}
