package lfs.gui.has;

import org.w3c.dom.events.Event;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public interface HasContainer<T extends HasContainer<T>> extends HasComponent<T> {
    Container getContainer();
    T setContainer(Container cont);
    
    default Component add(Component comp) { return getContainer().add(comp); } 
    default Component add(Component comp, int index) { return getContainer().add(comp, index); }
    default T add(Component comp, Object constraints) { getContainer().add(comp, constraints); return (T)this; } 
    default T add(Component comp, Object constraints, int index) { getContainer().add(comp, constraints, index); return (T)this; } 
    default Component add(String name, Component comp) { return getContainer().add(name, comp); }
    default T addContainerListener(ContainerListener l) { getContainer().addContainerListener(l); return (T)this; } 
    default T addNotify() { getContainer().addNotify(); return (T)this; } 
    default T addPropertyChangeListener(PropertyChangeListener listener) { getContainer().addPropertyChangeListener(listener); return (T)this; } 
    default T addPropertyChangeListener(String propertyName, PropertyChangeListener listener) { getContainer().addPropertyChangeListener(propertyName, listener); return (T)this; } 
    default T applyComponentOrientation(ComponentOrientation o) { getContainer().applyComponentOrientation(o); return (T)this; } 
    default boolean areFocusTraversalKeysSet(int id) { return getContainer().areFocusTraversalKeysSet(id); }
    default T doLayout() { getContainer().doLayout(); return (T)this; } 
    default Component findComponentAt(int x, int y) { return getContainer().findComponentAt(x, y); }
    default Component findComponentAt(Point p) { return getContainer().findComponentAt(p); }
    default float getAlignmentX() { return getContainer().getAlignmentX(); }
    default float getAlignmentY() { return getContainer().getAlignmentY(); }
    default Component getComponent(int n) { return getContainer().getComponent(n); }
    default Component getComponentAt(int x, int y) { return getContainer().getComponentAt(x, y); }
    default Component getComponentAt(Point p) { return getContainer().getComponentAt(p); }
    default int getComponentCount() { return getContainer().getComponentCount(); }
    default Component[] getComponents() { return getContainer().getComponents();}
    default int getComponentZOrder(Component comp) { return getContainer().getComponentZOrder(comp); }
    default ContainerListener[] getContainerListeners() { return getContainer().getContainerListeners(); }
    default Set<AWTKeyStroke> getFocusTraversalKeys(int id) { return getContainer().getFocusTraversalKeys(id); }
    default FocusTraversalPolicy getFocusTraversalPolicy() { return getContainer().getFocusTraversalPolicy(); }
    default Insets getInsets() { return getContainer().getInsets(); }
    default LayoutManager getLayout() { return getContainer().getLayout(); }
    default <V extends EventListener> V[] getListeners(Class<V> listenerType) { return getContainer().getListeners(listenerType); }
    default Dimension getMaximumSize() { return getContainer().getMaximumSize(); }
    default Dimension getMinimumSize() { return getContainer().getMinimumSize(); }
    default Point getMousePosition(boolean allowChildren) { return getContainer().getMousePosition(allowChildren); }
    default Dimension getPreferredSize() { return getContainer().getPreferredSize(); }
    default T invalidate() { getContainer().invalidate(); return (T)this; } 
    default boolean isAncestorOf(Component c) { return getContainer().isAncestorOf(c); }
    default boolean isFocusCycleRoot() { return getContainer().isFocusCycleRoot(); }
    default boolean isFocusCycleRoot(Container container) { return getContainer().isFocusCycleRoot(container); }
    default boolean isFocusTraversalPolicyProvider() { return getContainer().isFocusTraversalPolicyProvider(); }
    default boolean isFocusTraversalPolicySet() { return getContainer().isFocusTraversalPolicySet(); }
    default boolean isValidateRoot() { return getContainer().isValidateRoot(); }
    default T list(PrintStream out, int indent) { getContainer().list(out, indent); return (T)this; } 
    default T list(PrintWriter out, int indent) { getContainer().list(out, indent); return (T)this; } 
    default T paint(Graphics g) { getContainer().paint(g); return (T)this; } 
    default T paintComponents(Graphics g) { getContainer().paintComponents(g); return (T)this; } 
    default T print(Graphics g) { getContainer().print(g); return (T)this; } 
    default T printComponents(Graphics g) { getContainer().printComponents(g); return (T)this; } 
    default T remove(Component comp) { getContainer().remove(comp); return (T)this; } 
    default T remove(int index) { getContainer().remove(index); return (T)this; } 
    default T removeAll() { getContainer().removeAll(); return (T)this; } 
    default T removeContainerListener(ContainerListener l) { getContainer().removeContainerListener(l); return (T)this; } 
    default T removeNotify() { getContainer().removeNotify(); return (T)this; } 
    default T setComponentZOrder(Component comp, int index) { getContainer().setComponentZOrder(comp, index); return (T)this; } 
    default T setFocusCycleRoot(boolean focusCycleRoot) { getContainer().setFocusCycleRoot(focusCycleRoot); return (T)this; } 
    default T setFocusTraversalKeys(int id, Set<? extends AWTKeyStroke> keystrokes) { getContainer().setFocusTraversalKeys(id, keystrokes); return (T)this; } 
    default T setFocusTraversalPolicy(FocusTraversalPolicy policy) { getContainer().setFocusTraversalPolicy(policy); return (T)this; } 
    default T setFocusTraversalPolicyProvider(boolean provider) { getContainer().setFocusTraversalPolicyProvider(provider); return (T)this; } 
    default T setFont(Font f) { getContainer().setFont(f); return (T)this; } 
    default T setLayout(LayoutManager mgr) { getContainer().setLayout(mgr); return (T)this; } 
    default T transferFocusDownCycle() { getContainer().transferFocusDownCycle(); return (T)this; } 
    default T update(Graphics g) { getContainer().update(g); return (T)this; } 
    default T validate() { getContainer().validate(); return (T)this; } 
}
