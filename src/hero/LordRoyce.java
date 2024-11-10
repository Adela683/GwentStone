package hero;

import minion.Minion;

import java.util.ArrayList;

public class LordRoyce extends Hero {
	public LordRoyce(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked) {
		super(mana, health, description, colors, name, hasAttacked);
	}

	/**
	 * Sub-Zero ability freezes all minions on the row
	 * @param targetRow affected row
	 */
	@Override
	public void specialAbilityHero(Minion[] targetRow) {
		for (Minion minion : targetRow) {
			if (minion != null) {
				minion.setFrozen(true);
			}
		}
	}
}
