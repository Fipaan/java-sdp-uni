package fipaan.com.geom.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import fipaan.com.geom.has.HasLocation;
import fipaan.com.geom.has.HasSize;

public class FRectangle implements HasRect<FRectangle> {
    public int x, y, width, height;
    public FRectangle(int x, int y, int w, int h) {
        this.x      = x;
        this.y      = y;
        this.width  = w;
        this.height = h;
    }
    public FRectangle(int x, int y, int s)                      { this(x,        y,        s,               s);                }
    public FRectangle(int x, int y, Dimension s)                { this(x,        y,        s.width,         s.height);         }
    public FRectangle(int x, int y, HasSize<?> size)            { this(x,        y,        s.getWidth(),    s.getHeight());    }
    public FRectangle(int w, int h)                             { this(0,        0,        w,               h);                }
    public FRectangle(int s)                                    { this(0,        0,        s,               s);                }
    public FRectangle(Dimension s)                              { this(0,        0,        s.width,         s.height);         }
    public FRectangle(HasSize<?>    size)                       { this(0,        0,        size.getWidth(), size.getHeight()); }
    public FRectangle(Point p, int w, int h)                    { this(p.x,      p.y,      w,               h);                }
    public FRectangle(Point p, int s)                           { this(p.x,      p.y,      s,               s);                }
    public FRectangle(Point p, Dimension s)                     { this(p.x,      p.y,      s.width,         s.height);         }
    public FRectangle(Point p, HasSize<?> size)                 { this(p.x,      p.y,      s.getWidth(),    s.getHeight());    }
    public FRectangle(Point p)                                  { this(p.x,      p.y,      0,               0);                }
    public FRectangle(HasLocation<?> loc, int w, int h)         { this(p.getX(), p.getY(), w,               h);                }
    public FRectangle(HasLocation<?> loc, int s)                { this(p.getX(), p.getY(), s,               s);                }
    public FRectangle(HasLocation<?> loc, Dimension s)          { this(p.getX(), p.getY(), s.width,         s.height);         }
    public FRectangle(HasLocation<?> loc, HasSize<?> size)      { this(p.getX(), p.getY(), s.getWidth(),    s.getHeight());    }
    public FRectangle(HasLocation<?> loc)                       { this(p.getX(), p.getY(), 0,               0);                }
    public FRectangle()                                         { this(0,        0,        0,               0);                }

    public int getX()      { return x;      }
    public int getY()      { return y;      }
    public int getWidth()  { return width;  }
    public int getHeight() { return height; }

    public FRectangle setX(int x)      { this.x = x; return this; }
    public FRectangle setY(int y)      { this.y = y; return this; }
    public FRectangle setWidth(int w)  { width  = w; return this; }
    public FRectangle setHeight(int h) { height = h; return this; }

    public String toString() {
        return String.format("{x: %d, y: %d, w: %d, h: %d}", x, y, width, height);
    }
}
