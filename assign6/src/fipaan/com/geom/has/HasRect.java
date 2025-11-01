package fipaan.com.geom.has;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;

public interface HasRect<Self extends HasRect<Self>> extends HasLocation<Self>, HasSize<Self> {
    // public int getWidth();
    // public int getHeight();
    // public int getX();
    // public int getY();
    // public Self setWidth(int w);
    // public Self setHeight(int h);
    // public Self setX(int x);
    // public Self setY(int y);

    default Rectangle getRect() { return new Rectangle(getX(), getY(), getWidth(), getHeight()); }
    default Self setRect(Rectangle other) { return setX(other.x).setY(other.y).setWidth(other.width).setHeight(other.height); }
    default Self setRect(int x, int y, int width, int height) { return setX(x).setY(y).setWidth(width).setHeight(height); }
    default Self setRect(HasRect<?> other) { return setRect(other.getRect()); } 
    
    default boolean eqRect(int x, int y, int w, int h) { return eqPos(x, y) && eqSize(w, h); }
    default boolean eqRect(Rectangle other)            { return eqPos(other) && eqSize(other); }
    default boolean eqRect(HasRect<?> other)           { return eqRect(other.getRect()); }
    
    default Self resetRectBySize(Dimension size) { setLocation(size).setSize(size); return (Self)this; }

    default Self centerX() { return setX(getX() - getWidth()  / 2); }
    default Self centerY() { return setY(getY() - getHeight() / 2); }
    default Self centerL() { return centerX().centerY(); }
    
    default Self scaleW(double kw, boolean reverse) {
        if (!reverse) return scaleW(kw);
        int oldWidth = getWidth();
        scaleW(kw);
        int newWidth = getWidth();
        return subX(newWidth - oldWidth);
    }
    default Self scaleH(double kh, boolean reverse){
        if (!reverse) return scaleH(kh);
        int oldHeight = getHeight();
        scaleH(kh);
        return subY(getHeight() - oldHeight);
    }

    default Self cutX(double bw) {
        double width = getWidth();
        if (2*bw > width) bw = width / 2;
        return addX(bw).subW(2*bw);
    }
    default Self cutY(double bh) {
        double height = getHeight();
        if (2*bh > height) bh = height / 2;
        return addY(bh).subH(2*bh);
    }
    default Self cut(double bw, double bh) { return cutX(bw).cutY(bh); }
    default Self cut (double b)  { return cutX(b).cutY(b); }

    default Self pwcutX(double pb) { return cutX(pb * getSize().width); }
    default Self pwcutY(double pb) { return cutY(pb * getSize().width); }
    default Self pwcut (double pb) { return cut (pb * getSize().width); }
    default Self pwcut(double pbw, double pbh) {
        double w = getSize().width;
        return cut(pbw * w, pbh * w);
    }
    
    default Self phcutX(double pb) { return cutX(pb * getSize().height); }
    default Self phcutY(double pb) { return cutY(pb * getSize().height); }
    default Self phcut (double pb) { return cut (pb * getSize().height); }
    default Self phcut(double pbw, double pbh) {
        double h = getSize().height;
        return cut(pbw * h, pbh * h);
    }
}
