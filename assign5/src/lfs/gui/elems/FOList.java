package lfs.gui.elems;

import lfs.gui.wrapper.*;
import lfs.gui.has.*;
import lfs.gui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.function.Consumer;
import java.lang.Runnable;

public class FOList implements HasRect<FOList> {
    private DefaultComboBoxModel<String> model;
    public DefaultComboBoxModel<String> getComboBoxModel() { return model; }
    public FOList setComboBoxModel(DefaultComboBoxModel<String> m) { model = m; return this; }
    
    private RContainer<JComboBox<String>> comboBox;
    public JComboBox<String> getComboBox() { return comboBox.obj; }
    public FOList setComboBox(JComboBox<String> box) { comboBox.obj = box; return this; }
    public FRectangle getFRect() { return comboBox.rect; }
    public FOList setFRect(FRectangle rect) { comboBox.rect = rect; return this; }

    public FOList setSelectedItem(String item) { getComboBox().setSelectedItem(item); return this; }
    public FOList setSelectedIndex(int index) { getComboBox().setSelectedIndex(index); return this; }
    public FOList setVisible(boolean b) { getComboBox().setVisible(b); return this; }
    public boolean isVisible() { return getComboBox().isVisible(); }

    public FOList modifyBoundsAndReset(Dimension parentSize, Consumer<FRectangle> f) {
        FGUI.modifyBoundsAndReset(parentSize, comboBox, f);
        return this;
    }

    public Object getSelectedItem() {
        return getComboBox().getSelectedItem();
    }
    public FOList addActionListener(ActionListener l) {
        getComboBox().addActionListener(l);
        return this;
    }
    public int getSelectedIndex() {
        return getComboBox().getSelectedIndex();
    }

    private String noneText;
    public String getNoneText() { return noneText; }
    public FOList setNoneText(String text) {
        model.removeElementAt(0);
        model.insertElementAt(text, 0);
        noneText = text;
        return this;
    }
    
    public FOList(Container parent, Consumer<String> onSelect, Runnable onNoneSelect, String noneText) {
        model    = new DefaultComboBoxModel<>();
        comboBox = new RContainer(new JComboBox<>(model));
        this.noneText = noneText;
        model.addElement(noneText);
        
        // Add ActionListener to respond to selection
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getSelectedIndex() == 0) {
                    if (onNoneSelect != null) onNoneSelect.run();
                } else {
                    String selected = (String) getSelectedItem();
                    onSelect.accept(selected);
                }
            }
        });

        parent.add(comboBox.obj);
    }
    public FOList addElement(String e) { model.addElement(e); return this; }
    public FOList removeElement(String e) { model.removeElement(e); return this; }

    // Interfaces
    public int getWidth()  { return comboBox.getWidth();  }
    public int getHeight() { return comboBox.getHeight(); }
    public int getX()      { return comboBox.getX();      }
    public int getY()      { return comboBox.getY();      }

    public FOList setWidth(int w)  { comboBox.setWidth(w);  return this; }
    public FOList setHeight(int h) { comboBox.setHeight(h); return this; }
    public FOList setX(int x)      { comboBox.setX(x);      return this; }
    public FOList setY(int y)      { comboBox.setY(y);      return this; }
}
