package fipaan.game;

import fipaan.com.*;
import fipaan.com.console.*;
import fipaan.com.geom.wrapper.*;
import fipaan.com.utils.*;
import java.io.IOException;

public class Main {
    private static Console console = new ConsoleBuilder(
                                      .setCursorVisible(false)
                                      .setEcho(false)
                                      .setRawMode(true)
                                      .setHiddenMode(true)
                                    .build();
    private static int input = -1;
    private static FPoint cursor    = new FPoint(10, 3);
    private static FPoint cursorOld = new FPoint(10, 3);
    private static final int    FPS    = 60;
    private static final double FPS_DT = 1.0 / 60;

    private static void redrawCursor() {
        console
        .putStr(ConsoleColor16.BackRed.colored(" "), cursorOld.x, cursorOld.y)
        .putStr(ConsoleColor16.BackWhite.colored("*"), cursor.x, cursor.y)
        .flushOut();
        cursorOld.setLocation(cursor);
    }
    private static void handleInput() {
        switch (input) {
            case 'w': cursor.y -= 1; break;
            case 's': cursor.y += 1; break;
            case 'a': cursor.x -= 1; break;
            case 'd': cursor.x += 1; break;
        }
        if (!cursor.eqPos(cursorOld)) {
            redrawCursor();
        }
    }

    private static void onResize() {
        console
        .eraseSavedLines()
        .clearScreen()
        .fillBorders('#')
        .flushOut();
        redrawCursor();
    }
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            console
            .setCursorVisible(true)
            .setHiddenMode(false)
            .setRawMode(false)
            .setEcho(true);
        }));
        while (true) {
            long start = System.nanoTime();
            console.updateConsoleSize();
            while (console.getIn().available() > 0) {
                input = console.getIn().read();
                if (input == 'q') return;
                handleInput();
            }
            if (console.isResized()) {
                console.saveOldSize();
                onResize();
            }
            long end = System.nanoTime();
            long duration = end - start;
            double duration_s = duration / 1_000_000.0;
            if (FPS_DT > duration_s) ProcessUtils.sleep(FPS_DT - duration_s);
        }
    }
}
