package fipaan.com.geom.has;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;

public interface HasLocation<Self extends HasLocation<Self>> {
    int getX();
    Self setX(int x);
    int getY();
    Self setY(int y);
    
    default Self setX(float x)  { return setX((int)x); }
    default Self setY(float y)  { return setY((int)y); }
    default Self setX(double x) { return setX((int)x); }
    default Self setY(double y) { return setY((int)y); }

    default Point getLocation() { return new Point(getX(), getY()); }
    default Self setLocation(Point other) { return setX(other.x).setY(other.y); }

    default Self setLocation(int x, int y) { return setX(x).setY(y); }
    default Self setLocation(Dimension other) { return setX(other.width).setY(other.height); }
    default Self setLocation(Rectangle other) { return setX(other.x).setY(other.x); }
    default Self setLocation(HasLocation<?> other) { return setX(other.getX()).setY(other.getY()); }
    
    default boolean eqPos(int x, int y)         { return getX() == x && getY() == y; }
    default boolean eqPos(Dimension other)      { return getX() == other.width  && getY() == other.height; }
    default boolean eqPos(Rectangle other)      { return getX() == other.x      && getY() == other.x;      }
    default boolean eqPos(HasLocation<?> other) { return getX() == other.getX() && getY() == other.getY(); }
    
    default double minLocation() { return Math.min(getX(), getY()); }
    default double maxLocation() { return Math.max(getX(), getY()); }
    
    default Self scaleX(double kx) { return setX(getX() * kx); }
    default Self scaleY(double ky) { return setY(getY() * ky); }
    default Self scaleL(double kx, double ky) { return scaleX(kx).scaleY(ky); }
    default Self scaleL(double k) { return scaleX(k).scaleY(k); }

    default Self addX(double x) { return setX(getX() + x); }
    default Self addY(double y) { return setY(getY() + y); }
    default Self addL(double x, double y) { return addX(x).addY(y); }
    default Self addL(double t) { return addX(t).addY(t); }

    default Self subX(double x) { return setX(getX() - x); }
    default Self subY(double y) { return setY(getY() - y); }
    default Self subL(double x, double y) { return subX(x).subY(y); }
    default Self subL(double t) { return subX(t).subY(t); }
}
