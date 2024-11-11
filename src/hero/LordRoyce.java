package hero;

import minion.Minion;

import java.util.ArrayList;

public class LordRoyce extends Hero {
    public LordRoyce(final int mana, final int health, final String description,
                     final ArrayList<String> colors, final String name,
                     final boolean hasAttacked) {
        super(mana, health, description, colors, name, hasAttacked);
    }

    /**
     * Sub-Zero ability freezes all minions on the row
     * @param targetRow affected row
     */
    @Override
    public void specialAbilityHero(final Minion[] targetRow) {
        for (Minion minion : targetRow) {
            if (minion != null) {
                minion.setFrozen(true);
            }
        }
    }
}
