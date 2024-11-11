package minion;

import java.util.ArrayList;

public final class Ripper extends Minion {
    public Ripper(final int mana, final int health, final String description,
                  final ArrayList<String> colors, final String name,
                  final boolean hasAttacked, final int attackDamage) {
        super(mana, health, description, colors, name, hasAttacked, attackDamage);
    }

    private Ripper(final Ripper minion) {
        super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
                minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
    }

    @Override
    public Minion copy() {
        return new Ripper(this);
    }

    @Override
    public boolean isAllowedFront() {
        return true;
    }

    /**
     * Weak Knees ability removes two attack points from target. Attack is capped at 0.
     * @param minion target
     */
    @Override
    public void specialAbilityMinion(final Minion minion) {
        minion.setAttackDamage(Math.max(minion.getAttackDamage() - 2, 0));
    }
}
