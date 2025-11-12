package fipaan.com.array;

import fipaan.com.utils.StringUtils;
import fipaan.com.utils.ArrayUtils;
import fipaan.com.has.HasSize;
import fipaan.com.errors.FError;
import fipaan.com.errors.FThrow;
import java.util.Arrays;

public class Array2D<T> implements HasSize<Array2D<T>> {
    private T[] arr;
    private int width, height;

    public T[] getRaw()               { return arr; }
    public Array2D<T> setRaw(T[] arr) { throw FError.TODO(); }
    
    protected Array2D<T> setWidthRaw(int w)  { width  = w; return this; }
    protected Array2D<T> setHeightRaw(int h) { height = h; return this; }

    public Array2D(int w, int h) {
        FThrow.TODO();
        // T[] arr = ArrayUtils.<T>New(width*height);
        StringUtils.memset(arr, null);
        setRaw(arr);
        setWidthRaw(w);
        setWidthRaw(h);
    }

    @Override public int getWidth() { return width; }
    @Override public Array2D<T> setWidth(int w) {
        Array2D<T> newBuf = new Array2D<>(w, getHeight());
        ArrayUtils.moveArr(this, newBuf, new ArrayOpData());
        setRaw(newBuf.getRaw());
        setWidthRaw(w);
        return this;
    }
    @Override public int getHeight() { return height; }
    @Override public Array2D<T> setHeight(int h) {
        int width = getWidth();
        FThrow.TODO();
        // T[] buf = ArrayUtils.<T>New(width*h);
        memset(null);
        // System.arraycopy(getRaw(), 0, buf, 0, width*Math.min(getHeight(), h));
        // setRaw(buf);
        setHeightRaw(h);
        return this;
    }

    public T get(int x, int y) {
        return getRaw()[getWidth()*y + x];
    }
    public T[] get(int y) {
        int width = getWidth();
        return Arrays.copyOfRange(getRaw(), width*y, width*(y + 1));
    }
    public T[] getX(int x) {
        int height = getHeight();
        throw FError.TODO();
        // T[] buf = ArrayUtils.<T>New(height);
        // for (int j = 0; j < height; ++j) {
        //     buf[j] = get(x, j);
        // }
        // return buf;
    }
    public Array2D<T> set(T val, int x, int y) {
        getRaw()[getWidth()*y + x] = val;
        return this;
    }
    public Array2D<T> clear() {
        return memset(null);
    }
    public Array2D<T> move(T[] in, int inShift, int inLength, int x, int y) {
        System.arraycopy(in, inShift, getRaw(), getWidth()*y + x, inLength);
        return this;
    }
    public Array2D<T> move(T[] in, int x, int y) {
        return move(in, 0, in.length, x, y);
    }
    public Array2D<T> move(T[] in, int y) {
        return move(in, 0, in.length, 0, y);
    }
    public Array2D<T> shiftY(int dy) {
        int width  = getWidth();
        int height = getHeight();
        System.arraycopy(getRaw(), dy*width, getRaw(), 0, (height - dy)*width);
        memset((height - dy)*width, height*width, null);
        return this;
    }
    public int getLineWidth(int y) {
        T[] buf = getRaw();
        int width = getWidth();

        int cursor = 0;
        while (cursor < width && buf[width*y + cursor] != null) cursor += 1;
        return cursor;
    }
    public Array2D<T> eraseLine(int x, int y) { return erase(x, y, -1); }
    public Array2D<T> eraseChar(int y)        { return erase(-1, y, 1); }
    public Array2D<T> eraseChar(int x, int y) { return erase(x, y, 1);  }
    public Array2D<T> erase(int x, int y, int count) {
        int lineWidth = getLineWidth(y);
        if (x >= lineWidth) return this;
        if (x     < 0) x     += lineWidth;
        if (count < 0) count += lineWidth - x;
        int cursorStart = (getWidth() + 1)*y;
        int cursor      = cursorStart + x;
        // TODO: it shouldn't cut off the rest, but it's requires too much effort
        int cursorInterEnd = cursorStart + Math.min(x + lineWidth - count, lineWidth);
        int cursorEnd      = cursorStart + lineWidth;
        T[] buf = getRaw();
        while (cursor < cursorInterEnd) {
            buf[cursor] = buf[cursor + count];
            cursor += 1;
        }
        while (cursor < cursorEnd) {
            buf[cursor] = null;
            cursor += 1;
        }
        return this;
    }

    public Array2D<T> memset(T val) {
        StringUtils.memset(getRaw(), val);
        return this;
    }
    public Array2D<T> memset(int from, int to, T val) {
        StringUtils.memset(getRaw(), from, to, val);
        return this;
    }
    public Array2D<T> eraseToEOS(int x, int y) {
        int width = getWidth();
        return memset(width*y + x, width*getHeight(), null);
    }
    public Array2D<T> eraseToBOS(int x, int y) {
        return memset(0, getWidth()*y + x, null);
    }
    public Array2D<T> eraseToEOL(int x, int y) {
        int width = getWidth();
        return memset(width*y + x, width*y + width, null);
    }
    public Array2D<T> eraseToBOL(int x, int y) {
        int width = getWidth();
        return memset(width*y, width*y + x, null);
    }
    public Array2D<T> eraseLine(int y) {
        int width = getWidth();
        return memset(width*y, width*(y + 1), null);
    }
    public Array2D<T> erase() {
        return memset(0, getWidth()*getHeight(), null);
    }
    @Override public String toString() {
        int width  = getWidth();
        int height = getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("fipaan.Array2D<T>{"); // TODO: also display type, if possible
        for (int j = 0; j < height; ++j) {
            if (j > 0) sb.append(", ");
            sb.append("{");
            for (int i = 0; i < width; ++i) {
                if (i > 0) sb.append(", ");
                sb.append(get(i, j).toString());
            }
            sb.append("}");
        }
        sb.append("}");
        return new String(sb);
    }
}
