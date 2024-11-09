package minion;

import java.util.ArrayList;

public class Berserker extends Minion {
	public Berserker(int mana, int health, String description, ArrayList<String> colors, String name,
					 boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	private Berserker(Berserker minion) {
		super(minion.getMana(), minion.getHealth(), minion.getDescription(), minion.getColors(),
				minion.getName(), minion.isHasAttacked(), minion.getAttackDamage());
	}

	@Override
	public Minion copy() {
		return new Berserker(this);
	}

	@Override
	public boolean isAllowedFront() {
		return false;
	}

	@Override
	public boolean isAllowedBack() {
		return true;
	}

	@Override
	public void specialAbility() {

	}
}
