package minion;

import java.util.ArrayList;

public final class Miraj extends Minion {
    public Miraj(final int mana, final int health, final String description,
                 final ArrayList<String> colors, final String name,
                 final boolean hasAttacked, final int attackDamage) {
        super(mana, health, description, colors, name, hasAttacked, attackDamage);
    }

    private Miraj(final Miraj minion) {
        super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
                minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
    }

    @Override
    public Minion copy() {
        return new Miraj(this);
    }

    @Override
    public boolean isAllowedFront() {
        return true;
    }

    /**
     * Skyjack ability swaps health with target
     * @param minion target
     */
    @Override
    public void specialAbilityMinion(final Minion minion) {
        int tmp = getHealth();
        setHealth(minion.getHealth());
        minion.setHealth(tmp);
    }
}
