package minion;

import java.util.ArrayList;

public class Disciple extends Minion {
	public Disciple(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	private Disciple(Disciple minion) {
		super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
				minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
	}

	@Override
	public Minion copy() {
		return new Disciple(this);
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
	 * God's Plan ability adds two health points to an allied minion
	 * @param target minion
	 */
	@Override
	public void specialAbilityMinion(Minion target) {
		target.setHealth(target.getHealth() + 2);
	}
}
