package minion;

import java.util.ArrayList;

public class Disciple extends Minion {
	public Disciple(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked, attackDamage);
	}

	@Override
	public void specialAbility() {

	}
}
