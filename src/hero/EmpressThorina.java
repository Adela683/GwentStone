package hero;

import minion.Minion;

import java.util.ArrayList;

public class EmpressThorina extends Hero {
    public EmpressThorina(final int mana, final int health, final String description,
                          final ArrayList<String> colors, final String name,
                          final boolean hasAttacked) {
        super(mana, health, description, colors, name, hasAttacked);
    }

    /**
     * Low blow ability kills the highest health minion on the row
     * @param targetRow affected row
     */
    @Override
    public void specialAbilityHero(final Minion[] targetRow) {
        int maxHealth = -1;
        int indexMaxHealth = -1;

        for (int i = 0; i < targetRow.length; i++) {
            if (targetRow[i] != null && targetRow[i].getHealth() > maxHealth) {
                maxHealth = targetRow[i].getHealth();
                indexMaxHealth = i;
            }
        }

        targetRow[indexMaxHealth].setHealth(-1);
    }
}
