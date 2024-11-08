package hero;

import fileio.CardInput;

public class HeroFactory {

	public static Hero createHeroFromCardInput(CardInput cardInput, String heroType) {
		switch (heroType) {
			case "Lord Royce":
				return new LordRoyce(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
			case "Empress Thorina":
				return new EmpressThorina(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
			case "King Mudface":
				return new KingMudface(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
			case "General Kocioraw":
				return new GeneralKocioraw(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false);
		}
		return null;
	}


}
