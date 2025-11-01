package fipaan.com.geom.has;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;

public interface HasSize<Self extends HasSize<Self>> {
    int getWidth();
    Self setWidth(int w);
    int getHeight();
    Self setHeight(int h);
   
    default Self setWidth(float w)   { return setWidth((int)w);  }
    default Self setHeight(float h)  { return setHeight((int)h); }
    default Self setWidth(double w)  { return setWidth((int)w);  }
    default Self setHeight(double h) { return setHeight((int)h); }

    default Dimension getSize() { return new Dimension(getWidth(), getHeight()); }
    default Self setSize(Dimension other) { return setWidth(other.width).setHeight(other.height); }

    default Self setSize(int w, int h)     { return setWidth(w).setHeight(h); }
    default Self setSize(Point other)      { return setWidth(other.x).setHeight(other.y); }
    default Self setSize(Rectangle other)  { return setWidth(other.width).setHeight(other.height); }
    default Self setSize(HasSize<?> other) { return setWidth(other.getWidth()).setHeight(other.getHeight()); }
    
    default boolean eqSize(int w, int h)     { return getWidth() == w                && getHeight() == h;                 }
    default boolean eqSize(Point other)      { return getWidth() == other.x          && getHeight() == other.y;           }
    default boolean eqSize(Rectangle other)  { return getWidth() == other.width      && getHeight() == other.height;      }
    default boolean eqSize(HasSize<?> other) { return getWidth() == other.getWidth() && getHeight() == other.getHeight(); }
    
    default double minDimension() { return Math.min(getWidth(), getHeight()); }
    default double maxDimension() { return Math.max(getWidth(), getHeight()); }
    
    default Self scaleW(double kw) { return setWidth (getWidth()  * kw); }
    default Self scaleH(double kh) { return setHeight(getHeight() * kh); }
    default Self scale (double kw, double kh) { return scaleW(kw).scaleH(kh); }
    default Self scale (double k) { return scaleW(k).scaleH(k); }

    default Self addW(double w) { return setWidth (getWidth()  + w); }
    default Self addH(double h) { return setHeight(getHeight() + h); }
    default Self add (double w, double h) { return addW(w).addH(h); }
    default Self add (double s) { return addW(s).addH(s); }

    default Self subW(double w) { return setWidth (getWidth()  - w); }
    default Self subH(double h) { return setHeight(getHeight() - h); }
    default Self sub (double w, double h) { return subW(w).subH(h); }
    default Self sub (double s) { return subW(s).subH(s); }
}
