package minion;

import java.util.ArrayList;

public class Goliath extends Minion {
	public Goliath(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	@Override
	public void specialAbility() {

	}
}
