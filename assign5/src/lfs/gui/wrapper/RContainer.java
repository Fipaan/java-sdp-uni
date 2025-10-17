package lfs.gui.wrapper;

import lfs.gui.has.HasRect;
import lfs.gui.has.HasContainer;
import lfs.errors.FError;
import java.awt.*;

public class RContainer<T extends Container> implements HasContainer<RContainer<T>>, HasRect<RContainer<T>> {
    public T obj;
    public FRectangle rect;
    public RContainer()                      { obj = null; rect = new FRectangle(); }
    public RContainer(T o)                   { obj = o;    rect = new FRectangle(); }
    public RContainer(T o, FRectangle frect) { obj = o;    rect = frect;            }

    public Container getContainer() { return obj; }
    public RContainer setContainer(Container container) { throw FError.New("UB"); }

    public Component getComponent() { return obj; }
    public RContainer setComponent(Component component) { throw FError.New("UB"); }
    
    public int getX()      { return rect.getX();      }
    public int getY()      { return rect.getY();      }
    public int getWidth()  { return rect.getWidth();  }
    public int getHeight() { return rect.getHeight(); }

    public RContainer setX(int x)      { rect.setX(x);      return this; }
    public RContainer setY(int y)      { rect.setY(y);      return this; }
    public RContainer setWidth(int w)  { rect.setWidth(w);  return this; }
    public RContainer setHeight(int h) { rect.setHeight(h); return this; }
}
