package minion;

import java.util.ArrayList;

public class Ripper extends Minion {
	public Ripper(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	private Ripper(Ripper minion) {
		super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
				minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
	}

	@Override
	public Minion copy() {
		return new Ripper(this);
	}

	@Override
	public boolean isAllowedFront() {
		return true;
	}

	@Override
	public boolean isAllowedBack() {
		return false;
	}

	/**
	 * Weak Knees ability removes two attack points from target. Attack is capped at 0.
	 * @param minion target
	 */
	@Override
	public void specialAbilityMinion(Minion minion) {
		minion.setAttackDamage(Math.max(minion.getAttackDamage() - 2, 0));
	}
}
