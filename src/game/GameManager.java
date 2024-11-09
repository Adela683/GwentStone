package game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.GameInput;
import fileio.StartGameInput;
import hero.HeroFactory;
import lombok.Getter;
import minion.Minion;
import minion.MinionFactory;
import player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class GameManager {
	private Player player1;
	private Player player2;
	private ArrayList<GameInput> games;
	@Getter
	private ArrayNode output;

	public GameManager(Player player1, Player player2, ArrayList<GameInput> games, ArrayNode output) {
		this.player1 = player1;
		this.player2 = player2;
		this.games = games;
		this.output = output;
	}

	public void play() {
		for (GameInput game : games) {
			StartGameInput startInput = game.getStartGame();
			ArrayList<ActionsInput> gameActions = game.getActions();

			initPlayer(player1, startInput.getShuffleSeed(), startInput.getPlayerOneDeckIdx(), startInput.getPlayerOneHero());
			initPlayer(player2, startInput.getShuffleSeed(), startInput.getPlayerTwoDeckIdx(), startInput.getPlayerTwoHero());

			// Start a new game
			Game currentGame = new Game(player1, player2, startInput.getStartingPlayer());

			for (ActionsInput action : gameActions) {
				currentGame.executeCommand(action);
			}
			for (JsonNode node : currentGame.getGameOutput()) {
				output.add(node);
			}
		}
	}

	/**
	 * Prepares a player to play a game.
	 *
	 * @param player to prepare for game
	 */
	private void initPlayer(Player player, long shuffleSeed, int currentDeckIndex, CardInput hero) {
		player.setFrontRow(new Minion[5]);
		player.setBackRow(new Minion[5]);
		player.setCardsInHand(new ArrayList<>());
		player.setDone(false);
		player.setMana(0);
		player.setCurrentDeckIndex(currentDeckIndex);

		Stack<Minion> playerPacket = new Stack<>();
		Random random = new Random(shuffleSeed);

		// make a deep copy of the current deck, so we can keep the original
		ArrayList<Minion> deckPacketCopy = new ArrayList<>();
		for (Minion minion : player.getDeck().get(currentDeckIndex)) {
			Minion newMinion = minion.copy();
			deckPacketCopy.add(newMinion);
		}

		Collections.shuffle(deckPacketCopy, random);
		for (int i = player1.getCardsInDeck() - 1; i >= 0; i--) {
			playerPacket.push(deckPacketCopy.get(i));
		}

		player.setCurrentCardPacket(playerPacket);
		player.setHero(HeroFactory.createHeroFromCardInput(hero, hero.getName()));
	}
}
