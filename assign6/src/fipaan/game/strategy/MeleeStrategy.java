package fipaan.game.strategy;

import fipaan.com.*;
import fipaan.game.elems.*;

public class MeleeStrategy implements AttackStrategy {
    public  static final int   BASE_DAMAGE     = 8;
    public  static final int    MAX_DAMAGE     = 16;
    private static final int   DIFF_DAMAGE     = MAX_DAMAGE - BASE_DAMAGE + 1;
    private static final float CRIT_CHANCE     = 0.4f;
    private static final float SCALE_FOR_TANK  = 0.8f;
    private static final int   TANK_HEALTH_MIN = 80;

    public int computeDamage(Hero attacker, Hero target) {
        int base = BASE_DAMAGE + Global.random.nextInt(DIFF_DAMAGE);
        if (Global.random.nextDouble() < CRIT_CHANCE) base *= 2;
        if (attacker.getHealth() > TANK_HEALTH_MIN) base *= SCALE_FOR_TANK;
        return base;
    }

    public String getType() { return "Melee"; }
}

