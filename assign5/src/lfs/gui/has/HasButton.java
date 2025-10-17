package lfs.gui.has;

import javax.swing.JButton;

public interface HasButton<T extends HasButton<T>> {
    JButton getButton();
    T setButton(JButton btn);
}
