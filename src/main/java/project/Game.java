package project;

import project.entities.Deck;
import project.entities.Human;
import project.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Game {
    private int currentRound = 0;
    private int currentPlayer = 0;

    private List<Player> players = new ArrayList<>();
    private Deck cardDeck;

    private boolean gameEnd = false;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Game game = new Game(3, 1, 100);
        game.play();
    }

    public Game(int humanPlayers, int deckCount, int initialBudget) {
        if (humanPlayers <= 0) {
            throw new IllegalArgumentException("Player count must be positive: " + humanPlayers);
        }

        if (deckCount <= 0) {
            throw new IllegalArgumentException("Deck count must be positive: " + deckCount);
        }

        if (initialBudget < 100) {
            throw new IllegalArgumentException("Initial budget must be larger than 100: " + initialBudget);
        }

        for (int i = 0; i < humanPlayers; i++) {
            System.out.print("Enter player " + (i+1) + " name: ");
            players.add(new Human(scanner.next(), initialBudget));
        }

        cardDeck = new Deck(deckCount);
    }

    public void play() {
        while (!gameEnd) {
            round();

            System.out.print("Do you want to quit the game? (y/n) ");
            while (true) {
                String s = scanner.next();
                if (s.equals("y")) {
                    return;

                } else if (s.equals("n")) {
                    break;

                } else {
                    System.out.print("Unknown command. Please answer correctly: ");
                }
            }
        }
    }

    private void round() {
        currentRound++;
    }
}
