package game;

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
	private boolean isRoundStart = true;

	public Game(Player player1, Player player2, int currentPlayer) {
		this.player1 = player1;
		this.player2 = player2;
		this.currentPlayer = currentPlayer;
		gameOutput = mapper.createArrayNode();
	}

	public void executeCommand(ActionsInput actionsInput) {
		if (isRoundStart) {
			prepareRound();
		}
		// Check debbuging commands
		switch (actionsInput.getCommand()) {
			case Constants.GET_PLAYER_DECK: gameOutput.add(getPlayerDeck(actionsInput));
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
}
