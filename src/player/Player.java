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
import java.util.Stack;

@Getter
@Setter
public class Player {
	ArrayList<ArrayList<Minion>> deck;
	ArrayList<Minion> cardsInHand;
	Stack<Minion> currentCardPacket;
	ArrayList<Minion> frontRow;
	ArrayList<Minion> backRow;
	Hero hero;
	boolean isDone;
	int cardsInDeck;
	int nrDecks;
	int mana;
	int currentDeckIndex;
	private ObjectMapper mapper = new ObjectMapper();

	public Player(ArrayList<ArrayList<Minion>> deck, int cardsInDeck, int nrDecks) {
		this.deck = deck;
		this.cardsInDeck = cardsInDeck;
		this.nrDecks = nrDecks;
		cardsInHand = new ArrayList<>();
		frontRow = new ArrayList<>();
		backRow = new ArrayList<>();
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
}
