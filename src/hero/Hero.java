package hero;

import card.Card;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import minion.Minion;

import java.util.ArrayList;

public abstract class Hero extends Card {

    public Hero(final int mana, final int health, final String description,
                final ArrayList<String> colors, final String name,
                final boolean hasAttacked) {
        super(mana, health, description, colors, name, hasAttacked);
    }

    /**
     * Get the hero as an ObjectNode
     * @param playerIndex required in output for clarity
     * @return player's hero in ObjectNode format
     */
    public ObjectNode getHeroAsObjectNode(final int playerIndex) {
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

    /**
     * Special ability for heroes
     * @param targetRow minions affected
     */
    public void specialAbilityHero(final Minion[] targetRow) {

    }
}
