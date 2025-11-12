package fipaan.game;

import fipaan.game.elems.*;
import fipaan.game.events.*;
import fipaan.game.observer.*;
import fipaan.game.scenes.*;
import fipaan.game.strategy.*;

import fipaan.com.console.*;
import fipaan.com.printf.*;
import fipaan.com.console.*;
import fipaan.com.utils.*;
import fipaan.com.errors.*;
import fipaan.com.wrapper.*;
import fipaan.com.array.*;
import fipaan.com.*;
import java.util.*;
import java.io.IOException;

public class App {
    public Context context;

    public void run() throws IOException {
        while (true) {
            long start = System.nanoTime();
            context.updateConsoleSize();
            context.beginDrawing();
            while (context.available()) {
                context.readOne();
                if (context.input == 'q') return;
                context.onClick();
            }
            if (context.isResized()) {
                context
               .saveOldSize()
               .onResize()
               .flushInvisibleCursor();
            }
            context.endDrawing();
            long end = System.nanoTime();
            long duration = end - start;
            double duration_s = duration / 1_000_000.0;
            if (context.FPS_DT > duration_s) ProcessUtils.sleep(context.FPS_DT - duration_s);
        }
    }

    public App() {
          context = new Context()
         .addScene(new MenuScene("Main Menu"))
         .setMainScene("Main Menu");
    }
}
