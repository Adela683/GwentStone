package minion;

import java.util.ArrayList;

public final class Disciple extends Minion {
    public Disciple(final int mana, final int health, final String description,
                    final ArrayList<String> colors, final String name,
                    final boolean hasAttacked, final int attackDamage) {
        super(mana, health, description, colors, name, hasAttacked, attackDamage);
    }

    private Disciple(final Disciple minion) {
        super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
                minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
    }

    @Override
    public Minion copy() {
        return new Disciple(this);
    }

    @Override
    public boolean isAllowedFront() {
        return false;
    }

    /**
     * God's Plan ability adds two health points to an allied minion
     * @param target minion
     */
    @Override
    public void specialAbilityMinion(final Minion target) {
        target.setHealth(target.getHealth() + 2);
    }
}
