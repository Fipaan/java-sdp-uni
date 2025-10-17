package lfs.gui.wrapper;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import lfs.gui.has.HasRect;

public class FRectangle implements HasRect<FRectangle> {
    protected Rectangle rect;
    public FRectangle()                           { rect = new Rectangle();             }
    public FRectangle(FRectangle fr)              { rect = new Rectangle(fr.getRect()); }
    public FRectangle(Rectangle r)                { rect = new Rectangle(r);            }
    public FRectangle(Dimension s)                { rect = new Rectangle(s);            }
    public FRectangle(Point p)                    { rect = new Rectangle(p);            }
    public FRectangle(Point p, Dimension s)       { rect = new Rectangle(p, s);         }
    public FRectangle(int w, int h)               { rect = new Rectangle(w, h);         }
    public FRectangle(int x, int y, int w, int h) { rect = new Rectangle(x, y, w, h);   }

    public int getX()      { return rect.x;      }
    public int getY()      { return rect.y;      }
    public int getWidth()  { return rect.width;  }
    public int getHeight() { return rect.height; }

    public FRectangle setX(int x)      { rect.x      = x; return this; }
    public FRectangle setY(int y)      { rect.y      = y; return this; }
    public FRectangle setWidth(int w)  { rect.width  = w; return this; }
    public FRectangle setHeight(int h) { rect.height = h; return this; }

    public String toString() {
        return this.rect.toString();
    }
}
