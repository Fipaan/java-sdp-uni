package fipaan.com.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import fipaan.com.has.HasLocation;
import fipaan.com.has.HasSize;

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

    @Override public int getX()      { return x;      }
    @Override public int getY()      { return y;      }
    @Override public int getWidth()  { return width;  }
    @Override public int getHeight() { return height; }

    @Override public FRectangle setX(int x)      { this.x = x; return this; }
    @Override public FRectangle setY(int y)      { this.y = y; return this; }
    @Override public FRectangle setWidth(int w)  { width  = w; return this; }
    @Override public FRectangle setHeight(int h) { height = h; return this; }

    @Override public String toString() {
        return String.format("fipaan.FRectangle{x: %d, y: %d, width: %d, height: %d}", x, y, width, height);
    }
}
