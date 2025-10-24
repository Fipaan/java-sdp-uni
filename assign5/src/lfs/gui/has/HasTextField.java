package lfs.gui.has;

import javax.swing.JTextField;

public interface HasTextField<Self extends HasTextField<Self>> {
    JTextField getTextField();
    Self setTextField(JTextField tf);
}
