package lfs.gui.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import lfs.gui.has.HasLocation;
import lfs.gui.has.HasSize;

public class FPoint implements HasLocation<FPoint>, HasSize<FPoint> {
    protected Point p;
    public FPoint()             { p = new Point();                  }
    public FPoint(Dimension s)  { p = new Point(s.width, s.height); }
    public FPoint(Point p)      { p = new Point(p);                 }
    public FPoint(int x, int y) { p = new Point(x, y);              }
    public FPoint(int t)        { p = new Point(t, t);              }

    public int getWidth()  { return p.x; }
    public int getHeight() { return p.y; }
    public int getX()      { return p.x; }
    public int getY()      { return p.y; }

    public FPoint setWidth(int w)  { p.x = w; return this; }
    public FPoint setHeight(int h) { p.y = h; return this; }
    public FPoint setX(int x)      { p.x = x; return this; }
    public FPoint setY(int y)      { p.y = y; return this; }
}
