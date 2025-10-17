package lfs.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.function.Consumer;
import lfs.gui.wrapper.*;

public class FGUI {
    public static float getBaseFontSize(Dimension screenSize) { return Math.max(12, (float)screenSize.width / 20); }
    public static <T extends Container> void resetBounds(RContainer<T> comp) {
        comp.setBounds(comp.getRect());
    }
    public static FRectangle resetByScreenSize(Dimension screenSize, RContainer<?> obj) {
        return obj.rect.setSize(screenSize).setLocation(screenSize);
    }
    public static <T extends Container> void modifyBoundsAndReset(Dimension screenSize, RContainer<T> comp, Consumer<FRectangle> f) {
        f.accept(resetByScreenSize(screenSize, comp));
        resetBounds(comp);
    }

    public static <T> void bindKey(JComponent comp, String key, Consumer<T> action, T ref) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(key);
        String actionName = "action_" + key;

        comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(keyStroke, actionName);

        comp.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.accept(ref);
            }
        });
    }
    public static <T> void bindKey(JComponent comp, String key, Consumer<T> action) {
        bindKey(comp, key, action, null);
    }
    public static <T> void bindKey(JFrame frame, String key, Consumer<T> action, T ref) {
        bindKey(frame.getRootPane(), key, action, ref);
    }
    public static <T> void bindKey(JFrame frame, String key, Consumer<T> action) {
        bindKey(frame.getRootPane(), key, action, null);
    }
    public static void notifyInfo(Object obj) {
        JOptionPane.showMessageDialog(
            null,
            obj,
            "Info",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    public static void notifyInfo(String fmt, Object... args) {
        notifyInfo(String.format(fmt, (Object[])args));
    }
    public static void notifyWarn(Object obj) {
        JOptionPane.showMessageDialog(
            null,
            obj,
            "Warning",
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    public static void notifyWarn(String fmt, Object... args) {
        notifyWarn(String.format(fmt, (Object[])args));
    }
    
    public static void notifyError(Object obj) {
        JOptionPane.showMessageDialog(
            null,
            obj,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    public static void notifyError(String fmt, Object... args) {
        notifyError(String.format(fmt, (Object[])args));
    }
}
