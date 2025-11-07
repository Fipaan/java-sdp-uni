package fipaan.com.array;

import fipaan.com.utils.StringUtils;
import fipaan.com.utils.ArrayUtils;
import fipaan.com.errors.FError;
import fipaan.com.has.HasSize;
import java.util.Arrays;

public class Array2DInt implements HasSize<Array2DInt> {
    private int[] arr;
    private int width, height;

    public int[] getRaw()               { return arr; }
    public Array2DInt setRaw(int[] arr) {
        // TODO; safety check
        this.arr = arr;
        return this;
    }
    
    protected Array2DInt setWidthRaw(int w)  { width  = w; return this; }
    protected Array2DInt setHeightRaw(int h) { height = h; return this; }

    public Array2DInt(int w, int h) {
        int[] buf = new int[w*h];
        StringUtils.memset(buf, 0);
        setRaw(buf);
        setWidthRaw(w);
        setWidthRaw(h);
    }

    @Override public int getWidth() { return width; }
    @Override public Array2DInt setWidth(int w) {
        Array2DInt newBuf = new Array2DInt(w, getHeight());
        ArrayUtils.moveArr(this, newBuf, new ArrayOpData());
        setRaw(newBuf.getRaw());
        setWidthRaw(w);
        return this;
    }
    @Override public int getHeight() { return height; }
    @Override public Array2DInt setHeight(int h) {
        int width = getWidth();
        int[] buf = new int[width*h];
        memset(0);
        System.arraycopy(getRaw(), 0, buf, 0, width*Math.min(getHeight(), h));
        setRaw(buf);
        setHeightRaw(h);
        return this;
    }

    public int get(int x, int y) {
        return getRaw()[getWidth()*y + x];
    }
    public int[] get(int y) {
        int width = getWidth();
        return Arrays.copyOfRange(getRaw(), width*y, width*(y + 1));
    }
    public int[] getX(int x) {
        int height = getHeight();
        int[] buf = new int[height];
        for (int j = 0; j < height; ++j) {
            buf[j] = get(x, j);
        }
        return buf;
    }
    public Array2DInt set(int val, int x, int y) {
        getRaw()[getWidth()*y + x] = val;
        return this;
    }
    public Array2DInt clear() {
        return memset(0);
    }
    public Array2DInt move(int[] in, int inShift, int inLength, int x, int y) {
        System.arraycopy(in, inShift, getRaw(), getWidth()*y + x, inLength);
        return this;
    }
    public Array2DInt move(int[] in, int x, int y) {
        return move(in, 0, in.length, x, y);
    }
    public Array2DInt move(int[] in, int y) {
        return move(in, 0, in.length, 0, y);
    }
    public int getLineWidth(int y) {
        int[] buf = getRaw();
        int width = getWidth();

        int cursor = 0;
        while (cursor < width && buf[width*y + cursor] != 0) cursor += 1;
        return cursor;
    }
    public Array2DInt eraseLine(int x, int y) { return erase(x, y, -1); }
    public Array2DInt eraseChar(int y)        { return erase(-1, y, 1); }
    public Array2DInt eraseChar(int x, int y) { return erase(x, y, 1);  }
    public Array2DInt erase(int x, int y, int count) {
        int lineWidth = getLineWidth(y);
        if (x >= lineWidth) return this;
        if (x     < 0) x     += lineWidth;
        if (count < 0) count += lineWidth - x;
        int cursorStart = (getWidth() + 1)*y;
        int cursor      = cursorStart + x;
        // TODO: it shouldn't cut off the rest, but it's requires too much effort
        int cursorInterEnd = cursorStart + Math.min(x + lineWidth - count, lineWidth);
        int cursorEnd      = cursorStart + lineWidth;
        int[] buf = getRaw();
        while (cursor < cursorInterEnd) {
            buf[cursor] = buf[cursor + count];
            cursor += 1;
        }
        while (cursor < cursorEnd) {
            buf[cursor] = 0;
            cursor += 1;
        }
        return this;
    }

    public Array2DInt memset(int val) {
        StringUtils.memset(getRaw(), val);
        return this;
    }
    public Array2DInt memset(int from, int to, int val) {
        StringUtils.memset(getRaw(), from, to, val);
        return this;
    }
    public Array2DInt eraseToEOS(int x, int y) {
        int width = getWidth();
        return memset(width*y + x, width*getHeight(), 0);
    }
    public Array2DInt eraseToBOS(int x, int y) {
        return memset(0, getWidth()*y + x, 0);
    }
    public Array2DInt eraseToEOL(int x, int y) {
        int width = getWidth();
        return memset(width*y + x, width*y + width, 0);
    }
    public Array2DInt eraseToBOL(int x, int y) {
        int width = getWidth();
        return memset(width*y, width*y + x, 0);
    }
    public Array2DInt eraseLine(int y) {
        int width = getWidth();
        return memset(width*y, width*(y + 1), 0);
    }
    @Override public String toString() {
        int width  = getWidth();
        int height = getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("fipaan.Array2DInt{");
        for (int j = 0; j < height; ++j) {
            if (j > 0) sb.append(", ");
            sb.append("{");
            for (int i = 0; i < width; ++i) {
                if (i > 0) sb.append(", ");
                sb.append(String.valueOf(get(i, j)));
            }
            sb.append("}");
        }
        sb.append("}");
        return new String(sb);
    }
}
