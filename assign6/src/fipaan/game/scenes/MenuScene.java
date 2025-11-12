package fipaan.game.scenes;

import fipaan.com.console.*;
import fipaan.com.printf.*;
import fipaan.com.console.*;
import fipaan.com.utils.*;
import fipaan.com.errors.*;
import fipaan.com.has.*;
import fipaan.com.wrapper.*;
import fipaan.com.*;

public class MenuScene extends Scene {
    private Area dude = new Area(3, 3,
                " [] ",
                "/TT⧵",
                " /⧵ "
             // "*   ",
             // "    ",
             // "    "
            );
    public MenuScene(String name) { super(name); }

    private void redrawCursor() {
        dude.pos.setLocation(context.cursor);
        context.console
        // .displayArea(dude)
        .flushOut();
        context.updateOldCursor();
    }

    @Override
    public Scene onClick() {
        switch (context.input) {
            case 'w': subY(1); break;
            case 's': addY(1); break;
            case 'a': subX(1); break;
            case 'd': addX(1); break;
        }
        if (context.isCursorMoved()) {
            redrawCursor();
        }
        return this;
    }
    @Override
    public Scene onResize() {
        int backWidth = 76;
        int backHeight = 16;
        int backX = (context.getWidth()  - backWidth)  / 2;
        int backY = (context.getHeight() - backHeight) / 2;
        context.console
        .eraseStoredLines()
        .clearScreen()
        .fillBorders('#')
        .putStr("                                                                   __       ",            backX, backY +  0)
        .putStr("             /\\        /\\      /\\           /\\       /\\          _/  \\_     ",      backX, backY +  1)
        .putStr("    /\\      /  \\  /\\  /  \\    /  \\   /\\    /  \\  /\\ /  \\   /\\   /      \\    ", backX, backY +  2)
        .putStr("   /  \\    /    \\/  \\/    \\  /    \\ /  \\  /    \\/  \\    \\ /  \\ /        \\   ", backX, backY +  3)
        .putStr("  /    \\  /                \\/      \\    \\/          \\    \\    /          \\  ",     backX, backY +  4)
        .putStr("_/      \\/                               \\           \\    \\__/            \\_",       backX, backY +  5)
        .putStr("                                                                            ",            backX, backY +  6)
        .putStr("                     /\\             /\\                                      ",          backX, backY +  7)
        .putStr("        /\\          /  \\  /\\       /  \\       /\\                            ",       backX, backY +  8)
        .putStr("       /  \\  /\\    /    \\/  \\     /    \\  /\\ /  \\  /\\                       ",    backX, backY +  9)
        .putStr("      /    \\/  \\  /          \\___/      \\/  \\    \\/  \\                      ",     backX, backY + 10)
        .putStr("_____/          \\/                       \\        \\___________________      ",         backX, backY + 11)
        .putStr("                                                                            ",            backX, backY + 12)
        .putStr("                         ~  ~  ~  ~  ~  ~  ~  ~                             ",            backX, backY + 13)
        .putStr("                ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~                          ",            backX, backY + 14)
        .putStr("~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~  ~                     ",            backX, backY + 15)
        .putStr(" [] ", getX(), getY())
        .putStr("/TT⧵", getX(), getY() + 1)
        .putStr(" /⧵ ", getX(), getY() + 2)
        // .applyBack(dude)
        .flushOut();
        redrawCursor();
        return this;
    }
    @Override public Scene beginDrawing() {
        return this;
    }
    @Override public Scene endDrawing() {
        return this;
    }
}
