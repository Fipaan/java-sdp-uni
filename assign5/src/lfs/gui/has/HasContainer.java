package lfs.gui.has;

import lfs.errors.FError;
import java.awt.Container;
import java.awt.Component;
import javax.swing.JPanel;

public interface HasContainer<Self extends HasContainer<Self, Cont>, Cont extends Container>
            extends HasComponent<Self, Cont> {
    Cont getContainer();
    Self setContainer(Cont container);

    default Self setComponent(Cont component) { setContainer(component); return (Self)this; }
    default Cont getComponent() { return getContainer(); }
}
