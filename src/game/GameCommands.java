package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import constants.Constants;
import lombok.Getter;
import lombok.Setter;
import minion.Minion;
import player.Player;

public abstract class GameCommands {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static int cm = 0;

	/**
	 * Check if a card can be placed on the table and place it if possible
	 *
	 * @param player    player that wants to place a card
	 * @param minion    card to be placed
	 * @param handIndex index of card
	 * @param gameTable access the game table
	 * @return error message or an empty node, if operation was successful
	 */
	public static ObjectNode placeCard(Player player, Minion minion, int handIndex, GameTable gameTable) {
		ObjectNode node = mapper.createObjectNode();
		node.put("command", Constants.PLACE_CARD);
		node.put("handIdx", handIndex);
		Minion[] minionsRow;

		if (minion.isAllowedFront()) {
			minionsRow = gameTable.getFrontRow(player);
		} else {
			minionsRow = gameTable.getBackRow(player);
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
	 *
	 * @param attackerPlayer attacks a minion
	 * @param attackedPlayer gets attacked
	 * @param x_attacker     row of attacker
	 * @param y_attacker     column of attacker
	 * @param x_attacked     row of attacked card
	 * @param y_attacked     column of attacked card
	 * @param gameTable      access the game table
	 * @return in case of error, error details, otherwise an empty ObjectNode
	 */
	public static ObjectNode attackMinion(Player attackerPlayer, Player attackedPlayer,
										  int x_attacker, int y_attacker, int x_attacked, int y_attacked,
										  GameTable gameTable) {
		ObjectNode result = mapper.createObjectNode();
		result.put("command", Constants.CARD_USES_ATTACK);

		makeAttackDetails(x_attacker, y_attacker, x_attacked, y_attacked, result);

		if (isSameSide(x_attacker, x_attacked)) {
			result.put("error", Constants.CARD_ON_SAME_SIDE_ERROR);
			return result;
		}

		Minion[] minionAttackerRow = gameTable.getGameTable()[3 - x_attacker];
		Minion[] minionAttackedRow = gameTable.getGameTable()[3 - x_attacked];
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

		if (checkEnemyTank(gameTable.getFrontRow(attackedPlayer)) && !minionAttacked.isTank()) {
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
	 * Use a minion's special ability. Before using, check all conditions:
	 * 1. Minion is not frozen
	 * 2. Minion has not attacked this round
	 * 3. If minion is Disciple -> check if attacked minion is on the same team and use ability. Stop here if this works.
	 * 4. If minion is Ripper, Miraj, Cursed One -> check if attacked minion belongs to the enemy
	 * 5. Check if enemy has a tank.
	 * If attacked card dies, remake the row
	 *
	 * @param attackerPlayer minion that uses the ability
	 * @param attackedPlayer gets attacked
	 * @param x_attacker     row of attacker
	 * @param y_attacker     column of attacker
	 * @param x_attacked     row of attacked card
	 * @param y_attacked     column of attacked card
	 * @return in case of error, error details, otherwise an empty ObjectNode
	 */
	public static ObjectNode specialAbilityMinion(Player attackerPlayer, Player attackedPlayer,
												  int x_attacker, int y_attacker, int x_attacked, int y_attacked,
												  GameTable gameTable) {
		ObjectNode result = mapper.createObjectNode();
		result.put("command", Constants.CARD_USES_SPECIAL_ABILITY);

		makeAttackDetails(x_attacker, y_attacker, x_attacked, y_attacked, result);
		Minion[] minionAttackerRow = gameTable.getGameTable()[3 - x_attacker];
		Minion[] minionAttackedRow = gameTable.getGameTable()[3 - x_attacked];
		Minion minionAttacker = minionAttackerRow[y_attacker];
		Minion minionAttacked = minionAttackedRow[y_attacked];

		if (minionAttacker == null || minionAttacked == null) {
			return result.removeAll();
		}

		if (minionAttacker.isFrozen()) {
			result.put("error", Constants.CARD_IS_FROZEN);
			return result;
		}

		if (minionAttacker.isHasAttacked()) {
			result.put("error", Constants.CARD_HAS_ATTACKED_ERROR);
			return result;
		}

		// if minion is disciple and target is on the same side, heal.
		if (minionAttacker.getName().equals("Disciple") && isSameSide(x_attacker, x_attacked)) {
			minionAttacker.specialAbilityMinion(minionAttacked);
			minionAttacker.setHasAttacked(true);
			return result.removeAll();
		} else if (minionAttacker.getName().equals("Disciple")) {
			result.put("error", Constants.CARD_NOT_ON_SAME_SIDE_ERROR);
			return result;
		}

		if (isSameSide(x_attacker, x_attacked)) {
			result.put("error", Constants.CARD_ON_SAME_SIDE_ERROR);
			return result;
		}

		if (checkEnemyTank(gameTable.getFrontRow(attackedPlayer)) && !minionAttacked.isTank()) {
			result.put("error", Constants.ENEMY_HAS_TANK);
			return result;
		}

		minionAttacker.specialAbilityMinion(minionAttacked);
		minionAttacker.setHasAttacked(true);

		// if minion died, remove it from table
		if (minionAttacked.getHealth() <= 0) {
			minionAttackedRow[y_attacked] = null;
			moveCardsLeft(minionAttackedRow, y_attacked);
		}

		if (minionAttacker.getHealth() <= 0) {
			minionAttackerRow[y_attacker] = null;
			moveCardsLeft(minionAttackerRow, y_attacker);
		}

		return result.removeAll();
	}

	private static void makeAttackDetails(int x_attacker, int y_attacker, int x_attacked, int y_attacked, ObjectNode result) {
		ObjectNode cardAttacker = mapper.createObjectNode();
		cardAttacker.put("x", x_attacker);
		cardAttacker.put("y", y_attacker);
		result.set("cardAttacker", cardAttacker);

		ObjectNode cardAttacked = mapper.createObjectNode();
		cardAttacked.put("x", x_attacked);
		cardAttacked.put("y", y_attacked);
		result.set("cardAttacked", cardAttacked);
	}

	/**
	 * Move all minions after the killed minion one position to the left
	 *
	 * @param minionRow    row of killed minion
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
	 *
	 * @param frontRow might contain tanks
	 * @return true if enemy has tank
	 */
	private static boolean checkEnemyTank(Minion[] frontRow) {
		for (Minion minion : frontRow) {
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
}
