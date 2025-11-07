package fipaan.game.observer;

import fipaan.game.events.*;

public interface GameObserver {
    void onGameEvent(GameEvent event);
}
