package fipaan.com.is;

import fipaan.com.utils.*;
import fipaan.com.has.*;
import fipaan.com.array.*;
import java.util.Arrays;

public interface IsArray2DInt<Self extends IsArray2DInt<Self>> extends HasSize<Self> {
    int[] getRaw();
    Self setRaw(int[] arr);

    Self setWidthRaw(int w);
    Self setHeightRaw(int h);

    default void defaultConstructor(int width, int height) {
        int[] arr = new int[width*height];
        StringUtils.memset(arr, 0);
        setRaw(arr);
        setWidthRaw(width);
        setHeightRaw(height);
    }

    default Self setWidth(int w) {
        IsArray2DInt<?> newBuf = new Array2DInt(w, getHeight());
        ArrayUtils.moveArr((Self)this, newBuf);
        setRaw(newBuf.getRaw());
        setWidthRaw(w);
        return (Self)(Self)this;
    }
    default Self setHeight(int h) {
        int width = getWidth();
        int[] buf = new int[width*h];
        StringUtils.memset(buf, 0);
        System.arraycopy(getRaw(), 0, buf, 0, width*Math.min(getHeight(), h));
        setRaw(buf);
        setHeightRaw(h);
        return (Self)this;
    }

    default int get(int x, int y) {
        return getRaw()[getWidth()*y + x];
    }
    default int[] get(int y) {
        int width = getWidth();
        return Arrays.copyOfRange(getRaw(), width*y, width*(y + 1));
    }
    default int[] getX(int x) {
        int height = getHeight();
        int[] buf = new int[height];
        for (int j = 0; j < height; ++j) {
            buf[j] = get(x, j);
        }
        return buf;
    }
    default Self set(int val, int x, int y) {
        getRaw()[getWidth()*y + x] = val;
        return (Self)this;
    }
    default Self clear() {
        return memset(0);
    }
    default Self move(int[] in, int inShift, int inLength, int x, int y) {
        System.arraycopy(in, inShift, getRaw(), getWidth()*y + x, inLength);
        return (Self)this;
    }
    default Self move(int[] in, int x, int y) {
        return move(in, 0, in.length, x, y);
    }
    default Self move(int[] in, int y) {
        return move(in, 0, in.length, 0, y);
    }
    default Self move(String in, int inShift, int inLength, int x, int y) {
        int[] inArr = in.codePoints().toArray();
        System.arraycopy(inArr, inShift, getRaw(), getWidth()*y + x, inLength);
        return (Self)this;
    }
    default Self move(String in, int x, int y) {
        return move(in, 0, in.length(), x, y);
    }
    default Self move(String in, int y) {
        return move(in, 0, in.length(), 0, y);
    }
    default int getLineWidth(int y) {
        int[] buf = getRaw();
        int width = getWidth();

        int cursor = width*y;
        while (buf[cursor] != 0) cursor += 1;
        return cursor - width*y;
    }
    default Self eraseLine(int x, int y) { return erase(x, y, -1); }
    default Self eraseChar(int y)        { return erase(-1, y, 1); }
    default Self eraseChar(int x, int y) { return erase(x, y, 1);  }
    default Self erase(int x, int y, int count) {
        int lineWidth = getLineWidth(y);
        if (x >= lineWidth) return (Self)this;
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
        return (Self)this;
    }

    default Self memset(int val) {
        StringUtils.memset(getRaw(), val);
        return (Self)this;
    }
    default Self memset(int from, int to, int val) {
        StringUtils.memset(getRaw(), from, to, val);
        return (Self)this;
    }
    default Self eraseToEOS(int x, int y) {
        int width = getWidth();
        return memset(width*y + x, width*getHeight(), 0);
    }
    default Self eraseToBOS(int x, int y) {
        return memset(0, getWidth()*y + x, 0);
    }
    default Self eraseToEOL(int x, int y) {
        int width = getWidth();
        return memset(width*y + x, width*y + width, 0);
    }
    default Self eraseToBOL(int x, int y) {
        int width = getWidth();
        return memset(width*y, width*y + x, 0);
    }
    default Self eraseLine(int y) {
        int width = getWidth();
        return memset(width*y, width*(y + 1), 0);
    }
    String toString();
    default String toStringImpl() {
        int width  = getWidth();
        int height = getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("{\r\n");
        for (int j = 0; j < height; ++j) {
            if (j > 0) sb.append(",\r\n");
            sb.append("  {\r\n");
            for (int i = 0; i < width; ++i) {
                if (i > 0) {
                    if (i % 10 == 0) {
                        sb.append(",\r\n    ");
                    } else {
                        sb.append(", ");
                    }
                } else sb.append("    ");
                sb.append(String.valueOf(get(i, j)));
            }
            if (width % 10 != (10 - 1)) sb.append("\r\n");
            sb.append("\r  }");
        }
        sb.append("\r\n}\r\n");
        return new String(sb);
    }
}
