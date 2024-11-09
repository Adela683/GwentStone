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
	private int playerOneMovesCurrentRound = 0;
	private int playerTwoMovesCurrentRound = 0;
	private boolean isRoundStart;

	public Game(Player player1, Player player2, int currentPlayer) {
		this.player1 = player1;
		this.player2 = player2;
		this.currentPlayer = currentPlayer;
		isRoundStart = true;
		gameOutput = mapper.createArrayNode();
	}

	public void executeCommand(ActionsInput actionsInput) {
		if (isRoundStart) {
			prepareRound();
		}

		Player player;
		if (currentPlayer == 1) {
			player = player1;
		} else {
			player = player2;
		}

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
		}

		// Check if a normal game command was given
		switch (actionsInput.getCommand()) {
			case Constants.PLACE_CARD:
				if (actionsInput.getHandIdx() < player.getCardsInHand().size()) {
					ObjectNode result = GameCommands.placeCard(player, player.getCardsInHand().get(actionsInput.getHandIdx()), actionsInput.getHandIdx());
					if (!result.isEmpty()) {
						gameOutput.add(result);
					}
				}
				return;
		}
	}

	private void prepareRound() {
		if (manaRound < 10) {
			manaRound++;
		}

		playerOneMovesCurrentRound = 0;
		playerTwoMovesCurrentRound = 0;
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

	/**
	 * End the current's player turn
	 */
	private void endPlayerTurn() {
		if (currentPlayer == 1) {
			player1.setDone(true);
			currentPlayer = 2;
		} else {
			player2.setDone(true);
			currentPlayer = 1;
		}

		if (check_end_round()) {
			isRoundStart = true;
			player1.setDone(false);
			player2.setDone(false);
			playerOneMovesCurrentRound = 0;
			playerTwoMovesCurrentRound = 0;
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

		ArrayNode firstPlayerFront = player1.getFrontRowAsArrayNode();
		ArrayNode firstPlayerBack = player1.getBackRowAsArrayNode();

		ArrayNode secondPlayerFront = player2.getFrontRowAsArrayNode();
		ArrayNode secondPlayerBack = player2.getBackRowAsArrayNode();

		ArrayNode combinedCards = mapper.createArrayNode();

		combinedCards.add(secondPlayerBack);
		combinedCards.add(secondPlayerFront);
		combinedCards.add(firstPlayerFront);
		combinedCards.add(firstPlayerBack);

		result.set("output", combinedCards);
		return  result;
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
}
