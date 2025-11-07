package fipaan.game.observer;

import fipaan.game.events.*;
import fipaan.game.Local;

public class ConsoleObserver implements GameObserver {
    @Override public void onGameEvent(GameEvent event) {
        if (event.type == null) {
            Local.event.printfn("%s - %s", event.type, event.message);
        } else {
            Local.event.printfn("%s", event.type);
        }
    }
}
