package player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hero.Hero;
import lombok.Getter;
import lombok.Setter;
import minion.Minion;
import constants.Constants;

import java.util.ArrayList;
import java.util.Stack;

@Getter
@Setter
public class Player {
    private ArrayList<ArrayList<Minion>> deck;
    private ArrayList<Minion> cardsInHand;
    private Stack<Minion> currentCardPacket;
    private Hero hero;
    private boolean isDone;
    private int cardsInDeck;
    private int nrDecks;
    private int mana;
    private int currentDeckIndex;
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int playerId;
    private ObjectMapper mapper = new ObjectMapper();

    public Player(final ArrayList<ArrayList<Minion>> deck, final int cardsInDeck,
                  final int nrDecks, final int playerId) {
        this.deck = deck;
        this.cardsInDeck = cardsInDeck;
        this.nrDecks = nrDecks;
        cardsInHand = new ArrayList<>();
        isDone = false;
        currentCardPacket = new Stack<>();
        mana = 0;
        this.playerId = playerId;
    }

    /**
     * @return total games played as ObjectNode
     */
    public ObjectNode getGamesPlayedAsObjectNode() {
        ObjectNode gamesNode = mapper.createObjectNode();
        gamesNode.put("command", Constants.GET_TOTAL_GAMES_PLAYED);
        gamesNode.put("output", gamesPlayed);
        return gamesNode;
    }

    /**
     * @return total games won as ObjectNode
     */
    public ObjectNode getGamesWonAsObjectNode() {
        ObjectNode gamesNode = mapper.createObjectNode();
        if (playerId == 1) {
            gamesNode.put("command", Constants.GET_PLAYER_ONE_WINS);
        } else {
            gamesNode.put("command", Constants.GET_PLAYER_TWO_WINS);
        }
        gamesNode.put("output", gamesWon);
        return gamesNode;
    }

    /**
     * Get the player's current card packet as an ObjectNode
     * @param playerIndex required in output for clarity
     * @return the player's current card packet
     */
    public ObjectNode getDeckAsObjectNode(final int playerIndex) {
        ObjectNode deckNode = mapper.createObjectNode();
        deckNode.put("command", Constants.GET_PLAYER_DECK);
        deckNode.put(Constants.PLAYER_IDX, playerIndex);

        ArrayNode outputNode = mapper.createArrayNode();

        for (int i = currentCardPacket.size() - 1; i >= 0; i--) {
            ObjectNode packetObjectNode = currentCardPacket.get(i).getMinionAsObjectNode();
            outputNode.add(packetObjectNode);
        }

        deckNode.set("output", outputNode);

        return deckNode;
    }

    /**
     * Get cards in hand of the player
     * @param playerIndex required in output for clarity
     * @return cards in hand
     */
    public ObjectNode getCardsInHandAsObjectNode(final int playerIndex) {
        ObjectNode cardsNode = mapper.createObjectNode();
        cardsNode.put("command", Constants.GET_CARDS_IN_HAND);
        cardsNode.put(Constants.PLAYER_IDX, playerIndex);

        ArrayNode outputNode = mapper.createArrayNode();

        for (Minion minion : cardsInHand) {
            ObjectNode packetObjectNode = minion.getMinionAsObjectNode();
            outputNode.add(packetObjectNode);
        }
        cardsNode.set("output", outputNode);

        return cardsNode;
    }

    /**
     * Get player's mana as ObjectNode
     * @param playerIndex required in output for clarity
     * @return mana
     */
    public ObjectNode getManaAsObjectNode(final int playerIndex) {
        ObjectNode manaNode = mapper.createObjectNode();
        manaNode.put("command", Constants.GET_PLAYER_MANA);
        manaNode.put(Constants.PLAYER_IDX, playerIndex);
        manaNode.put("output", mana);

        return manaNode;
    }
}
