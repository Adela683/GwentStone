package hero;

import minion.Minion;

import java.util.ArrayList;

public class KingMudface extends Hero {
	public KingMudface(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked) {
		super(mana, health, description, colors, name, hasAttacked);
	}

	/**
	 * Earth Born increases health by 1
	 * @param targetRow affected row
	 */
	@Override
	public void specialAbilityHero(Minion[] targetRow) {
		for (Minion minion : targetRow) {
			if (minion != null) {
				minion.setHealth(minion.getHealth() + 1);
			}
		}
	}
}
