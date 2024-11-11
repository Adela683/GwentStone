package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import lombok.Getter;
import minion.Minion;
import player.Player;
import constants.Constants;

public class Game {
    private final Player player1;
    private final Player player2;
    private int manaRound = 0;
    private int currentPlayer;
    @Getter
    private final ArrayNode gameOutput;
    private final ObjectMapper mapper = new ObjectMapper();
    private boolean isRoundStart;
    private final GameTable gameTable;

    public Game(final Player player1, final Player player2, final int currentPlayer) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        isRoundStart = true;
        gameOutput = mapper.createArrayNode();
        gameTable = new GameTable();
    }

    /**
     * Execute a game command
     * @param actionsInput command to be executed
     */
    public void executeCommand(final ActionsInput actionsInput) {
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
            case Constants.GET_FROZEN_CARDS:
                gameOutput.add(getFrozenCardsOnTable());
                return;
            case Constants.GET_PLAYER_ONE_WINS:
                gameOutput.add(player1.getGamesWonAsObjectNode());
                return;
            case Constants.GET_PLAYER_TWO_WINS:
                gameOutput.add(player2.getGamesWonAsObjectNode());
                return;
            case Constants.GET_TOTAL_GAMES_PLAYED:
                gameOutput.add(player1.getGamesPlayedAsObjectNode());
                return;
            default:
        }

        // normal commands are not allowed after a hero died
        if (player1.getHero().getHealth() <= 0 || player2.getHero().getHealth() <= 0) {
            return;
        }

        // Check if a normal game command was given
        ObjectNode result;
        switch (actionsInput.getCommand()) {
            case Constants.PLACE_CARD:
                // check if we got a valid index
                if (actionsInput.getHandIdx() < player.getCardsInHand().size()) {
                    Minion placedCard = player.getCardsInHand().get(actionsInput.getHandIdx());
                    result = GameCommands.placeCard(player, placedCard,
                            actionsInput.getHandIdx(), gameTable);
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
            case Constants.ATTACK_HERO:
                result = setUpAttackOnHero(actionsInput);
                if (!result.isEmpty()) {
                    gameOutput.add(result);
                }
                return;
            case Constants.USE_HERO_ABILITY:
                result = setUpHeroAbility(actionsInput);
                if (!result.isEmpty()) {
                    gameOutput.add(result);
                }
            default:
        }
    }

    /**
     * Prepares both players for a new round.
     */
    private void prepareRound() {
        if (manaRound < Constants.MAX_MANA_PER_ROUND) {
            manaRound++;
        }

        // Increase both player's mana
        player1.setMana(player1.getMana() + manaRound);
        player2.setMana(player2.getMana() + manaRound);

        // check if players can take a new card from the packet.
        if (!player1.getCurrentCardPacket().isEmpty()) {
            player1.getCardsInHand().add(player1.getCurrentCardPacket().pop());
        }
        if (!player2.getCurrentCardPacket().isEmpty()) {
            player2.getCardsInHand().add(player2.getCurrentCardPacket().pop());
        }

        isRoundStart = false;
    }

    /**
     * Get the card at the specified position.
     * @param actionsInput contains the x and y coordinates of the card
     * @return card at x,y coordinates
     */
    private ObjectNode getCardAtPosition(final ActionsInput actionsInput) {
        return gameTable.getMinionOnPosition(actionsInput.getX(), actionsInput.getY());
    }

    /**
     * End the current's player turn. Frozen effect resets,
     * and minions are able to attack again.
     */
    private void endPlayerTurn() {
        if (currentPlayer == 1) {
            player1.setDone(true);
            gameTable.resetMinionStats(Constants.FIRST_PLAYER_FRONT, Constants.FIRST_PLAYER_BACK);
            player1.getHero().setHasAttacked(false);
            currentPlayer = 2;
        } else {
            player2.setDone(true);
            gameTable.resetMinionStats(Constants.SECOND_PLAYER_FRONT, Constants.SECOND_PLAYER_BACK);
            player2.getHero().setHasAttacked(false);
            currentPlayer = 1;
        }

        if (checkEndRound()) {
            isRoundStart = true;
            player1.setDone(false);
            player2.setDone(false);
        }
    }

    /**
     * Check if a round has ended
     */
    private boolean checkEndRound() {
        return player1.isDone() && player2.isDone();
    }

    /**
     * Get the specified player's deck.
     * @param actionsInput contains the player index
     * @return a player's deck
     */
    private ObjectNode getPlayerDeck(final ActionsInput actionsInput) {
        int playerIndex = actionsInput.getPlayerIdx();

        ObjectNode objectNode;
        if (playerIndex == 1) {
            objectNode = player1.getDeckAsObjectNode(playerIndex);
        } else {
            objectNode = player2.getDeckAsObjectNode(playerIndex);
        }

        return objectNode;
    }

    /**
     * Get the cards in the hand of the specified player.
     * @param actionsInput contains the player index.
     * @return a player's hand cards
     */
    private ObjectNode getCardsInHand(final ActionsInput actionsInput) {
        int playerIndex = actionsInput.getPlayerIdx();

        ObjectNode objectNode;
        if (playerIndex == 1) {
            objectNode = player1.getCardsInHandAsObjectNode(playerIndex);
        } else {
            objectNode = player2.getCardsInHandAsObjectNode(playerIndex);
        }

        return objectNode;
    }

    /**
     * Find all cards on the table, from the top row to the bottom.
     * @return cards placed on table.
     */
    private ObjectNode getCardsOnTable() {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", Constants.GET_CARDS_ON_TABLE);

        ArrayNode firstPlayerFront = gameTable.getRowAsArrayNode(Constants.FIRST_PLAYER_FRONT);
        ArrayNode firstPlayerBack = gameTable.getRowAsArrayNode(Constants.FIRST_PLAYER_BACK);

        ArrayNode secondPlayerFront = gameTable.getRowAsArrayNode(Constants.SECOND_PLAYER_FRONT);
        ArrayNode secondPlayerBack = gameTable.getRowAsArrayNode(Constants.SECOND_PLAYER_BACK);

        ArrayNode combinedCards = mapper.createArrayNode();

        combinedCards.add(secondPlayerBack);
        combinedCards.add(secondPlayerFront);
        combinedCards.add(firstPlayerFront);
        combinedCards.add(firstPlayerBack);

        result.set("output", combinedCards);
        return result;
    }

    /**
     * Get all frozen cards on the table, from top row to bottom.
     * @return frozen cards.
     */
    private ObjectNode getFrozenCardsOnTable() {
        ObjectNode result = mapper.createObjectNode();
        result.put("command", Constants.GET_FROZEN_CARDS);

        ArrayNode frozenCards = gameTable.getFrozenMinionsAsArrayNode();

        result.set("output", frozenCards);
        return result;
    }

    /**
     * Prepare a minion for attack: extract attacker and attacked minion coordinates.
     * @param actionsInput contains coordinates of the involved cards
     * @return in case of error, error details
     */
    private ObjectNode setUpMinionAttack(final ActionsInput actionsInput) {
        Player attackedPlayer = getNextPlayer();

        int xAttacker = actionsInput.getCardAttacker().getX();
        int yAttacker = actionsInput.getCardAttacker().getY();
        int xAttacked = actionsInput.getCardAttacked().getX();
        int yAttacked = actionsInput.getCardAttacked().getY();
        return GameCommands.attackMinion(attackedPlayer, xAttacker, yAttacker,
                xAttacked, yAttacked, gameTable);
    }

    /**
     * Prepare an attack on a hero: extract the attacker minion's coordinates.
     * @param actionsInput contains coordinates of attacker
     * @return error details if attack failed or a game won message if hero died
     */
    private ObjectNode setUpAttackOnHero(final ActionsInput actionsInput) {
        Player attackerPlayer = getCurrentPlayer();
        Player attackedPlayer = getNextPlayer();

        int xAttacker = actionsInput.getCardAttacker().getX();
        int yAttacker = actionsInput.getCardAttacker().getY();
        return GameCommands.attackHero(attackerPlayer, attackedPlayer,
                xAttacker, yAttacker, gameTable);
    }

    /**
     * Prepare a hero to use their ability: get the affected row.
     * @param actionsInput contains coordinates for attack
     * @return error message in case ability use failed
     */
    private ObjectNode setUpHeroAbility(final ActionsInput actionsInput) {
        Player attackerPlayer = getCurrentPlayer();

        int xAttacked = actionsInput.getAffectedRow();
        int xAttacker = 0;
        if (attackerPlayer.getPlayerId() == 1) {
            // use this to check if attacker is on the same side.
            xAttacker = Constants.SECOND_PLAYER_BACK;
        }

        return GameCommands.useHeroAbility(attackerPlayer, xAttacker, xAttacked, gameTable);
    }

    /**
     * Prepares a minion to use their special ability: extract attack coordinates
     * @param actionsInput contains attack coordinates
     * @return an error message in case special fails
     */
    private ObjectNode setUpMinionSpecial(final ActionsInput actionsInput) {
        Player attackedPlayer = getNextPlayer();

        int xAttacker = actionsInput.getCardAttacker().getX();
        int yAttacker = actionsInput.getCardAttacker().getY();
        int xAttacked = actionsInput.getCardAttacked().getX();
        int yAttacked = actionsInput.getCardAttacked().getY();
        return GameCommands.specialAbilityMinion(attackedPlayer, xAttacker, yAttacker,
                xAttacked, yAttacked, gameTable);
    }

    /**
     * Show information about a player's hero.
     * @param actionsInput contains player index
     * @return the selected player's hero as an ObjectNode
     */
    private ObjectNode getPlayerHero(final ActionsInput actionsInput) {
        int playerIndex = actionsInput.getPlayerIdx();

        ObjectNode objectNode;
        if (playerIndex == 1) {
            objectNode = player1.getHero().getHeroAsObjectNode(playerIndex);
        } else {
            objectNode = player2.getHero().getHeroAsObjectNode(playerIndex);
        }

        return objectNode;
    }

    /**
     * Get a player's mana
     * @param actionsInput contains player index
     * @return the selected player's mana
     */
    private ObjectNode getPlayerMana(final ActionsInput actionsInput) {
        int playerIndex = actionsInput.getPlayerIdx();

        ObjectNode objectNode;
        if (playerIndex == 1) {
            objectNode = player1.getManaAsObjectNode(playerIndex);
        } else {
            objectNode = player2.getManaAsObjectNode(playerIndex);
        }

        return objectNode;
    }

    /**
     * Find the current player
     * @return index of player
     */
    private ObjectNode getPlayerTurn() {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", Constants.GET_PLAYER_TURN);
        node.put("output", currentPlayer);

        return node;
    }

    /**
     * @return the current player
     */
    private Player getCurrentPlayer() {
        if (currentPlayer == 1) {
            return player1;
        }
        return player2;
    }

    /**
     * @return the next player to make a move
     */
    private Player getNextPlayer() {
        if (currentPlayer == 1) {
            return player2;
        }
        return player1;
    }
}
