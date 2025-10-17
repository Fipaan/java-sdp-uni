package lfs.gui.has;

import javax.swing.JTextField;

public interface HasTextField<T extends HasTextField<T>> {
    JTextField getTextField();
    T setTextField(JTextField tf);
}
