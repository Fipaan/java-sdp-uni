package lfs.gui.has;

import javax.swing.JLabel;

public interface HasHint<Self extends HasHint<Self>> {
    JLabel getHint();
    Self setHint(JLabel hint);
}
