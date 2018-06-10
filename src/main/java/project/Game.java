package project;

import java.util.List;
import java.util.Scanner;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Game {
    private int turn;
    private int currentPlayer;
    private List<Player> players;

    public Game(int humanPlayers) {
        if (humanPlayers <= 0) {
            throw new IllegalArgumentException("Player count must be positive: " + humanPlayers);
        }

        turn = 1;
        currentPlayer = 0;
        Scanner scanner = new Scanner(System.in);
        
        for (int i = 0; i < humanPlayers; i++) {
            String name = scanner.next();
            System.out.println(name);
        }
    }

    public static void main(String[] args) {
        Game game = new Game(3);
    }
}
