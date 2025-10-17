package lfs.gui.has;

import lfs.print.Debug;
import java.awt.Point;
import java.awt.Dimension;

public interface HasSize<T extends HasSize<T>> {
    int getWidth();
    T setWidth(int w);
    int getHeight();
    T setHeight(int h);
   
    default T setWidth(float w)   { return setWidth((int)w);  }
    default T setHeight(float h)  { return setHeight((int)h); }
    default T setWidth(double w)  { return setWidth((int)w);  }
    default T setHeight(double h) { return setHeight((int)h); }

    default Dimension getSize() { return new Dimension(getWidth(), getHeight()); }
    default T setSize(Dimension other) { return setWidth(other.width).setHeight(other.height); }

    default T setSize(int w, int h) { return setWidth(w).setHeight(h); }
    default T setSize(Point other) { return setWidth(other.x).setHeight(other.y); }
    default T setSize(HasSize<?> other) { return setWidth(other.getWidth()).setHeight(other.getHeight()); }
    default double minDimension() { return Math.min(getWidth(), getHeight()); }
    default double maxDimension() { return Math.max(getWidth(), getHeight()); }
    
    default T scaleW(double kw) { return setWidth (getWidth()  * kw); }
    default T scaleH(double kh) { return setHeight(getHeight() * kh); }
    default T scale (double kw, double kh) { return scaleW(kw).scaleH(kh); }
    default T scale (double k) { return scaleW(k).scaleH(k); }

    default T addW(double w) { return setWidth (getWidth()  + w); }
    default T addH(double h) { return setHeight(getHeight() + h); }
    default T add (double w, double h) { return addW(w).addH(h); }
    default T add (double s) { return addW(s).addH(s); }

    default T subW(double w) { return setWidth (getWidth()  - w); }
    default T subH(double h) { return setHeight(getHeight() - h); }
    default T sub (double w, double h) { return subW(w).subH(h); }
    default T sub (double s) { return subW(s).subH(s); }
}
