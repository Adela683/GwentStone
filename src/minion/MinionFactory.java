package minion;

import fileio.CardInput;

import java.util.ArrayList;

public abstract class MinionFactory {
    /**
     * Create a Minion from a CardInput. Minion type is determined by name
     * @param cardInput original input
     * @return minion
     */
    public static Minion createMinionFromCardInput(final CardInput cardInput) {
        int mana = cardInput.getMana();
        int health = cardInput.getHealth();
        String description = cardInput.getDescription();
        ArrayList<String> colors = cardInput.getColors();
        String name = cardInput.getName();
        int attackDamage = cardInput.getAttackDamage();
        switch (name) {
            case "Sentinel":
                return new Sentinel(mana, health, description, colors, name, false, attackDamage);
            case "Berserker":
                return new Berserker(mana, health, description, colors, name, false, attackDamage);
            case "Goliath":
                return new Goliath(mana, health, description, colors, name, false, attackDamage);
            case "Warden":
                return new Warden(mana, health, description, colors, name, false, attackDamage);
            case "Miraj":
                return new Miraj(mana, health, description, colors, name, false, attackDamage);
            case "The Ripper":
                return new Ripper(mana, health, description, colors, name, false, attackDamage);
            case "The Cursed One":
                return new CursedOne(mana, health, description, colors, name, false, attackDamage);
            case "Disciple":
                return new Disciple(mana, health, description, colors, name, false, attackDamage);
            default:
                return null;
        }
    }
}
