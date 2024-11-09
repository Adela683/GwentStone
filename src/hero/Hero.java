package hero;

import card.Card;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;

import java.util.ArrayList;

public abstract class Hero extends Card {

	public Hero(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked) {
		super(mana, health, description, colors, name, hasAttacked);
	}

	public ObjectNode getHeroAsObjectNode(int playerIndex) {
		ObjectNode heroNode = getMapper().createObjectNode();
		heroNode.put("command", Constants.GET_PLAYER_HERO);
		heroNode.put(Constants.PLAYER_IDX, playerIndex);

		ObjectNode heroObject = getMapper().createObjectNode();
		heroObject.put("mana", getMana());
		heroObject.put("description", getDescription());

		ArrayNode colorsNode = getMapper().createArrayNode();
		for (String color : getColors()) {
			colorsNode.add(color);
		}

		heroObject.set("colors", colorsNode);
		heroObject.put("name", getName());
		heroObject.put("health", getHealth());

		heroNode.set("output", heroObject);
		return heroNode;
	}
}
