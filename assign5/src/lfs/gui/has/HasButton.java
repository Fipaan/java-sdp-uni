package lfs.gui.has;

import javax.swing.JButton;

public interface HasButton<Self extends HasButton<Self>> {
    JButton getButton();
    Self setButton(JButton btn);
}
