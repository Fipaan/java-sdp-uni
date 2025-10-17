package lfs.gui.has;

import javax.swing.JLabel;

public interface HasHint<T extends HasHint<T>> {
    JLabel getHint();
    T setHint(JLabel hint);
}
