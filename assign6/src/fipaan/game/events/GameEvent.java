package fipaan.game.events;

import fipaan.game.elems.*;

public class GameEvent {
    public final EventType type;
    public final Hero source;
    public final Hero target;
    public final int damage;
    public final int targetHealthAfter;
    public final String message;

    public GameEvent(EventType type, Hero source, Hero target,
                     int damage, int targetHealthAfter, String message) {
        this.type              = type;
        this.source            = source;
        this.target            = target;
        this.damage            = damage;
        this.targetHealthAfter = targetHealthAfter;
        this.message           = message;
    }
}
