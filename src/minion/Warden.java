package minion;

import java.util.ArrayList;

public final class Warden extends Minion {
    public Warden(final int mana, final int health, final String description,
                  final ArrayList<String> colors, final String name,
                  final boolean hasAttacked, final int attackDamage) {
        super(mana, health, description, colors, name, hasAttacked, attackDamage);
    }

    private Warden(final Warden minion) {
        super(minion.getMana(), minion.getHealth(), minion.getDescription(),
                minion.getColors(), minion.getName(), minion.isHasAttacked(),
                minion.getAttackDamage());
    }

    @Override
    public Minion copy() {
        return new Warden(this);
    }

    @Override
    public boolean isAllowedFront() {
        return true;
    }

    @Override
    public boolean isTank() {
        return true;
    }
}
