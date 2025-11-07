package fipaan.game.events;

import fipaan.game.elems.*;
import fipaan.com.errors.*;

public class GameEventBuilder {
    private EventType type = null;
    private Hero source = null;
    private Hero target = null;
    private int damage = 0;
    private int health = -1;
    private String message = null;

    public GameEventBuilder() {}

    public GameEventBuilder setType(EventType et)  { type    = et;   return this; }
    public GameEventBuilder setSource(Hero src)    { source  = src;  return this; }
    public GameEventBuilder setTarget(Hero hero)   { target  = hero; return this; }
    public GameEventBuilder setDamage(int dmg)     { damage  = dmg;  return this; }
    public GameEventBuilder setHealthAfter(int hp) { health  = hp;   return this; }
    public GameEventBuilder setMessage(String msg) { message = msg;  return this; }
    public GameEventBuilder setMessage(String fmt, Object... args) { message = String.format(fmt, args); return this; }
    public GameEventBuilder setMessage(Object msg) { return setMessage(msg.toString()); }

    public GameEvent build() {
        FError.verifyNotNull(type, "event type");
        return new GameEvent(type, source, target, damage, health, message);
    }
}
