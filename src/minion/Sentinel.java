package minion;

import java.util.ArrayList;

public class Sentinel extends Minion{
	public Sentinel(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
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
