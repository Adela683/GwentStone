package minion;

import fileio.CardInput;

public class MinionFactory {

	public static Minion createMinionFromCardInput(CardInput cardInput, String minionType) {
		switch (minionType) {
			case "Sentinel":
				return new Sentinel(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "Berserker":
				return new Berserker(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "Goliath":
				return new Goliath(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "Warden":
				return new Warden(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "Miraj":
				return new Miraj(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "The Ripper":
				return new Ripper(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "The Cursed One":
				return new CursedOne(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
			case "Disciple":
				return new Disciple(cardInput.getMana(), cardInput.getHealth(), cardInput.getDescription(), cardInput.getColors(), cardInput.getName(), false, cardInput.getAttackDamage());
		}
		return null;
	}


}
