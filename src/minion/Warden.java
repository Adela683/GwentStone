package minion;

import java.util.ArrayList;

public class Warden extends Minion {
	public Warden(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	@Override
	public boolean isAllowedFront() {
		return true;
	}

	@Override
	public boolean isAllowedBack() {
		return false;
	}

	@Override
	public void specialAbility() {

	}
}
