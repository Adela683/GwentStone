package minion;

import card.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Minion extends Card {
	private int attackDamage;
	private boolean isFrozen;
	private ObjectMapper objectMapper = new ObjectMapper();

	public Minion(int mana, int health, String description, ArrayList<String> colors, String name, boolean hasAttacked, int attackDamage) {
		super(mana, health, description, colors, name, hasAttacked);
		this.attackDamage = attackDamage;
		this.isFrozen = false;
	}

	public ObjectNode getMinionAsObjectNode() {
		ObjectNode minionObjectNode = objectMapper.createObjectNode();
		minionObjectNode.put("mana", getMana());
		minionObjectNode.put("attackDamage", getAttackDamage());
		minionObjectNode.put("health", getHealth());
		minionObjectNode.put("description", getDescription());

		ArrayNode colorsNode = objectMapper.createArrayNode();
		for (String color : getColors()) {
			colorsNode.add(color);
		}

		minionObjectNode.set("colors", colorsNode);
		minionObjectNode.put("name", getName());

		return minionObjectNode;
	}


}
