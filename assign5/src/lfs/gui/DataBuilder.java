package lfs.gui;

import lfs.errors.FError;
import java.util.function.Consumer;
import java.lang.Runnable;
import java.awt.*;
import javax.swing.*;

public class DataBuilder {
    private Dimension screenSize;
    private Font font;
    private JFrame frame;
    private Color background, foreground;
    private Consumer<Data> onBuild;
    private Consumer<Data> onResize;
    public DataBuilder() {}
 
    public DataBuilder setInitScreenSize(Dimension initScreenSize) { screenSize = initScreenSize; return this; }
    public DataBuilder setFont(Font f) { font = f; return this; }
    public DataBuilder setParent(JFrame parent) { frame = parent; return this; }
    public DataBuilder setBackground(Color bg) { background = bg; return this; }
    public DataBuilder setForeground(Color fg) { foreground = fg; return this; }
    
    public DataBuilder actionOnBuild(Consumer<Data> action) { onBuild = action; return this; }
    public DataBuilder actionOnBuild(Runnable action) { onBuild = (data) -> action.run(); return this; }
    
    public DataBuilder actionOnResize(Consumer<Data> action) { onResize = action; return this; }
    public DataBuilder actionOnResize(Runnable action) { onResize = (data) -> action.run(); return this; }

    public Data build() {
        FError.verifyNotNull(screenSize, "Screen size");
        FError.verifyNotNull(font,       "Font");
        FError.verifyNotNull(frame,      "Parent frame");
        if (background == null && foreground == null) {
            background = Color.WHITE;
            foreground = Color.BLACK;
        } else {
            FError.verifyNotNull(background, "Background");
            FError.verifyNotNull(foreground, "Foreground");
        }
        if (onResize == null) onResize = (data) -> {};
        if (onBuild  == null) onBuild  = (data) -> {};
        if (onResize == null) onResize = (data) -> {};
        return new Data(
            screenSize, font, frame,
            background, foreground,
            onBuild, onResize
        );
    }
}
