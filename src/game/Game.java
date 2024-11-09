package game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import lombok.Getter;
import player.Player;
import constants.Constants;

public class Game {
	private Player player1;
	private Player player2;
	private int manaRound = 0;
	private int currentPlayer;
	@Getter
	private ArrayNode gameOutput;
	private final ObjectMapper mapper = new ObjectMapper();
	private boolean isRoundStart;
	private GameTable gameTable;

	public Game(Player player1, Player player2, int currentPlayer) {
		this.player1 = player1;
		this.player2 = player2;
		this.currentPlayer = currentPlayer;
		isRoundStart = true;
		gameOutput = mapper.createArrayNode();
		gameTable = new GameTable();
	}

	public void executeCommand(ActionsInput actionsInput) {
		if (isRoundStart) {
			prepareRound();
		}

		Player player = getCurrentPlayer();

		// Check if a debugging/statistics command was given
		switch (actionsInput.getCommand()) {
			case Constants.GET_PLAYER_DECK:
				gameOutput.add(getPlayerDeck(actionsInput));
				return;
			case Constants.GET_PLAYER_HERO:
				gameOutput.add(getPlayerHero(actionsInput));
				return;
			case Constants.GET_PLAYER_TURN:
				gameOutput.add(getPlayerTurn());
				return;
			case Constants.END_PLAYER_TURN:
				endPlayerTurn();
				return;
			case Constants.GET_CARDS_IN_HAND:
				gameOutput.add(getCardsInHand(actionsInput));
				return;
			case Constants.GET_PLAYER_MANA:
				gameOutput.add(getPlayerMana(actionsInput));
				return;
			case Constants.GET_CARDS_ON_TABLE:
				gameOutput.add(getCardsOnTable());
				return;
			case Constants.GET_CARD_AT_POSITION:
				gameOutput.add(getCardAtPosition(actionsInput));
				return;
		}

		// Check if a normal game command was given
		ObjectNode result;
		switch (actionsInput.getCommand()) {
			case Constants.PLACE_CARD:
				if (actionsInput.getHandIdx() < player.getCardsInHand().size()) {
					result = GameCommands.placeCard(player, player.getCardsInHand().get(actionsInput.getHandIdx()), actionsInput.getHandIdx(), gameTable);
					if (!result.isEmpty()) {
						gameOutput.add(result);
					}
				}
				return;
			case Constants.CARD_USES_ATTACK:
				result = setUpMinionAttack(actionsInput);
				if (!result.isEmpty()) {
					gameOutput.add(result);
				}
				return;
			case Constants.CARD_USES_SPECIAL_ABILITY:
				result = setUpMinionSpecial(actionsInput);
				if (!result.isEmpty()) {
					gameOutput.add(result);
				}
				return;
		}
	}

	private void prepareRound() {
		if (manaRound < 10) {
			manaRound++;
		}

		player1.setMana(player1.getMana() + manaRound);
		player2.setMana(player2.getMana() + manaRound);

		if (!player1.getCurrentCardPacket().isEmpty()) {
			player1.getCardsInHand().add(player1.getCurrentCardPacket().pop());
		}
		if (!player2.getCurrentCardPacket().isEmpty()) {
			player2.getCardsInHand().add(player2.getCurrentCardPacket().pop());
		}

		isRoundStart = false;
	}

	private ObjectNode getCardAtPosition(ActionsInput actionsInput) {
		return gameTable.getMinionOnPosition(actionsInput.getX(), actionsInput.getY());
	}

	/**
	 * End the current's player turn
	 */
	private void endPlayerTurn() {
		if (currentPlayer == 1) {
			player1.setDone(true);
			gameTable.resetMinionStats(1, 0);
			currentPlayer = 2;
		} else {
			player2.setDone(true);
			gameTable.resetMinionStats(2, 3);
			currentPlayer = 1;
		}

		if (check_end_round()) {
			isRoundStart = true;
			player1.setDone(false);
			player2.setDone(false);
		}
	}

	/**
	 * Check if a round has ended
	 */
	private boolean check_end_round() {
		return player1.isDone() && player2.isDone();
	}

