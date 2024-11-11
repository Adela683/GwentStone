package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import lombok.Getter;
import lombok.Setter;
import minion.Minion;
import player.Player;

@Getter
@Setter
public final class GameTable {
    private Minion[][] gameTable = new Minion[Constants.TABLE_ROWS][Constants.TABLE_COLUMNS];
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Returns a specific minion as an object node
     * @param x minion position
     * @param y minion position
     * @return minion or error message
     */
    public ObjectNode getMinionOnPosition(final int x, final int y) {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", Constants.GET_CARD_AT_POSITION);
        result.put("x", x);
        result.put("y", y);

        Minion minion = gameTable[Constants.ROW_OFFSET - x][y];
        if (minion == null) {
            result.put("output", Constants.NO_CARD_AT_POSITION);
            return result;
        }

        ObjectNode objectNodeMinion = minion.getMinionAsObjectNode();
        result.set("output", objectNodeMinion);
        return result;
    }

    /**
     * Return an entire row of cards as an ArrayNode
     * @param x row
     * @return row as ObjectNode
     */
    public ArrayNode getRowAsArrayNode(final int x) {
        ArrayNode node = mapper.createArrayNode();

        for (Minion minion : gameTable[x]) {
            if (minion != null) {
                ObjectNode minionObject = minion.getMinionAsObjectNode();
                node.add(minionObject);
            }
        }

        return node;
    }

    /**
     * Make an ArrayNode with all frozen minions
     * @return all frozen minions from top to bottom row
     */
    public ArrayNode getFrozenMinionsAsArrayNode() {
        ArrayNode node = mapper.createArrayNode();

        for (int i = gameTable.length - 1; i >= 0; i--) {
            for (int j = 0; j < gameTable[0].length; j++) {
                if (gameTable[i][j] != null && gameTable[i][j].isFrozen()) {
                    ObjectNode minionObject = gameTable[i][j].getMinionAsObjectNode();
                    node.add(minionObject);
                }
            }
        }

        return node;
    }

    /**
     * Unfreeze and let minions attack again
     * @param frontRow of player
     * @param backRow of player
     */
    public void resetMinionStats(final int frontRow, final int backRow) {
        for (Minion minion : gameTable[frontRow]) {
            if (minion != null) {
                minion.setFrozen(false);
                minion.setHasAttacked(false);
            }
        }

        for (Minion minion : gameTable[backRow]) {
            if (minion != null) {
                minion.setFrozen(false);
                minion.setHasAttacked(false);
            }
        }
    }

    /**
     * Get the front row for a player
     * @param player use ID to get front row
     * @return front row
     */
    public Minion[] getFrontRow(final Player player) {
        if (player.getPlayerId() == 1) {
            return gameTable[Constants.FIRST_PLAYER_FRONT];
        }
        return gameTable[Constants.SECOND_PLAYER_FRONT];
    }

    /**
     * Get the back row for a player
     * @param player use ID to get back row
     * @return back row
     */
    public Minion[] getBackRow(final Player player) {
        if (player.getPlayerId() == 1) {
            return gameTable[Constants.FIRST_PLAYER_BACK];
        }
        return gameTable[Constants.SECOND_PLAYER_BACK];
    }
}
