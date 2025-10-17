package lfs.gui.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import lfs.gui.has.HasLocation;
import lfs.gui.has.HasSize;

public class FDimension implements HasLocation<FDimension>, HasSize<FDimension> {
    protected Dimension size;
    public FDimension()             { size = new Dimension();         }
    public FDimension(Dimension s)  { size = new Dimension(s);        }
    public FDimension(Point p)      { size = new Dimension(p.x, p.y); }
    public FDimension(int w, int h) { size = new Dimension(w, h);     }

    public int getWidth()  { return size.width;  }
    public int getHeight() { return size.height; }
    public int getX()      { return size.width;  }
    public int getY()      { return size.height; }

    public FDimension setWidth(int w)  { size.width  = w; return this; }
    public FDimension setHeight(int h) { size.height = h; return this; }
    public FDimension setX(int x)      { size.width  = x; return this; }
    public FDimension setY(int y)      { size.height = y; return this; }
}
