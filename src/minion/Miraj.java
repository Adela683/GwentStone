package minion;

import java.util.ArrayList;

public class Miraj extends Minion{
	public Miraj(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	private Miraj(Miraj minion) {
		super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
				minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
	}

	@Override
	public Minion copy() {
		return new Miraj(this);
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
	 * Skyjack ability swaps health with target
	 * @param minion target
	 */
	@Override
	public void specialAbilityMinion(Minion minion) {
		int tmp = getHealth();
		setHealth(minion.getHealth());
		minion.setHealth(tmp);
	}
}
