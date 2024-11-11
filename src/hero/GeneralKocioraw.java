package hero;

import minion.Minion;

import java.util.ArrayList;

public class GeneralKocioraw extends Hero {

    public GeneralKocioraw(final int mana, final int health, final String description,
                           final ArrayList<String> colors, final String name,
                           final boolean hasAttacked) {
        super(mana, health, description, colors, name, hasAttacked);
    }

    /**
     * Blood Thirst ability increases attack damage by 1
     * @param targetRow affected row
     */
    @Override
    public void specialAbilityHero(final Minion[] targetRow) {
        for (Minion minion : targetRow) {
            if (minion != null) {
                minion.setAttackDamage(minion.getAttackDamage() + 1);
            }
        }
    }
}
