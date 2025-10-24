package lfs.gui.has;

import java.awt.Component;

public interface HasComponent<Self extends HasComponent<Self, Comp>, Comp extends Component> {
    Comp getComponent();
    Self setComponent(Comp component);
}
