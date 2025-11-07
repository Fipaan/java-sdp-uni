package fipaan.game.elems;

import fipaan.game.events.*;
import fipaan.game.observer.*;
import fipaan.game.scenes.*;
import fipaan.game.strategy.*;

import fipaan.com.errors.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Hero {
    private final String name;
    private int health;
    private AttackStrategy strategy;
    private final ArrayList<GameObserver> observers = new ArrayList<>();

    public Hero(String heroName, int hp, AttackStrategy strat) {
        FError.verifyNotNull(strat, "strategy");

        name = heroName;
        health = hp;
        strategy = strat;
    }

    public String getName() { return name; }
    public String getNameSafe() { return name == null ? "(null)" : name; }
    public int getHealth() { return health; }
    public boolean isDead() { return health <= 0; }

    public void registerObserver(GameObserver o) {
        if (o != null && !observers.contains(o)) observers.add(o);
    }

    public void unregisterObserver(GameObserver o) {
        observers.remove(o);
    }

    private void notifyObservers(GameEvent e) {
        for (GameObserver o : new ArrayList<>(observers)) o.onGameEvent(e);
    }

    public void setStrategy(AttackStrategy strat) {

        String oldType = strategy.getType();
        String newType = strat.getType();
        strategy = strat;
        notifyObservers(new GameEventBuilder()
            .setType(EventType.STRATEGY_CHANGE)
            .setSource(this)
            .setMessage("%s changed strategy: %s -> %s", name, oldType, newType)
            .build()
        );
    }

    public void attack(Hero target) {
        if (this.isDead()) return;
        if (target == null || target.isDead()) return;

        int damage = strategy.computeDamage(this, target);
        target.applyDamage(damage, this);

        notifyObservers(new GameEventBuilder()
            .setType(EventType.ATTACK)
            .setSource(this)
            .setTarget(target)
            .setDamage(damage)
            .setHealthAfter(target.getHealth())
            .setMessage("%s attacks %s with %s for %d damage",
                        name, target.getName(), strategy.getType(), damage)
            .build()
        );
    }

    public void applyDamage(int damage, Hero from) {
        if (isDead()) return;
        int before = health;
        health -= damage;
        notifyObservers(new GameEventBuilder()
            .setType(EventType.HEALTH_CHANGE)
            .setSource(from)
            .setTarget(this)
            .setDamage(damage)
            .setHealthAfter(health)
            .setMessage("%s took %d damage (HP %d -> %d)",
                        name, damage, before, health)
            .build()
        );
        if (health == 0) notifyObservers(new GameEventBuilder()
            .setType(EventType.DEATH)
            .setSource(from)
            .setTarget(this)
            .setDamage(damage)
            .setHealthAfter(health)
            .setMessage("%s has died", name)
            .build()
        );
    }

    public String toString() {
        return String.format("%s(HP=%d)", name, health);
    }
}
