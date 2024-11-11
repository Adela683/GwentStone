package minion;

import card.Card;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public abstract class Minion extends Card {
    private int attackDamage;
    private boolean isFrozen;

    public Minion(final int mana, final int health, final String description,
                  final ArrayList<String> colors, final String name,
                  final boolean hasAttacked, final int attackDamage) {
        super(mana, health, description, colors, name, hasAttacked);
        this.attackDamage = attackDamage;
        this.isFrozen = false;
    }

    /**
     * Checks if a particular minion type is allowed on the front line.
     * @return true if a minion is allowed on the front line
     */
    public abstract boolean isAllowedFront();

    /**
     * Checks if a minion is of type tank. By default, return false.
     * @return true if minion is tank.
     */
    public boolean isTank() {
        return false;
    }

    /**
     * Make a deep copy of a minion.
     * @return deep copy of minion.
     */
    public abstract Minion copy();

    /**
     * Get a minion in ObjectNode format.
     * @return minion as ObjectNode.
     */
    public ObjectNode getMinionAsObjectNode() {
        ObjectNode minionObjectNode = getMapper().createObjectNode();
        minionObjectNode.put("mana", getMana());
        minionObjectNode.put("attackDamage", getAttackDamage());
        minionObjectNode.put("health", getHealth());
        minionObjectNode.put("description", getDescription());

        ArrayNode colorsNode = getMapper().createArrayNode();
        for (String color : getColors()) {
            colorsNode.add(color);
        }

        minionObjectNode.set("colors", colorsNode);
        minionObjectNode.put("name", getName());

        return minionObjectNode;
    }

    /**
     * Override for minions that have a special ability
     * @param target minion
     */
    public void specialAbilityMinion(final Minion target) {

    }
}
