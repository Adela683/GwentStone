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
public class GameTable {
	private Minion[][] gameTable = new Minion[4][5];
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Returns a specific minion as an object node
	 * @param x minion position
	 * @param y minion position
	 * @return minion or error message
	 */
	public ObjectNode getMinionOnPosition(int x, int y) {
		ObjectNode result = mapper.createObjectNode();
		result.put("command", Constants.GET_CARD_AT_POSITION);
		result.put("x", x);
		result.put("y", y);

		Minion minion = gameTable[3 - x][y];
		if (minion == null) {
			result.put("output", Constants.NO_CARD_AT_POSITION);
			return result;
		}

		ObjectNode objectNodeMinion = minion.getMinionAsObjectNode();
		result.set("output", objectNodeMinion);
		return result;
	}

	public ArrayNode getRowAsArrayNode(int x) {
		ArrayNode node = mapper.createArrayNode();

		for (Minion minion : gameTable[x]) {
			if (minion != null) {
				ObjectNode minionObject = minion.getMinionAsObjectNode();
				node.add(minionObject);
			}
		}

		return node;
	}

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

	public void resetMinionStats(int frontRow, int backRow) {
		for (Minion minion : gameTable[frontRow]){
			if (minion != null) {
				minion.setFrozen(false);
				minion.setHasAttacked(false);
			}
		}

		for (Minion minion : gameTable[backRow]){
			if (minion != null) {
				minion.setFrozen(false);
				minion.setHasAttacked(false);
			}
		}
	}

	public Minion[] getFrontRow(Player player) {
		if (player.getPlayerId() == 1) {
			return gameTable[1];
		}
		return gameTable[2];
	}

	public Minion[] getBackRow(Player player) {
		if (player.getPlayerId() == 1) {
			return gameTable[0];
		}
		return gameTable[3];
	}
}
