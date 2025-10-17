package lfs.gui.has;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.Map;
import java.text.AttributedCharacterIterator;

public interface HasFont<T extends HasFont<T>> {
    Font getFont();
    T setFont(Font font);

    default Font deriveFont(float size)                       { return getFont().deriveFont(size); }
    default Font deriveFont(int style)                        { return getFont().deriveFont(style); }
    default Font deriveFont(int style, AffineTransform trans) { return getFont().deriveFont(style, trans); }
    default Font deriveFont(int style, float size)            { return getFont().deriveFont(style, size); }
    default Font deriveFont(Map<? extends AttributedCharacterIterator.Attribute,?>
                            attributes)                       { return getFont().deriveFont(attributes); }
}
