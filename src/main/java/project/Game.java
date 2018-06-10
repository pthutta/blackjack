package project;

import project.entities.*;
import project.enums.CardRank;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Game {
    public static final String HIT = "hit";
    public static final String STAND = "stand";
    public static final String SURRENDER = "surrender";
    public static final String SPLIT = "split";
    public static final String DOUBLE_DOWN = "double down";
    public static final String HELP = "help";

    private int currentRound = 0;

    private Dealer dealer;
    private List<Human> players = new ArrayList<>();
    private Deck cardDeck;

    private boolean gameEnd = false;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Game game = new Game(2, 1, 100);
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
        dealer = new Dealer("Bruce");
    }

    public void play() {
        while (!gameEnd) {
            round();

            gameEnd = dialog("Do you want to quit the game? (y/n) ", "y", "n");
        }
    }

    private boolean dialog(String message, String yes, String no) {
        System.out.print(message);

        while (true) {
            String s = scanner.next();

            if (s.equals(yes)) {
                return true;

            } else if (s.equals(no)) {
                return false;

            } else {
                System.out.print("Unknown command. Please answer correctly: ");
            }
        }
    }

    private void round() {
        currentRound++;
        System.out.println("Round " + currentRound);

        placeBets();

        initialDeal();
        turns();

        evaluateRound();
        cleanup();
        System.out.println();
    }

    private void placeBets() {
        for (Human player : players) {
            System.out.print(player.getName() + ", your budget is " + player.getBudget() + ". Place your bet: ");

            while (true) {
                int bet = readInt();

                if (bet <= 0 || bet > player.getBudget()) {
                    System.out.print("Please, enter positive integral number smaller than your budget: ");

                } else {
                    player.setCurrentBet(bet);
                    break;
                }
            }

            System.out.println();
        }
    }

    private int readInt() {
        try {
            return scanner.nextInt();

        } catch (InputMismatchException ex) {
            scanner.next();
            return -1;
        }
    }

    private void initialDeal() {
        for (Human player : players) {
            player.hit(cardDeck.draw());
            player.hit(cardDeck.draw());
        }

        dealer.hit(cardDeck.draw());
        dealer.hit(cardDeck.draw());

        if (dealer.firstCard().getRank() == CardRank.ACE11) {
            placeInsurance();
            resolveInsurance();

            if (dealer.hasBlackjack()) {
                //TODO dealer has a blackjack
            }

            //TODO check player blackjacks
        }
    }

    private void placeInsurance() {
        System.out.println("Dealer has an ace!");

        for (Human player : players) {
            System.out.print(player.getName() + ", enter sum you want to place as insurance: ");

            while (true) {
                int insurance = readInt();

                if (insurance < 0 || insurance > player.getCurrentBet() / 2) {
                    System.out.print("Please, enter non negative value up to half of your bet (" +
                            (player.getCurrentBet() / 2) + "): ");

                } else {
                    player.setInsurance(insurance);
                    break;
                }
            }
        }

        System.out.println();
    }

    private void resolveInsurance() {
        System.out.println("Dealer " + ((dealer.hasBlackjack()) ? "has" : "doesn't have") + " a blackjack.");

        for (Human player : players) {
            int insurance = player.getInsurance();

            if (insurance == 0) {
                continue;
            }

            if (dealer.hasBlackjack()) {
                insurance *= 2;
                System.out.println(player.getName() + " won " + insurance + " as an insurance.");

            } else {
                System.out.println(player.getName() + " lost " + insurance + " as an insurance.");
                insurance *= -1;
            }

            player.addToBudget(insurance);
            System.out.println();
        }
    }

    private void turns() {
        for (Human player : players) {
            playerTurn(player);
        }
    }

    private void playerTurn(Human player) {
        System.out.println(player);
        boolean done = false;

        while (!done) {
            System.out.print("What do you want to do? ");

            switch (scanner.next()) {
                case HIT:
                    done = hit(player);
                    break;

                case STAND:
                    player.stand();
                    done = true;
                    break;

                case SURRENDER:
                    done = surrender(player);
                    break;

                case SPLIT:
                    if (player.canSplit()) {
                        split(player);
                    }
                    break;

                case DOUBLE_DOWN:
                    player.doubleDown();
                    break;

                case HELP:
                    printHelp();
                    break;

                default:
                    System.out.println("Unknown command.");
                    printHelp();
                    break;
            }
        }

        System.out.println();
    }

    private boolean hit(Human player) {
        Card card = cardDeck.draw();

        System.out.println("You drew " + card);

        if (card.getRank() == CardRank.ACE11 &&
                !dialog("Do you want to keep it's value as 11? (y/n) ", "y", "n")) {
            card.setRank(CardRank.ACE1);
        }
        player.hit(card);
        System.out.println("Your sum is " + player.getHandSum());

        if (player.isBusted()) {
            System.out.println("You busted!");
            return true;
        }
        return false;
    }

    private void split(Human player) {
        Card card = player.split();
        if (card != null) {
            Human newHand = new Human(player.getName() + " - 2. hand", player.getBudget());
            newHand.hit(card);
            newHand.hit(cardDeck.draw());
            newHand.setHasSplit(true);

            player.setName(player.getName() + " - 1. hand");
            player.hit(cardDeck.draw());
            player.setHasSplit(true);
            int index = players.indexOf(player);
            players.add(index + 1, newHand);
        }
    }

    private boolean surrender(Human player) {
        if (player.getCardCount() == 2) {
            System.out.println("You lost " + (player.getCurrentBet() / 2) + ".");
            player.surrender();
            System.out.println(" Your budget is " + player.getBudget());
            return true;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("help");
    }

    private void evaluateRound() {
        System.out.println();

        for (Human player : players) {
            int bet = player.getCurrentBet();

            System.out.println(player.getName() + ((player.isBusted()) ? " lost " : " won ") + bet + " this round.");

            if (player.isBusted()) {
                bet *= -1;
            }

            player.addToBudget(bet);

            if (player.getBudget() == 0) {
                System.out.println(player.getName() + " lost all their money. Good bye.");
            }
        }

        players.removeIf(p -> p.getBudget() == 0);
        System.out.println();
    }

    private void cleanup() {
        System.out.println("cleanup");
    }
}
