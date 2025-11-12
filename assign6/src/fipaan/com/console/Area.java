package fipaan.com.console;

import fipaan.com.array.*;
import fipaan.com.wrapper.*;
import fipaan.com.utils.*;
import fipaan.com.errors.*;

// TODO: simpler movArray syntax
public class Area {
    public FPoint pos;
    private Array2DInt buf, backBuf;
    
    public int getWidth() { return buf.getHeight(); }
    public int getHeight() { return buf.getHeight(); }

    public Area(int x, int y, String... lines) {
        int height = lines.length;
        if (height == 0) throw FError.New("Expected at least one String");
        int width = lines[0].length();
        for (int j = 1; j < height; ++j) {
            if (lines[j].length() != width) throw FError.New("Not a rectangular region");
        }
        backBuf = new Array2DInt(width, height);
        buf     = new Array2DInt(width, height);
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                backBuf.set(' ', i, j);
                buf.set(lines[j].codePointAt(i), i, j);
            }
        }
        this.pos = new FPoint(x, y);
    }

    public Area applyBack(Console console) {
        int width  = getWidth();
        int height = getHeight();
        ArrayUtils.onArraysDo(
            console.getScreenArr(),
            backBuf,
            new ArrayOpData(pos.x, pos.y, true),
            (in, out, inX, inY, outX, outY) -> {
                if (outX <= 0 || outX >= width  - 1) return;
                if (outY <= 0 || outY >= height - 1) return;
                int code = console.getAt(inX, inY);
                if (code == '\0') code = ' ';
                
                backBuf.set(code, outX, outY);
            });
        return this;
    }

    public Area restoreBack(Console console) {
        int width  = console.getWidth();
        int height = console.getHeight();
        System.err.println("restoreBack()");
        ArrayUtils.onArraysDo(
            backBuf,
            console.getScreenArr(),
            new ArrayOpData(pos.x, pos.y),
            (in, out, inX, inY, outX, outY) -> {
                if (outX <= 0 || outX >= width  - 1) return;
                if (outY <= 0 || outY >= height - 1) return;
                int backCode = backBuf.get(inX, inY);
                console.putCodepoint(backCode, outX, outY);
            });
        return this;
    }


    public Area displayArea(Console console) {
        int width  = console.getWidth();
        int height = console.getHeight();
        ArrayUtils.onArraysDo(
            backBuf,
            console.getScreenArr(),
            new ArrayOpData(pos.x, pos.y),
            (in, out, inX, inY, outX, outY) -> {
                if (outX <= 0 || outX >= width  - 1) return;
                if (outY <= 0 || outY >= height - 1) return;
                int code     = buf.get(inX, inY);
                int backCode = backBuf.get(inX, inY);
                console
               .putCodepoint(code == ' ' ? backCode : code, outX, outY);
            });
        return this;
    }
}
