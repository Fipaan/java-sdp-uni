package fipaan.com.array;

import java.awt.Rectangle;

public class ArrayOpData {
    public Rectangle in, out;
    public int width, height;
    public ArrayOpData(Rectangle inRect, Rectangle outRect, int w, int h) {
        in     = inRect;
        out    = outRect;
        width  = w;
        height = h;
    }
    public ArrayOpData(int  inX, int  inY, int  inW, int  inH,
                       int outX, int outY, int outW, int outH,
                       int w, int h) {
        this(new Rectangle(inX, inY, inW, inH), new Rectangle(outX, outY, outW, outH), w, h);
    }
    public ArrayOpData(int  inX, int  inY,
                       int outX, int outY,
                       int w, int h) {
        this(inX, inY, -1, -1, outX, outY, -1, -1, w, h);
    }
    public ArrayOpData(int outX, int outY,
                       int w, int h) {
        this(0, 0, -1, -1, outX, outY, -1, -1, w, h);
    }
    public ArrayOpData(int outX, int outY) {
        this(0, 0, -1, -1, outX, outY, -1, -1, -1, -1);
    }
    public ArrayOpData(int x, int y, boolean rev) {
        this(rev ? x : 0, rev ? y : 0, -1, -1, rev ? 0 : y, rev ? 0 : y, -1, -1, -1, -1);
    }
    public ArrayOpData() {
        this(0, 0, -1, -1, 0, 0, -1, -1, -1, -1);
    }
    private ArrayOpData applySizes(int inW, int inH, int outW, int outH) {
        if ( in.width  < 0)  in.width  =  inW;
        if ( in.height < 0)  in.height =  inH;
        if (out.width  < 0) out.width  = outW;
        if (out.height < 0) out.height = outH;
        if (    width  < 0)     width  =  inW;
        if (    height < 0)     height =  inH;
        return this;
    }
    public <T> ArrayOpData apply(T[][] in, T[][] out) {
        if (in.length  == 0) return null;
        if (out.length == 0) return null;
        return applySizes(in[0].length, in.length, out[0].length, out.length);
    }
    public <T> ArrayOpData apply(Array2D<T> in, Array2D<T> out) {
        return applySizes(in.getWidth(), in.getHeight(), out.getWidth(), out.getHeight());
    }
    public ArrayOpData apply(Array2DInt in, Array2DInt out) {
        return applySizes(in.getWidth(), in.getHeight(), out.getWidth(), out.getHeight());
    }
    public boolean verify() {
        if (in.y < 0) { height += in.y; in.y = 0; }
        if (in.y > in.height) return false;
        else if (in.y + height >= in.height) height = in.height - in.y;

        if (out.y < 0) { height += out.y; out.y = 0; }
        if (out.y > out.height) return false;
        else if (out.y + height >= out.height) height = Math.min(height, out.height - out.y);

        if (height <= 0) return false;
        
        if (in.x < 0) { width += in.x; in.x = 0; }
        if (in.x > in.width) return false;
        else if (in.x + width >= in.width) width = in.width - in.x;
        
        if (out.x < 0) { width += out.x; out.x = 0; }
        if (out.x > out.width) return false;
        else if (out.x + width > out.width) width = Math.min(width, out.width - out.x);

        if (width <= 0) return false;

        return true;
    }

    @Override public String toString() {
        return String.format("fipaan.ArrayOpData{in: %s, out: %s, width: %d, height: %d}", in.toString(), out.toString(), width, height);
    }
}
