package fipaan.game;

import fipaan.com.console.*;
import fipaan.com.printf.*;
import fipaan.com.console.*;
import fipaan.com.*;

public class App {
    public Console console;
    public void run() {
        FormattedObj[] res = Sscanf.sscanf("[%d;%dH", "[3;3H");
        ConsoleBuffer buf = new ConsoleBuffer(console);
        Global.info.printfn("res.length() = %d", res.length);
    }

    public App() {
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
}
