package hero;

import minion.Minion;

import java.util.ArrayList;

public class LordRoyce extends Hero {
	public LordRoyce(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked) {
		super(mana, health, description, colors, name, hasAttacked);
	}

	@Override
	public void specialAbilityHero(Minion[] targetRow) {

	}
}
