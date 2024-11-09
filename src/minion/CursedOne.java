package minion;

import java.util.ArrayList;

public class CursedOne extends Minion{
	public CursedOne(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	private CursedOne(CursedOne minion) {
		super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
				minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
	}

	@Override
	public Minion copy() {
		return new CursedOne(this);
	}

	@Override
	public boolean isAllowedFront() {
		return false;
	}

	@Override
	public boolean isAllowedBack() {
		return true;
	}


	/**
	 * Shapeshift ability swaps health and attack damage
	 * @param target minion
	 */
	@Override
	public void specialAbilityMinion(Minion target) {
		int tmp = target.getAttackDamage();
		target.setAttackDamage(target.getHealth());
		target.setHealth(tmp);
	}
}
