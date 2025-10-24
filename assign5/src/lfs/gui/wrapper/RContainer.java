package lfs.gui.wrapper;

import lfs.gui.has.HasRect;
import lfs.gui.has.HasContainer;
import lfs.errors.FError;
import java.awt.*;

public class RContainer<Cont extends Container> implements HasContainer<RContainer<Cont>, Cont>, HasRect<RContainer<Cont>> {
    public Cont obj;
    public FRectangle rect;
    public RContainer()                      { obj = null; rect = new FRectangle(); }
    public RContainer(Cont o)                   { obj = o;    rect = new FRectangle(); }
    public RContainer(Cont o, FRectangle frect) { obj = o;    rect = frect;            }

    public RContainer<Cont> resetBounds() { obj.setBounds(getRect()); return this; }

    // interfaces
    public Cont getContainer() { return obj; }
    public RContainer setContainer(Cont container) { obj = container; return this; }
    
    public int getX()      { return rect.getX();      }
    public int getY()      { return rect.getY();      }
    public int getWidth()  { return rect.getWidth();  }
    public int getHeight() { return rect.getHeight(); }

    public RContainer setX(int x)      { rect.setX(x);      return this; }
    public RContainer setY(int y)      { rect.setY(y);      return this; }
    public RContainer setWidth(int w)  { rect.setWidth(w);  return this; }
    public RContainer setHeight(int h) { rect.setHeight(h); return this; }
}