	private ObjectNode getPlayerDeck(ActionsInput actionsInput) {
		int playerIndex = actionsInput.getPlayerIdx();

		ObjectNode objectNode;
		if (playerIndex == 1) {
			objectNode = player1.getDeckAsObjectNode(playerIndex);
		} else {
			objectNode = player2.getDeckAsObjectNode(playerIndex);
		}

		return objectNode;
	}

	private ObjectNode getCardsInHand(ActionsInput actionsInput) {
		int playerIndex = actionsInput.getPlayerIdx();

		ObjectNode objectNode;
		if (playerIndex == 1) {
			objectNode = player1.getCardsInHandAsObjectNode(playerIndex);
		} else {
			objectNode = player2.getCardsInHandAsObjectNode(playerIndex);
		}

		return objectNode;
	}

	private ObjectNode getCardsOnTable() {
		ObjectNode result = mapper.createObjectNode();
		result.put("command", Constants.GET_CARDS_ON_TABLE);

		ArrayNode firstPlayerFront = gameTable.getRowAsArrayNode(1);
		ArrayNode firstPlayerBack = gameTable.getRowAsArrayNode(0);

		ArrayNode secondPlayerFront = gameTable.getRowAsArrayNode(2);
		ArrayNode secondPlayerBack = gameTable.getRowAsArrayNode(3);

		ArrayNode combinedCards = mapper.createArrayNode();

		combinedCards.add(secondPlayerBack);
		combinedCards.add(secondPlayerFront);
		combinedCards.add(firstPlayerFront);
		combinedCards.add(firstPlayerBack);

		result.set("output", combinedCards);
		return result;
	}

	private ObjectNode setUpMinionAttack(ActionsInput actionsInput) {
		Player attackerPlayer = getCurrentPlayer();
		Player attackedPlayer = getNextPlayer();

		int x_attacker = actionsInput.getCardAttacker().getX();
		int y_attacker = actionsInput.getCardAttacker().getY();
		int x_attacked = actionsInput.getCardAttacked().getX();
		int y_attacked = actionsInput.getCardAttacked().getY();
		return GameCommands.attackMinion(attackerPlayer, attackedPlayer, x_attacker, y_attacker, x_attacked, y_attacked, gameTable);
	}

	private ObjectNode setUpMinionSpecial(ActionsInput actionsInput) {
		Player attackerPlayer = getCurrentPlayer();
		Player attackedPlayer = getNextPlayer();

		int x_attacker = actionsInput.getCardAttacker().getX();
		int y_attacker = actionsInput.getCardAttacker().getY();
		int x_attacked = actionsInput.getCardAttacked().getX();
		int y_attacked = actionsInput.getCardAttacked().getY();
		return GameCommands.specialAbilityMinion(attackerPlayer, attackedPlayer, x_attacker, y_attacker, x_attacked, y_attacked, gameTable);
	}

	private ObjectNode getPlayerHero(ActionsInput actionsInput) {
		int playerIndex = actionsInput.getPlayerIdx();

		ObjectNode objectNode;
		if (playerIndex == 1) {
			objectNode = player1.getHero().getHeroAsObjectNode(playerIndex);
		} else {
			objectNode = player2.getHero().getHeroAsObjectNode(playerIndex);
		}

		return objectNode;
	}

	private ObjectNode getPlayerMana(ActionsInput actionsInput) {
		int playerIndex = actionsInput.getPlayerIdx();

		ObjectNode objectNode;
		if (playerIndex == 1) {
			objectNode = player1.getManaAsObjectNode(playerIndex);
		} else {
			objectNode = player2.getManaAsObjectNode(playerIndex);
		}

		return objectNode;
	}

	private ObjectNode getPlayerTurn() {
		ObjectNode node = mapper.createObjectNode();
		node.put("command", Constants.GET_PLAYER_TURN);
		node.put("output", currentPlayer);

		return node;
	}

	private Player getCurrentPlayer() {
		if (currentPlayer == 1) {
			return player1;
		}
		return player2;
	}

	private Player getNextPlayer() {
		if (currentPlayer == 1) {
			return player2;
		}
		return player1;
	}
}
