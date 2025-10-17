package lfs.gui.elems.builder;

import lfs.errors.FError;
import lfs.gui.elems.*;
import lfs.print.*;
import java.util.function.Consumer;
import java.awt.*;
import javax.swing.*;

public class FTextboxBuilder {
    private Container parent;
    private Dimension screenSize;
    private int textFieldColumns;
    private Color foregroundText;
    private String hintMessage;
    private Color foregroundHint;
    private String buttonMessage;
    private Consumer<String> onSubmit;
    private float textFieldPerc;
    private float hintBorderX;
    private float hintBorderY;
    private float buttonPerc;
    private Font f;
    private float fontRatio;

    public FTextboxBuilder() {}
    public FTextboxBuilder setParent(Container parent, Dimension size) {
        this.parent = parent;
        screenSize  = size;
        return this;
    }
    public FTextboxBuilder setTextField(int columns, Color foreground, float perc) {
        textFieldColumns = columns;
        foregroundText = foreground;
        textFieldPerc = perc;
        return this;
    }
    public FTextboxBuilder setButton(String message, float perc, Consumer<String> onSubmit) {
        buttonMessage = message;
        this.onSubmit = onSubmit;
        buttonPerc = perc;
        return this;
    }
    public FTextboxBuilder setHint(String message, Color foreground) {
        hintMessage = message;
        foregroundHint = foreground;
        return this;
    }
    public FTextboxBuilder setHintBorder(float px, float py) {
        hintBorderX = px;
        hintBorderY = py;
        return this;
    }
    public FTextboxBuilder setFont(Font f, float fontRatio) {
        this.f = f;
        this.fontRatio = fontRatio;
        return this;
    }

    private void verifyZero(int num, String name)    { if (num == 0)    throw FError.New(name + " is not specified"); } 
    private void verifyZero(float num, String name)  { if (num == 0.0f) throw FError.New(name + " is not specified"); } 
    private void verifyNull(Object obj, String name) { if (obj == null) throw FError.New(name + " is not specified"); } 
    public FTextbox build() {
        verifyNull(parent, "Parent frame");
        verifyNull(screenSize, "Screen size");
        verifyZero(textFieldColumns, "Columns count");
        verifyNull(foregroundText, "Text color");
        verifyNull(hintMessage, "Hint message");
        verifyNull(foregroundHint, "Hint color");
        verifyNull(buttonMessage, "Button message");
        verifyNull(onSubmit, "onSubmit callback");
        verifyZero(textFieldPerc, "Text field width");
        verifyZero(buttonPerc, "Button width");
        verifyNull(f, "Font");
        return new FTextbox(parent, screenSize,
                    textFieldColumns, foregroundText,
                    hintMessage, foregroundHint,
                    buttonMessage, onSubmit,
                    textFieldPerc,
                    hintBorderX, hintBorderY,
                    buttonPerc,
                    f, fontRatio);
    }
}
