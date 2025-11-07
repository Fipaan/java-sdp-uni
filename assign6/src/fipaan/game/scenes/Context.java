package fipaan.game.scenes;

import fipaan.com.console.style.*;
import fipaan.com.console.*;
import fipaan.com.printf.*;
import fipaan.com.console.*;
import fipaan.com.utils.*;
import fipaan.com.errors.*;
import fipaan.com.wrapper.*;
import fipaan.com.has.*;
import fipaan.com.*;
import java.util.*;
import java.io.IOException;

public class Context implements HasSize<Context>, HasLocation<Context> {
    public Console console;
    public int input               = -1;
    public FPoint cursor           = new FPoint(10, 3);
    public FPoint cursorOld        = new FPoint(10, 3);
    public final int    FPS        = 60;
    public final double FPS_DT     = 1.0 / 60;
    public final int    MIN_WIDTH  = 100;
    public final int    MIN_HEIGHT = 20;
    public ArrayList<Scene> scenes = new ArrayList<>();
    public Scene mainScene         = null;
    public Context() {
        console = new ConsoleBuilder()
                   .setCursorVisible(false)
                   .setEcho(false)
                   .setRawMode(true)
                   .setHiddenMode(true)
                 .build();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            console
            .setCursorVisible(true)
            .setHiddenMode(false)
            .setRawMode(false)
            .setEcho(true);
        }));
    }
    public Scene findScene(String name) {
        int length = scenes.size();
        for (int i = 0; i < length; ++i) {
            Scene scene = scenes.get(i);
            if (name.equals(scene.name)) return scene;
        }
        return null;
    }
    private Scene expectScene(String name) {
        Scene scene = findScene(name);
        if (scene == null) throw FError.New("Couldn't find \"%s\" scene", name);
        return scene;
    }
    public Context addScene(Scene scene) { scenes.add(scene); return this; }
    public Context setMainScene(String name) {
        mainScene = expectScene(name);
        return this;
    }
    
    public Context updateConsoleSize() { console.updateConsoleSize(); return this; }
    public boolean available() throws IOException { return console.getIn().available() > 0; }
    public Context readOne() throws IOException { input = console.getIn().read(); return this; }
    public boolean isResized() { return console.isResized(); }
    public Context saveOldSize() { console.saveOldSize(); return this; }
    public Context flushInvisibleCursor() {
        console.setCursorVisible(false).flushOut();
        return this;
    }
    public Context onClick() {
        if (getWidth() >= MIN_WIDTH && getHeight() >= MIN_HEIGHT) {
            mainScene.applyContext(this).onClick();
        }
        return this;
    }
    public Context onResize() {
        if (getWidth() >= MIN_WIDTH && getHeight() >= MIN_HEIGHT) {
            mainScene.applyContext(this).onClick();
        } else {
            String msg = String.format("Too small screen, you need at least %dx%d", MIN_WIDTH, MIN_HEIGHT);
            console
           .eraseStoredLines()
           .oldClearScreen()
           .applyStyles(ConsoleColor16.BackRed, ConsoleColor16.ForeWhite)
           .fillBorders('#')
           .putStr(msg, (getWidth() - msg.length()) / 2, (getHeight() - 1) / 2)
           .applyStyles(ConsoleColor16.defaultBackAndFore)
           .flushOut();
        }
        return this;
    }
    public Context beginDrawing() {
        if (getWidth() >= MIN_WIDTH && getHeight() >= MIN_HEIGHT) {
            mainScene.applyContext(this).beginDrawing();
        }
        return this;
    }
    public Context endDrawing() {
        if (getWidth() >= MIN_WIDTH && getHeight() >= MIN_HEIGHT) {
            mainScene.applyContext(this).endDrawing();
        }
        return this;
    }
    public boolean isCursorMoved() {
        return !cursor.eqPos(cursorOld);
    }
    public Context updateOldCursor() {
        cursorOld.setLocation(cursor);
        return this;
    }

    public int getX() { return cursor.getX(); }
    public int getY() { return cursor.getY(); }
    public Context setX(int x) { cursor.setX(x); return this; }
    public Context setY(int y) { cursor.setY(y); return this; }

    public int getWidth()  { return console.getWidth(); }
    public int getHeight() { return console.getHeight(); }
    public Context setWidth(int w)  { console.setWidth(w); return this; }
    public Context setHeight(int h) { console.setHeight(h); return this; }
    public Context displayArea(Area area) {
        area.displayArea(console);
        return this;
    }
}
