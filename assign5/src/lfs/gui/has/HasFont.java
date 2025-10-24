package lfs.gui.has;

import java.awt.Font;

public interface HasFont<Self extends HasFont<Self>> {
    Font getFont();
    Self setFont(Font font);
}
