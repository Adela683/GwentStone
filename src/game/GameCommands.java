package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import minion.Minion;
import player.Player;

public abstract class GameCommands {
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Check if a card can be placed on the table and place it if possible
	 * @param player player that wants to place a card
	 * @param minion card to be placed
	 * @param handIndex index of card
	 * @return error message or an empty node, if operation was successful
	 */
	public static ObjectNode placeCard(Player player, Minion minion, int handIndex) {
		ObjectNode node = mapper.createObjectNode();
		node.put("command", Constants.PLACE_CARD);
		node.put("handIdx", handIndex);
		Minion[] minionsRow;

		if (minion.isAllowedFront()) {
			minionsRow = player.getFrontRow();
		} else {
			minionsRow = player.getBackRow();
		}

		if (minion.getMana() > player.getMana()) {
			node.put("error", Constants.NOT_ENOUGH_MANA_PLACE);
			return node;
		}

		if (!hasSpace(minionsRow)) {
			node.put("error", Constants.ROW_FULL_PLACE);
			return node;
		}

		// place the card
		for (int i = 0; i < minionsRow.length; i++) {
			if (minionsRow[i] == null) {
				minionsRow[i] = minion;
				break;
			}
		}

		player.getCardsInHand().remove(handIndex);
		player.setMana(player.getMana() - minion.getMana());
		return node.removeAll();
	}

	/**
	 * Check if a new card can be placed on row
	 * @param minionRow check if it has enough space
	 * @return true/false
	 */
	private static boolean hasSpace(Minion[] minionRow) {
		for (Minion minion : minionRow) {
			if (minion == null) {
				return true;
			}
		}
		return false;
	}
}
