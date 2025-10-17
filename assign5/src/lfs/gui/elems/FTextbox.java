package lfs.gui.elems;

import lfs.gui.has.*;
import lfs.gui.wrapper.*;
import lfs.gui.*;
import lfs.print.*;
import lfs.errors.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;


public class FTextbox implements HasTextField<FTextbox>, HasHint<FTextbox>, HasButton<FTextbox>, HasRect<FTextbox>, HasFont<FTextbox> {
    public static final float DEFAULT_TF_PERC    = 0.7f;
    public static final float DEFAULT_HB_PERC    = 0.05f;
    public static final float DEFAULT_FONT_RATIO = 0.9f;

    private JLayeredPane layeredPane;

    private RContainer<JTextField> textField;
    public JTextField getTextField() { return textField.obj; }
    public FTextbox setTextField(JTextField tf) { Todo.New("who owns it"); return this; }
    private float textFieldPerc;
    public float getTextFieldPerc() { return textFieldPerc; }
    public FTextbox setTextFieldPerc(float perc) { textFieldPerc = perc; return this; }

    public FTextbox setText(String text) { getTextField().setText(text); return this; }

    private RContainer<JLabel> hint;
    public JLabel getHint() { return hint.obj; }
    public FTextbox setHint(JLabel hint) { Todo.New("who owns it"); return this; }
    private float hintBorderX, hintBorderY;
    public float getHintBorderX() { return hintBorderX; }
    public FTextbox setHintBorderX(float perc) { hintBorderX = perc; return this; }
    public float getHintBorderY() { return hintBorderY; }
    public FTextbox setHintBorderY(float perc) { hintBorderY = perc; return this; }
    public FTextbox setHintBorder(float percX, float percY) { hintBorderX = percX; hintBorderY = percY; return this; }

    private RContainer<JButton> button;
    public JButton getButton() { return button.obj; }
    public FTextbox setButton(JButton btn) { Todo.New("who owns it"); return this; }
    private float buttonPerc;
    public float getButtonPerc() { return buttonPerc; }
    public FTextbox setButtonPerc(float perc) { buttonPerc = perc; return this; }
 
    private FRectangle rect;
    public Rectangle getRect() { return rect.getRect(); }
    public FTextbox setRect(Rectangle rect) { this.rect.setRect(rect); rectResize(); return this; }
    
    private Font baseFont;
    public Font getFont() { return baseFont; }
    public FTextbox setFont(Font font) { baseFont = font; return this; }
    private float fontRatio;
    public float getFontRatio() { return fontRatio; }
    public FTextbox setFontRatio(float ratio) { fontRatio = ratio; return this; }
    public float getFontSize() { return (float)(FGUI.getBaseFontSize(screenSize) * fontRatio); }
    public FTextbox updateFontSize() {
        float fontSize = getFontSize();
        textField.setFont(deriveFont(fontSize));
        hint.setFont(deriveFont(fontSize));
        button.setFont(deriveFont(fontSize));
        return this;
    }

    public FTextbox(Container parent, Dimension screenSize,
                    int textFieldColumns, Color foregroundText,
                    String hintMessage, Color foregroundHint,
                    String buttonMessage, Consumer<String> onSubmit,
                    float textFieldPerc,
                    float hintBorderX, float hintBorderY,
                    float buttonPerc,
                    Font baseFont, float fontRatio) {
        textField = new RContainer<>(new JTextField(textFieldColumns));
        textField.obj.setForeground(foregroundText);
        hint      = new RContainer<>(new JLabel(hintMessage));
        hint.obj.setForeground(foregroundHint);
        hint.obj.setOpaque(false);
        hint.obj.setVerticalAlignment(SwingConstants.TOP);
        hint.obj.setHorizontalAlignment(SwingConstants.LEFT);

        textField.obj.getDocument().addDocumentListener(new DocumentListener() {
            private void checkEmpty() {
                hint.setVisible(textField.obj.getText().trim().isEmpty());
            }

            @Override
            public void insertUpdate(DocumentEvent e) { checkEmpty(); }
            @Override
            public void removeUpdate(DocumentEvent e) { checkEmpty(); }
            @Override
            public void changedUpdate(DocumentEvent e) { checkEmpty(); }
        });

        button    = new RContainer<>(new JButton(buttonMessage));

        button.obj.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSubmit.accept(textField.obj.getText());
            }
        });

        this.rect          = new FRectangle();
        this.textFieldPerc = textFieldPerc;
        this.hintBorderX   = hintBorderX;
        this.hintBorderY   = hintBorderY;
        this.buttonPerc    = buttonPerc;
        this.baseFont      = baseFont;
        this.fontRatio     = fontRatio;
        this.screenSize    = screenSize;

        layeredPane = new JLayeredPane();
        parent.add(layeredPane);
        layeredPane.add(textField.obj, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(hint.obj, JLayeredPane.PALETTE_LAYER);
        layeredPane.add(button.obj, JLayeredPane.DEFAULT_LAYER);
    }

    private void rectResize() {
        layeredPane.setBounds(getRect());
        rawRectResizeTextField();
        rawRectResizeHint();
        rawRectResizeButton();
    }
    private <T extends Container> void rawRectModifyBounds(RContainer<T> comp, Consumer<FRectangle> f) {
        f.accept(comp.rect.setRect(getRect()));
        FGUI.resetBounds(comp);
    }
    private <T extends Container> void modifyBoundsAndReset(RContainer<T> comp, Consumer<FRectangle> f) {
        f.accept(comp.rect.setLocation(0, 0).setSize(getSize()));
        FGUI.resetBounds(comp);
    }

    private void rawRectResizeTextField() {
        modifyBoundsAndReset(textField, rect -> rect
            .scaleW(textFieldPerc)
        );
        textField.setFont(deriveFont(getFontSize()));
    }
    private void rawRectResizeHint() {
        modifyBoundsAndReset(hint, rect -> rect
            .scaleW(textFieldPerc)
            .phcut(hintBorderX, hintBorderY)
        );
        hint.setFont(deriveFont(getFontSize()));
    }
    private void rawRectResizeButton() {
        modifyBoundsAndReset(button, rect -> rect
            .scaleW(buttonPerc, true)
        );
        button.setFont(deriveFont(getFontSize()));
    }

    private Dimension screenSize;
    public Dimension getScreenSize() { return screenSize; }
    public FTextbox setScreenSize(Dimension size) { screenSize = size; return this; }

    public FTextbox modifyBoundsAndReset(Consumer<FRectangle> f) {
        setLocation(screenSize).setSize(screenSize);
        f.accept(rect);
        rectResize();
        return this;
    }

    // Interfaces
    public int getWidth()  { return rect.getWidth();  }
    public int getHeight() { return rect.getHeight(); }
    public int getX()      { return rect.getX();      }
    public int getY()      { return rect.getY();      }

    public FTextbox setWidth(int w)  { rect.setWidth(w);  return this; }
    public FTextbox setHeight(int h) { rect.setHeight(h); return this; }
    public FTextbox setX(int x)      { rect.setX(x);      return this; }
    public FTextbox setY(int y)      { rect.setY(y);      return this; }
}
