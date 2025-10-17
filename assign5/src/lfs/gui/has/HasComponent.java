package lfs.gui.has;

import org.w3c.dom.events.Event;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public interface HasComponent<T extends HasComponent<T>> {
    Component getComponent();
    T setComponent(Component component);

    default T setBounds(int x, int y, int width, int height) { getComponent().setBounds(x, y, width, height); return (T)this; }
    default T setBounds(Rectangle r) { getComponent().setBounds(r); return (T)this; }
    default T setVisible(boolean b) { getComponent().setVisible(b); return (T)this; }

    default T setBackground(Color c) { getComponent().setBackground(c); return (T)this; }
}
