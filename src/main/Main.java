package main;

import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.CardInput;
import fileio.Input;
import game.GameManager;
import minion.Minion;
import minion.MinionFactory;
import player.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        ArrayList<ArrayList<Minion>> playerOneDecks =
                getPlayerDecksAsMinions(inputData.getPlayerOneDecks().getDecks());
        ArrayList<ArrayList<Minion>> playerTwoDecks =
                getPlayerDecksAsMinions(inputData.getPlayerTwoDecks().getDecks());

        Player player1 = new Player(playerOneDecks,
                inputData.getPlayerOneDecks().getNrCardsInDeck(),
                inputData.getPlayerOneDecks().getNrCardsInDeck(),
                1);

        Player player2 = new Player(playerTwoDecks,
                inputData.getPlayerTwoDecks().getNrCardsInDeck(),
                inputData.getPlayerTwoDecks().getNrCardsInDeck(),
                2);

        GameManager gameManager = new GameManager(player1, player2, inputData.getGames(), output);
        gameManager.play();

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    private static ArrayList<ArrayList<Minion>>
    getPlayerDecksAsMinions(final ArrayList<ArrayList<CardInput>> playerDeck) {
        ArrayList<ArrayList<Minion>> deck = new ArrayList<>();
        for (ArrayList<CardInput> cardInputs : playerDeck) {
            ArrayList<Minion> cards = new ArrayList<>();
            for (CardInput cardInput : cardInputs) {
                Minion minion = MinionFactory.createMinionFromCardInput(cardInput);
                cards.add(minion);
            }
            deck.add(cards);
        }
        return deck;
    }
}
