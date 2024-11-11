package game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ActionsInput;
import fileio.CardInput;
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
    private final Player player1;
    private final Player player2;
    private final ArrayList<GameInput> games;
    @Getter
    private final ArrayNode output;

    public GameManager(final Player player1, final Player player2,
                       final ArrayList<GameInput> games, final ArrayNode output) {
        this.player1 = player1;
        this.player2 = player2;
        this.games = games;
        this.output = output;
    }

    /**
     * Run all games in the input file.
     */
    public void play() {
        for (GameInput game : games) {
            StartGameInput startInput = game.getStartGame();
            ArrayList<ActionsInput> gameActions = game.getActions();

            initPlayer(player1, startInput.getShuffleSeed(), startInput.getPlayerOneDeckIdx(),
                    startInput.getPlayerOneHero());
            initPlayer(player2, startInput.getShuffleSeed(), startInput.getPlayerTwoDeckIdx(),
                    startInput.getPlayerTwoHero());

            // Start a new game
            Game currentGame = new Game(player1, player2, startInput.getStartingPlayer());

            // Execute all actions for the current game.
            for (ActionsInput action : gameActions) {
                currentGame.executeCommand(action);
            }

            // Put game result in the output node.
            for (JsonNode node : currentGame.getGameOutput()) {
                output.add(node);
            }
        }
    }

    /**
     * Prepares a player to play a game.
     * @param player to prepare for game
     */
    private void initPlayer(final Player player, final long shuffleSeed,
                            final int currentDeckIndex, final CardInput hero) {
        player.setCardsInHand(new ArrayList<>());
        player.setDone(false);
        player.setMana(0);
        player.setCurrentDeckIndex(currentDeckIndex);

        Stack<Minion> playerPacket = new Stack<>();
        Random random = new Random(shuffleSeed);

        // Make a deep copy of the current packet, so we can keep the deck in the original form.
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
        player.setHero(HeroFactory.createHeroFromCardInput(hero));
    }
}
