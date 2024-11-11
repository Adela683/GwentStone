package hero;

import minion.Minion;

import java.util.ArrayList;

public class KingMudface extends Hero {
    public KingMudface(final int mana, final int health, final String description,
                       final ArrayList<String> colors, final String name,
                       final boolean hasAttacked) {
        super(mana, health, description, colors, name, hasAttacked);
    }

    /**
     * Earth Born increases health by 1
     * @param targetRow affected row
     */
    @Override
    public void specialAbilityHero(final Minion[] targetRow) {
        for (Minion minion : targetRow) {
            if (minion != null) {
                minion.setHealth(minion.getHealth() + 1);
            }
        }
    }
}
