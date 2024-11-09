package hero;

import constants.Constants;
import fileio.CardInput;

public class HeroFactory {

	public static Hero createHeroFromCardInput(CardInput cardInput, String heroType) {
		switch (heroType) {
			case "Lord Royce":
				return new LordRoyce(cardInput.getMana(), Constants.HERO_START_HEALTH, cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
			case "Empress Thorina":
				return new EmpressThorina(cardInput.getMana(), Constants.HERO_START_HEALTH, cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
			case "King Mudface":
				return new KingMudface(cardInput.getMana(), Constants.HERO_START_HEALTH, cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
			case "General Kocioraw":
				return new GeneralKocioraw(cardInput.getMana(), Constants.HERO_START_HEALTH, cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
		}
		return null;
	}


}
