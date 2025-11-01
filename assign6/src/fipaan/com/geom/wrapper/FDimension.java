package fipaan.com.geom.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import fipaan.com.geom.has.HasLocation;
import fipaan.com.geom.has.HasSize;

public class FDimension implements HasLocation<FDimension>, HasSize<FDimension> {
    public int width, height;
    public FDimension(int w, int h)       { width = w; height = h;                   }
    public FDimension(int s)              { this(s, s);                              }
    public FDimension()                   { this(0, 0);                              }
    public FDimension(Dimension s)        { this(s.width, s.height);                 }
    public FDimension(HasSize<?> size)    { this(size.getWidth(), size.getHeight()); }
    public FDimension(Point p)            { this(p.x, p.y);                          }
    public FDimension(HasLocation<?> loc) { this(loc.getX(), loc.getY());            }

    public int getWidth()  { return width;  }
    public int getHeight() { return height; }
    public int getX()      { return width;  }
    public int getY()      { return height; }

    public FDimension setWidth(int w)  { width  = w; return this; }
    public FDimension setHeight(int h) { height = h; return this; }
    public FDimension setX(int x)      { width  = x; return this; }
    public FDimension setY(int y)      { height = y; return this; }
}
