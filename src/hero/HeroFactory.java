package hero;

import constants.Constants;
import fileio.CardInput;

import java.util.ArrayList;

public abstract class HeroFactory {

    /**
     * Create a Hero from a CardInput. Hero type is determined by name
     * @param cardInput original input
     * @return hero
     */
    public static Hero createHeroFromCardInput(final CardInput cardInput) {
        int mana = cardInput.getMana();
        int health = Constants.HERO_START_HEALTH;
        String description = cardInput.getDescription();
        ArrayList<String> colors = cardInput.getColors();
        String name = cardInput.getName();

        switch (name) {
            case "Lord Royce":
                return new LordRoyce(mana, health, description, colors, name, false);
            case "Empress Thorina":
                return new EmpressThorina(mana, health, description, colors, name, false);
            case "King Mudface":
                return new KingMudface(mana, health, description, colors, name, false);
            case "General Kocioraw":
                return new GeneralKocioraw(mana, health, description, colors, name, false);
            default:
                return null;
        }
    }


}
