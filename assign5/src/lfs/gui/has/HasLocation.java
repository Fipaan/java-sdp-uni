package lfs.gui.has;

import java.awt.Point;
import java.awt.Dimension;

public interface HasLocation<T extends HasLocation<T>> {
    int getX();
    T setX(int x);
    int getY();
    T setY(int y);
    
    default T setX(float x)  { return setX((int)x); }
    default T setY(float y)  { return setY((int)y); }
    default T setX(double x) { return setX((int)x); }
    default T setY(double y) { return setY((int)y); }

    default Point getLocation() { return new Point(getX(), getY()); }
    default T setLocation(Point other) { return setX(other.x).setY(other.y); }

    default T setLocation(int x, int y) { return setX(x).setY(y); }
    default T setLocation(Dimension other) { return setX(other.width).setY(other.height); }
    default T setLocation(HasLocation<?> other) { return setX(other.getX()).setY(other.getY()); }
    default double minLocation() { return Math.min(getX(), getY()); }
    default double maxLocation() { return Math.max(getX(), getY()); }
    
    default T scaleX(double kx) { return setX(getX() * kx); }
    default T scaleY(double ky) { return setY(getY() * ky); }
    default T scaleL(double kx, double ky) { return scaleX(kx).scaleY(ky); }
    default T scaleL(double k) { return scaleX(k).scaleY(k); }

    default T addX(double x) { return setX(getX() + x); }
    default T addY(double y) { return setY(getY() + y); }
    default T addL(double x, double y) { return addX(x).addY(y); }
    default T addL(double t) { return addX(t).addY(t); }

    default T subX(double x) { return setX(getX() - x); }
    default T subY(double y) { return setY(getY() - y); }
    default T subL(double x, double y) { return subX(x).subY(y); }
    default T subL(double t) { return subX(t).subY(t); }
}
