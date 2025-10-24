package lfs.gui;

import lfs.gui.has.*;
import lfs.gui.elems.*;
import lfs.gui.wrapper.*;
import lfs.print.*;
import lfs.errors.FError;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.function.*;

public class Data {
    private static final String TYPE_CONTAINER = "Container";
    private static final String TYPE_OLIST     = "FOList";
    private static final String TYPE_TEXTBOX   = "FTextbox";
    public Dimension screenSize;
    public Font font;
    public JFrame frame;
    public Color background, foreground;
    
    public void updateScreenSize() {
        screenSize.setSize(frame.getSize());
    }

    public Map<String, Named<?>> elems = new HashMap<>();

    public Data(
        Dimension screenSize, Font font, JFrame frame,
        Color background, Color foreground,
        Consumer<Data> onBuild, Consumer<Data> onResize
    ) {
        this.screenSize = screenSize;
        this.font       = font;
        this.background = background;
        this.foreground = foreground;

        this.frame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(screenSize);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(background);
        frame.getContentPane().setForeground(foreground);

        FGUI.bindKey(frame, "ESCAPE", (Object e) -> frame.dispose());

        onBuild.accept(this);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScreenSize();
                onResize.accept(Data.this);
            }
        });

        frame.getContentPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frame.requestFocus();
            }
        });

        frame.setVisible(true);
    }
    public <Cont extends Container> Data put(String name, Cont container) {
        RContainer<Cont> elem = new RContainer<>(container);
        elems.put(name, new Named(elem, TYPE_CONTAINER));
        return this;
    }
    public Data put(String name, FOList olist) {
        elems.put(name, new Named(olist, TYPE_OLIST));
        return this;
    }
    public Data put(String name, FTextbox textbox) {
        elems.put(name, new Named(textbox, TYPE_TEXTBOX));
        return this;
    }
    private static void verifyType(Named<?> elem, String type) {
        if (!type.equals(elem.name)) {
            throw FError.New("Invalid type: expected %s, got %s", type, elem.name);
        }
    }
    private Named<?> getAndVerifyType(Named<?> elem, String name, String type) {
        if (elem == null) throwUnknownElement(name, "Couldn't get element");
        verifyType(elem, type);
        return elem;
    }
    private <Cont extends Container> RContainer<Cont> getRCont(Named<?> elem, String name) {
        getAndVerifyType(elem, name, TYPE_CONTAINER);
        @SuppressWarnings("unchecked")
        RContainer<Cont> result = (RContainer<Cont>)elem.obj;
        return result;
    }
    public <Cont extends Container> RContainer<Cont> getRCont(String name) {
        return getRCont(elems.get(name), name);
    }
    
    private static void throwUnknownType(String type) { throw FError.New("unknown type: %s", type); }
    private String getElemType(String name) { return elems.get(name).name; }
    private static String getElemType(Named<?> elem) { return elem.name; }
    
    private <Cont extends Container> Cont getCont(Named<?> elem, String name) {
        return this.<Cont>getRCont(name).getContainer();
    }
    public <Cont extends Container> Cont getCont(String name) {
        return getCont(elems.get(name), name);
    }
    private FOList getOList(Named<?> elem, String name) {
        getAndVerifyType(elem, name, TYPE_OLIST);
        @SuppressWarnings("unchecked")
        FOList result = (FOList)elem.obj;
        return result;
    }
    public FOList getOList(String name) {
        return getOList(elems.get(name), name);
    }
    private FTextbox getTextbox(Named<?> elem, String name) {
        getAndVerifyType(elem, name, TYPE_TEXTBOX);
        @SuppressWarnings("unchecked")
        FTextbox result = (FTextbox)elem.obj;
        return result;
    }
    public FTextbox getTextbox(String name) {
        return getTextbox(elems.get(name), name);
    }
    public ArrayList<Container> getContainersBy(Predicate<String> cond) {
        ArrayList<Container> result = new ArrayList<>();
        for (Map.Entry<String, Named<?>> entry : elems.entrySet()) {
            String name = entry.getKey();
            Named<?> elem = entry.getValue();
            if (!cond.test(name)) continue;
            switch (getElemType(elem)) {
                case TYPE_CONTAINER: {
                    result.add(getCont(elem, name));
                } break;
                case TYPE_OLIST: {
                    result.add(getOList(elem, name).getComboBox());
                } break;
                case TYPE_TEXTBOX: {
                    result.add(getTextbox(elem, name).getContainer());
                } break;
                default: throwUnknownType(getElemType(elem));
            }
        }
        return result;
    }
    public ArrayList<Container> getContainersBySubstring(String substring) {
        return getContainersBy((name) -> name.contains(substring));
    }
    public ArrayList<Container> getContainersByAnySubstring(String... substrings) {
        return getContainersBy((name) -> {
            for (String s : substrings) {
                if (name.contains(s)) return true;
            }
            return false;
        });
    }
    public ArrayList<Container> getContainersBySubstrings(String... substrings) {
        return getContainersBy((name) -> {
            for (String s : substrings) {
                if (!name.contains(s)) return false;
            }
            return true;
        });
    }

    public Rectangle getRect(String name) { return getRCont(name).getRect(); }
    public Data setRect(String name, HasRect<?> rect) { getRCont(name).setRect(rect.getRect()); return this; }

    public static void throwUnknownElement(String name) { throw FError.New("unknown element (%s)", name); }
    public static void throwUnknownElement(String name, String msg) { throw FError.New("%s: unknown element (%s)", msg, name); }

    public Data modifyBoundsAndReset(String name, float fontRatio, Consumer<FRectangle> f) {
        Named<?> elem = elems.get(name);
        if (elem == null) throwUnknownElement(name);
        float fontSize = (float)(FGUI.getBaseFontSize(screenSize) * fontRatio);
        switch (getElemType(elem)) {
            case TYPE_CONTAINER: {
                RContainer<Container> rcontainer = getRCont(elem, name);
                f.accept(rcontainer.rect.resetRectBySize(screenSize));
                rcontainer.resetBounds()
                          .getContainer()
                          .setFont(font.deriveFont(fontSize));
            } break;
            case TYPE_OLIST: {
                FOList olist = getOList(elem, name);
                f.accept(olist.getFRect().resetRectBySize(screenSize));
                Container container = olist.getComboBox();
                container.setBounds(olist.getRect());
                container.setFont(font.deriveFont(fontSize));
            } break;
            case TYPE_TEXTBOX: {
                FTextbox textbox = getTextbox(elem, name);
                f.accept(textbox.getFRect().resetRectBySize(screenSize));
                textbox.setFontRatio(fontRatio)
                       .rectResize();
            } break;
            default: throwUnknownType(getElemType(elem));
        }
        return this;
    }

    public <T extends Container> Data addActionListenerToCheckbox(T parent, String name, String elemName, BiConsumer<JCheckBox, String> onAction) {
        JCheckBox checkbox = new JCheckBox(name);
        checkbox.setBackground(background);
        checkbox.setForeground(foreground);

        checkbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAction.accept(checkbox, name);
            }
        });
        checkbox.setVisible(false);
        parent.add(checkbox);
        put(elemName, checkbox);
        return this;
    }
    public Data addActionListenerToCheckbox(String name, String elemName, BiConsumer<JCheckBox, String> onAction) {
        return this.<JFrame>addActionListenerToCheckbox(frame, name, elemName, onAction);
    }
}
