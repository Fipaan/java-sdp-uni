package fipaan.game.strategy;

import fipaan.com.*;
import fipaan.game.elems.*;

public class MagicStrategy implements AttackStrategy {
    public  static final int   BASE_DAMAGE     = 7;
    public  static final int    MAX_DAMAGE     = 19;
    private static final int   DIFF_DAMAGE     = MAX_DAMAGE - BASE_DAMAGE + 1;
    private static final float CRIT_CHANCE     = 0.05f;

    public int computeDamage(Hero attacker, Hero target) {
        int base = BASE_DAMAGE + Global.random.nextInt(DIFF_DAMAGE);
        if (Global.random.nextDouble() < CRIT_CHANCE) base *= 2;
        return base;
    }

    public String getType() { return "Magic"; }
}

