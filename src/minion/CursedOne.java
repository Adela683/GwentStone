package minion;

import java.util.ArrayList;

public final class CursedOne extends Minion {
    public CursedOne(final int mana, final int health, final String description,
                     final ArrayList<String> colors, final String name,
                     final boolean hasAttacked, final int attackDamage) {
        super(mana, health, description, colors, name, hasAttacked, attackDamage);
    }

    private CursedOne(final CursedOne minion) {
        super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
                minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
    }

    @Override
    public Minion copy() {
        return new CursedOne(this);
    }

    @Override
    public boolean isAllowedFront() {
        return false;
    }

    /**
     * Shapeshift ability swaps health and attack damage
     * @param target minion
     */
    @Override
    public void specialAbilityMinion(final Minion target) {
        int tmp = target.getAttackDamage();
        target.setAttackDamage(target.getHealth());
        target.setHealth(tmp);
    }
}
