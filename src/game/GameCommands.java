package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import minion.Minion;
import player.Player;

public abstract class GameCommands {
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Check if a card can be placed on the table and place it if possible
	 *
	 * @param player    player that wants to place a card
	 * @param minion    card to be placed
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
	 * Attack another player's minion. Before attacking, check all conditions:
	 * 1. Minions are on different teams
	 * 2. Attacker has not attacked before in this round
	 * 3. Attacker is not frozen
	 * 4. If enemy has a tank, the tank must be attacked first.
	 * If attacked card dies, remake the row
	 * @param attackerPlayer attacks a minion
	 * @param attackedPlayer gets attacked
	 * @param x_attacker row of attacker
	 * @param y_attacker column of attacker
	 * @param x_attacked row of attacked card
	 * @param y_attacked column of attacked card
	 * @return in case of error, error details, otherwise an empty ObjectNode
	 */
	public static ObjectNode attackMinion(Player attackerPlayer, Player attackedPlayer,
										 int x_attacker, int y_attacker, int x_attacked, int y_attacked) {
		ObjectNode result = mapper.createObjectNode();
		result.put("command", Constants.CARD_USES_ATTACK);

		ObjectNode cardAttacker = mapper.createObjectNode();
		cardAttacker.put("x", x_attacker);
		cardAttacker.put("y", y_attacker);
		result.set("cardAttacker", cardAttacker);

		ObjectNode cardAttacked = mapper.createObjectNode();
		cardAttacked.put("x", x_attacked);
		cardAttacked.put("y", y_attacked);
		result.set("cardAttacked", cardAttacked);

		if (isSameSide(x_attacker, x_attacked)) {
			result.put("error", Constants.CARD_ON_SAME_SIDE_ERROR);
			return result;
		}

		Minion[] minionAttackerRow = getMinionRow(attackerPlayer, x_attacker);
		Minion[] minionAttackedRow = getMinionRow(attackedPlayer, x_attacked);
		Minion minionAttacker = minionAttackerRow[y_attacker];
		Minion minionAttacked = minionAttackedRow[y_attacked];

		if (minionAttacker == null || minionAttacked == null) {
			return result.removeAll();
		}

		if (minionAttacker.isHasAttacked()) {
			result.put("error", Constants.CARD_HAS_ATTACKED_ERROR);
			return result;
		}

		if (minionAttacker.isFrozen()) {
			result.put("error", Constants.CARD_IS_FROZEN);
			return result;
		}

		if (checkEnemyTank(attackedPlayer) && !minionAttacked.isTank()) {
			result.put("error", Constants.ENEMY_HAS_TANK);
			return result;
		}

		minionAttacked.setHealth(minionAttacked.getHealth() - minionAttacker.getAttackDamage());
		minionAttacker.setHasAttacked(true);

		// if minion died, remove it from table
		if (minionAttacked.getHealth() <= 0) {
			minionAttackedRow[y_attacked] = null;
			moveCardsLeft(minionAttackedRow, y_attacked);
		}

		return result.removeAll();
	}

	/**
	 * Move all minions after the killed minion one position to the left
	 * @param minionRow row of killed minion
	 * @param killedMinion index
	 */
	private static void moveCardsLeft(Minion[] minionRow, int killedMinion) {
		for (int i = killedMinion; i < minionRow.length - 1; i++) {
			minionRow[i] = minionRow[i + 1];
		}
		minionRow[minionRow.length - 1] = null;
	}

	/**
	 * Check if the enemy player has a tank on the front row
	 * @param enemy player
	 * @return true if enemy has tank
	 */
	private static boolean checkEnemyTank(Player enemy) {
		for (Minion minion : enemy.getFrontRow()) {
			if (minion != null && minion.isTank()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if a new card can be placed on row
	 *
	 * @param minionRow check if it has enough space
	 * @return true if there is enough place on row
	 */
	private static boolean hasSpace(Minion[] minionRow) {
		for (Minion minion : minionRow) {
			if (minion == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if two cards belong to the same player
	 *
	 * @param x1 first card row
	 * @param x2 second card row
	 * @return true if two cards are on the same side
	 */
	private static boolean isSameSide(int x1, int x2) {
		return (x1 <= 1 && x2 <= 1) || (x1 >= 2 && x2 >= 2);
	}

	/**
	 * Get a player's minion row
	 *
	 * @param player    to get minion row from
	 * @param minionRow index of row
	 * @return a minion array
	 */
	private static Minion[] getMinionRow(Player player, int minionRow) {
		if (minionRow == 3 || minionRow == 0) {
			return player.getBackRow();
		}
		return player.getFrontRow();
	}
}
