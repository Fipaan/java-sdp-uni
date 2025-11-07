package fipaan.com.utils;

import fipaan.com.wrapper.FDimension;
import fipaan.com.errors.FThrow;
import fipaan.com.errors.FError;
import java.io.*;

public class ConsoleUtils {
    public static final String ESC = "\u001b";
    public static void outWrite(OutputStream out, String str) {
        try {
            out.write(str.getBytes());
        } catch (IOException e) { FThrow.New(e, "write failed"); }
    }
    public static String escaped(String fmt, Object... args) {
        return String.format(ESC + fmt, args);
    }
    public static String escaped(Object obj) {
        return ESC + obj.toString();
    }
    public static void escaped(OutputStream out, String fmt, Object... args) {
        outWrite(out, escaped(fmt, args));
    }
    public static void escaped(OutputStream out, Object obj) {
        outWrite(out, escaped(obj));
    }
    public static void escaped(OutputStream out, boolean flushed, String fmt, Object... args) {
        String command = String.format(fmt, args);
        outWrite(out, escaped(command));
        if (!flushed) return;
        try {
            out.flush();
        } catch (IOException e) {
            FThrow.New(e, "Couldn't flush escape sequence: <ESC>%s", command);
        }
    }
    public static void escaped(OutputStream out, boolean flushed, Object obj) {
        escaped(out, flushed, "%s", obj.toString());
    }

    public static FDimension getConsoleSize(FDimension out) {
        try {
            Process p = new ProcessBuilder("sh", "-c", "stty size < /dev/tty").start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = br.readLine();
            if (line != null) {
                String[] parts = line.trim().split("\\s+");
                out.setHeight(Integer.parseInt(parts[0]));
                out.setWidth(Integer.parseInt(parts[1]));
                return out;
            } else throw FError.New("Couldn't get terminal size");
        } catch (Exception e) {
            throw FError.New(e, "Couldn't get terminal size");
        }
    }
    public static FDimension getConsoleSize() { return getConsoleSize(new FDimension()); }
}
