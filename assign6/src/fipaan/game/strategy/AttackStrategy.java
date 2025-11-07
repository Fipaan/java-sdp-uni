package fipaan.game.strategy;

import fipaan.game.elems.*;

public interface AttackStrategy {
    int computeDamage(Hero attacker, Hero target);
    String getType();
}
