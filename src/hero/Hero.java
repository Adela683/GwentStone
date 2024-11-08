package hero;

import card.Card;

import java.util.ArrayList;

public abstract class Hero extends Card {

	public Hero(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked) {
		super(mana, health, description, colors, name, hasAttacked);
	}
}
