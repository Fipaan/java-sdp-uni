package lfs.gui.has;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import lfs.print.Debug;

public interface HasRect<T extends HasRect<T>> extends HasLocation<T>, HasSize<T> {
    // public int getWidth();
    // public int getHeight();
    // public int getX();
    // public int getY();
    // public T setWidth(int w);
    // public T setHeight(int h);
    // public T setX(int x);
    // public T setY(int y);

    default Rectangle getRect() { return new Rectangle(getX(), getY(), getWidth(), getHeight()); }
    default T setRect(Rectangle other) { return setX(other.x).setY(other.y).setWidth(other.width).setHeight(other.height); }
    default T setRect(int x, int y, int width, int height) { return setX(x).setY(y).setWidth(width).setHeight(height); }
    default T setRect(HasRect<?> other) { return setRect(other.getRect()); } 

    default T centerX() { return setX(getX() - getWidth()  / 2); }
    default T centerY() { return setY(getY() - getHeight() / 2); }
    default T centerL() { return centerX().centerY(); }
    
    default T scaleW(double kw, boolean reverse) {
        if (!reverse) return scaleW(kw);
        int oldWidth = getWidth();
        scaleW(kw);
        int newWidth = getWidth();
        return subX(newWidth - oldWidth);
    }
    default T scaleH(double kh, boolean reverse){
        if (!reverse) return scaleH(kh);
        int oldHeight = getHeight();
        scaleH(kh);
        return subY(getHeight() - oldHeight);
    }

    default T cutX(double bw) {
        double width = getWidth();
        if (2*bw > width) bw = width / 2;
        return addX(bw).subW(2*bw);
    }
    default T cutY(double bh) {
        double height = getHeight();
        if (2*bh > height) bh = height / 2;
        return addY(bh).subH(2*bh);
    }
    default T cut(double bw, double bh) { return cutX(bw).cutY(bh); }
    default T cut (double b)  { return cutX(b).cutY(b); }

    default T pwcutX(double pb) { return cutX(pb * getSize().width); }
    default T pwcutY(double pb) { return cutY(pb * getSize().width); }
    default T pwcut (double pb) { return cut (pb * getSize().width); }
    default T pwcut(double pbw, double pbh) {
        double w = getSize().width;
        return cut(pbw * w, pbh * w);
    }
    
    default T phcutX(double pb) { return cutX(pb * getSize().height); }
    default T phcutY(double pb) { return cutY(pb * getSize().height); }
    default T phcut (double pb) { return cut (pb * getSize().height); }
    default T phcut(double pbw, double pbh) {
        double h = getSize().height;
        return cut(pbw * h, pbh * h);
    }
}
