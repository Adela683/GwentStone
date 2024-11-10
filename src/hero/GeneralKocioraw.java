package hero;

import minion.Minion;

import java.util.ArrayList;

public class GeneralKocioraw extends Hero {

	public GeneralKocioraw(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked) {
		super(mana, health, description, colors, name, hasAttacked);
	}

	/**
	 * Blood Thirst ability increases attack damage by 1
	 * @param targetRow affected row
	 */
	@Override
	public void specialAbilityHero(Minion[] targetRow) {
		for (Minion minion : targetRow) {
			if (minion != null) {
				minion.setAttackDamage(minion.getAttackDamage() + 1);
			}
		}
	}
}
