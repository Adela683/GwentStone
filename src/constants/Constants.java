package constants;

public abstract class Constants {
    public static final String GET_PLAYER_DECK = "getPlayerDeck";
    public static final String PLAYER_IDX = "playerIdx";
    public static final String GET_PLAYER_HERO = "getPlayerHero";
    public static final int HERO_START_HEALTH = 30;
    public static final String GET_PLAYER_TURN = "getPlayerTurn";
    public static final String END_PLAYER_TURN = "endPlayerTurn";
    public static final String PLACE_CARD = "placeCard";
    public static final String NOT_ENOUGH_MANA_PLACE = "Not enough mana to place "
            + "card on table.";
    public static final String ROW_FULL_PLACE = "Cannot place card on table since row is full.";
    public static final String GET_CARDS_IN_HAND = "getCardsInHand";
    public static final String GET_PLAYER_MANA = "getPlayerMana";
    public static final String GET_CARDS_ON_TABLE = "getCardsOnTable";
    public static final String CARD_USES_ATTACK = "cardUsesAttack";
    public static final String CARD_ON_SAME_SIDE_ERROR = "Attacked card does not "
            + "belong to the enemy.";
    public static final String CARD_HAS_ATTACKED_ERROR = "Attacker card has already "
            + "attacked this turn.";
    public static final String CARD_IS_FROZEN = "Attacker card is frozen.";
    public static final String ENEMY_HAS_TANK = "Attacked card is not of type 'Tank'.";
    public static final String GET_CARD_AT_POSITION = "getCardAtPosition";
    public static final String NO_CARD_AT_POSITION = "No card available at that position.";
    public static final String CARD_USES_SPECIAL_ABILITY = "cardUsesAbility";
    public static final String CARD_NOT_ON_SAME_SIDE_ERROR = "Attacked card does not belong to "
            + "the current player.";
    public static final String ATTACK_HERO = "useAttackHero";
    public static final String PLAYER_ONE_KILLED_ENEMY_HERO = "Player one killed the enemy hero.";
    public static final String PLAYER_TWO_KILLED_ENEMY_HERO = "Player two killed the enemy hero.";
    public static final String GET_FROZEN_CARDS = "getFrozenCardsOnTable";
    public static final String USE_HERO_ABILITY = "useHeroAbility";
    public static final String NOT_ENOUGH_MANA_HERO = "Not enough mana to use hero's ability.";
    public static final String HERO_ALREADY_ATTACKED = "Hero has already attacked this turn.";
    public static final String SELECTED_ROW_SAME_PLAYER = "Selected row does not belong "
            + "to the enemy.";
    public static final String SELECTED_ROW_ENEMY_PLAYER = "Selected row does not belong to "
            + "the current player.";
    public static final String GET_TOTAL_GAMES_PLAYED = "getTotalGamesPlayed";
    public static final String GET_PLAYER_ONE_WINS = "getPlayerOneWins";
    public static final String GET_PLAYER_TWO_WINS = "getPlayerTwoWins";
    public static final int MAX_MANA_PER_ROUND = 10;
    public static final int FIRST_PLAYER_FRONT = 1;
    public static final int FIRST_PLAYER_BACK = 0;
    public static final int SECOND_PLAYER_FRONT = 2;
    public static final int SECOND_PLAYER_BACK = 3;
    public static final int ROW_OFFSET = 3;
    public static final int TABLE_ROWS = 4;
    public static final int TABLE_COLUMNS = 5;
}
