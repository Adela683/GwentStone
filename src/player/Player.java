package player;

import card.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import hero.Hero;
import lombok.Getter;
import lombok.Setter;
import minion.Minion;
import constants.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

@Getter
@Setter
public class Player {
	private ArrayList<ArrayList<Minion>> deck;
	private ArrayList<Minion> cardsInHand;
	private Stack<Minion> currentCardPacket;
	private Minion[] frontRow;
	private Minion[] backRow;
	private Hero hero;
	private boolean isDone;
	private int cardsInDeck;
	private int nrDecks;
	private int mana;
	private int currentDeckIndex;
	private int gamesPlayed = 0;
	private int gamesWon = 0;

	private ObjectMapper mapper = new ObjectMapper();

	public Player(ArrayList<ArrayList<Minion>> deck, int cardsInDeck, int nrDecks) {
		this.deck = deck;
		this.cardsInDeck = cardsInDeck;
		this.nrDecks = nrDecks;
		cardsInHand = new ArrayList<>();
		frontRow = new Minion[5];
		backRow = new Minion[5];
		isDone = false;
		currentCardPacket = new Stack<>();
		mana = 0;
	}

	public ObjectNode getDeckAsObjectNode(int playerIndex) {
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

	public ObjectNode getCardsInHandAsObjectNode(int playerIndex) {
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

	public ObjectNode getManaAsObjectNode(int playerIndex) {
		ObjectNode manaNode = mapper.createObjectNode();
		manaNode.put("command", Constants.GET_PLAYER_MANA);
		manaNode.put(Constants.PLAYER_IDX, playerIndex);
		manaNode.put("output", mana);

		return manaNode;
	}


	public ArrayNode getFrontRowAsArrayNode() {
		ArrayNode node = mapper.createArrayNode();

		for (Minion minion : frontRow) {
			if (minion != null) {
				ObjectNode minionObject = minion.getMinionAsObjectNode();
				node.add(minionObject);
			}
		}

		return node;
	}

	public ArrayNode getBackRowAsArrayNode() {
		ArrayNode node = mapper.createArrayNode();

		for (Minion minion : backRow) {
			if (minion != null) {
				ObjectNode minionObject = minion.getMinionAsObjectNode();
				node.add(minionObject);
			}
		}

		return node;
	}

	private void resetRowMinions(Minion[] row) {
		for (Minion minion : row){
			if (minion != null) {
				minion.setFrozen(false);
				minion.setHasAttacked(false);
			}
		}
	}

	public void resetMinionStats() {
		resetRowMinions(frontRow);
		resetRowMinions(backRow);
	}

	public ObjectNode getMinionOnRow(int x, int y) {
		if (x == 0 || x == 3) {
			return getMinionOnPosition(x, y, backRow);
		}
		return getMinionOnPosition(x, y, frontRow);
	}

	/**
	 * Returns a specific minion as an object node
	 * @param x minion position
	 * @param y minion position
	 * @param row where the minion is
	 * @return minion or error message
	 */
	private ObjectNode getMinionOnPosition(int x, int y, Minion[] row) {
		ObjectNode result = mapper.createObjectNode();
		result.put("command", Constants.GET_CARD_AT_POSITION);
		result.put("x", x);
		result.put("y", y);

		Minion minion = row[y];
		if (minion == null) {
			result.put("output", Constants.NO_CARD_AT_POSITION);
			return result;
		}

		ObjectNode objectNodeMinion = minion.getMinionAsObjectNode();
		result.set("output", objectNodeMinion);
		return result;
	}
}
