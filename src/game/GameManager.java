package game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.GameInput;
import fileio.StartGameInput;
import hero.HeroFactory;
import lombok.Getter;
import minion.Minion;
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

			// TODO MAKE COPY FOR PLAYERS CURRENT DECK, SO CHANGES LAST ONLY ONE ROUND
			player1.setHero(HeroFactory.createHeroFromCardInput(startInput.getPlayerOneHero(), startInput.getPlayerOneHero().getName()));
			player2.setHero(HeroFactory.createHeroFromCardInput(startInput.getPlayerTwoHero(), startInput.getPlayerTwoHero().getName()));

			Stack<Minion> player1Packet = new Stack<>();
			Stack<Minion> player2Packet = new Stack<>();

			// TODO MAKE COPY OF THE DECK.GET(startInput.getPlayerOneDeckIdx()), so it works on multiple games
			Random random = new Random(startInput.getShuffleSeed());
			Collections.shuffle(player1.getDeck().get(startInput.getPlayerOneDeckIdx()), random);

			random = new Random(startInput.getShuffleSeed());
			Collections.shuffle(player2.getDeck().get(startInput.getPlayerTwoDeckIdx()), random);

			for (int i = player1.getCardsInDeck() - 1; i >= 0; i--) {
				player1Packet.push(player1.getDeck().get(startInput.getPlayerOneDeckIdx()).get(i));
			}

			for (int i = player2.getCardsInDeck() - 1; i >= 0; i--) {
				player2Packet.push(player2.getDeck().get(startInput.getPlayerTwoDeckIdx()).get(i));
			}

			// TODO FULLY INITIATE PLAYERS
			player1.setCurrentDeckIndex(startInput.getPlayerOneDeckIdx());
			player2.setCurrentDeckIndex(startInput.getPlayerTwoDeckIdx());

			player1.setCurrentCardPacket(player1Packet);
			player2.setCurrentCardPacket(player2Packet);

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
}
