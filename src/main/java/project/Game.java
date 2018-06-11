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
    public static final String DOUBLE_DOWN = "doubledown";
    public static final String HELP = "help";

    private int currentRound = 0;

    private Dealer dealer;
    private List<Player> players = new ArrayList<>();
    private Deck cardDeck;

    private boolean gameEnd = false;

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter player count: ");
        int players = readInt();
        while (players <= 0) {
            System.out.print("Please, enter positive integral number: ");
            players = readInt();
        }

        System.out.print("Enter deck count: ");
        int deckCount = readInt();
        while (deckCount <= 0) {
            System.out.print("Please, enter positive integral number: ");
            deckCount = readInt();
        }

        System.out.print("Enter initial budget: ");
        int budget = readInt();
        while (budget < 100) {
            System.out.print("Please, enter integral number larger than 100: ");
            budget = readInt();
        }

        Game game = new Game(players, deckCount, budget);
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
            players.add(new Player(scanner.next(), initialBudget));
        }

        cardDeck = new Deck(deckCount);
        dealer = new Dealer("Bruce");
    }

    /**
     * Starts the game.
     */
    public void play() {
        while (!gameEnd) {
            round();

            gameEnd = dialog("Do you want to quit the game? (y/n) ", "y", "n");
        }
    }

    /**
     * Displays confirmation dialog.
     * @param message message to show
     * @param yes confirmation
     * @param no rejection
     * @return true if player typed yes, false otherwise
     */
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

        if (dealer.hasBlackjack()) {
            dealerBlackjack();
        } else {
            turns();
            dealersTurn();
        }

        evaluateRound();
        cleanup();
        System.out.println();
    }

    private void placeBets() {
        for (Player player : players) {
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

    private static int readInt() {
        try {
            return scanner.nextInt();

        } catch (InputMismatchException ex) {
            scanner.next();
            return -1;
        }
    }

    private void initialDeal() {
        for (Player player : players) {
            player.hit(cardDeck.draw());
            player.hit(cardDeck.draw());
            System.out.println(player.getName() + "'s hand:");
            System.out.println(player.getHand());
        }

        dealer.hit(cardDeck.draw());
        dealer.hit(cardDeck.draw());

        System.out.println("Dealer's revealed card is: " + dealer.firstCard());
        System.out.println();
        System.out.println("Press ENTER to continue...");
        new Scanner(System.in).nextLine();

        if (dealer.firstCard().getRank() == CardRank.ACE11) {
            placeInsurance();
            resolveInsurance();
        }
    }

    private void placeInsurance() {
        System.out.println("Dealer has an ace!");

        for (Player player : players) {
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

        for (Player player : players) {
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

    private void dealerBlackjack() {
        for (Player player : players) {
            if (player.hasBlackjack()) {
                System.out.println(player.getName() + " also has a blackjack! They lost nothing.");
                player.setCurrentBet(0);
            }
        }
    }

    private void turns() {
        for (Player player : players) {
            playerTurn(player);

            System.out.println("Press ENTER to continue...");
            new Scanner(System.in).nextLine();
        }
    }

    private void playerTurn(Player player) {
        boolean done = false;

        while (!done) {
            System.out.println(player);
            System.out.println("Dealer's revealed card is " + dealer.firstCard());

            if (player.hasBlackjack()) {
                System.out.println("You have a blackjack!");
                System.out.println();
                break;
            }

            System.out.println();
            System.out.print("What do you want to do? ");

            done = chooseAction(player, scanner.next().trim());

            if (done && player.isSplit() && !player.isPlayingAsSplit()) {
                player.setPlayingAsSplit(true);
                done = false;
            }

            System.out.println();
        }
    }

    private boolean chooseAction(Player player, String command) {
        switch (command) {
            case HIT:
                return hit(player);

            case STAND:
                return true;

            case SURRENDER:
                return surrender(player);

            case SPLIT:
                if (player.canSplit()) {
                    player.split(cardDeck.draw(), cardDeck.draw());
                }
                return false;

            case DOUBLE_DOWN:
                player.doubleDown();
                return false;

            case HELP:
                printHelp();
                return false;

            default:
                System.out.println("Unknown command.");
                printHelp();
                return false;
        }
    }

    private boolean hit(Player player) {
        Card card = cardDeck.draw();
        System.out.println("You drew " + card);

        if (card.getRank() == CardRank.ACE11 &&
                !dialog("Do you want to keep it's value as 11? (y/n) ", "y", "n")) {
            card.setRank(CardRank.ACE1);

        } else if (card.getRank() == CardRank.ACE1 &&
                !dialog("Do you want to keep it's value as 1? (y/n) ", "y", "n")) {
            card.setRank(CardRank.ACE11);
        }
        player.hit(card);
        System.out.println("Your sum is " + player.getHandSum());

        if (player.isBusted()) {
            System.out.println("You busted!");
            return true;
        }
        return false;
    }

    private boolean surrender(Player player) {
        if (player.getCardCount() == 2) {
            System.out.println("You lost " + (player.getCurrentBet() / 2) + ".");
            player.surrender();
            System.out.println("Your budget is " + player.getBudget());
            return true;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("\t" + HIT + " - draw a new card");
        System.out.println("\t" + STAND + " - end your turn");
        System.out.println("\t" + SURRENDER + " - loose half of your bet (only if you have two cards)");
        System.out.println("\t" + SPLIT + " - split your hand to two hands with separate bets (only if you have " +
                "two cards with the same value)");
        System.out.println("\t" + DOUBLE_DOWN + " - double your current bet");
        System.out.println("\t" + HELP + " - print this help");
        System.out.println();
        System.out.println("Press ENTER to continue...");
        new Scanner(System.in).nextLine();
    }

    private void dealersTurn() {
        System.out.println("Dealer's hand: ");
        System.out.println(dealer.getHand());

        while (dealer.getHandSum() < 17) {
            Card card = cardDeck.draw();
            System.out.println("Dealer drew " + card);

            dealer.hit(card);

            if (dealer.isBusted()) {
                System.out.println("Dealer busted!");
            }
        }
        System.out.println("Dealer's sum is " + dealer.getHandSum());
    }

    private void evaluateRound() {
        System.out.println();

        for (Player player : players) {
            int profit = computeProfit(player);

            if (profit == 0) {
                System.out.println(player.getName() + " didn't win anything this round.");

            } else if (profit < 0) {
                System.out.println(player.getName() + " lost " + (-profit) + " this round.");

            } else {
                System.out.println(player.getName() + " won " + profit + " this round.");
            }

            player.addToBudget(profit);

            if (player.getBudget() == 0) {
                System.out.println(player.getName() + " lost all their money. Good bye.");
            }
        }

        players.removeIf(p -> p.getBudget() == 0);
        System.out.println();
    }

    private int computeProfit(Player player) {
        player.setPlayingAsSplit(false);
        boolean playerWon = hasPlayerWon(player);

        int profit = player.getCurrentBet();
        if (!playerWon) {
            profit *= -1;
        }

        if (player.isSplit()) {
            player.setPlayingAsSplit(true);
            playerWon = hasPlayerWon(player);
            profit += ((playerWon) ? 1 : -1) * player.getCurrentBet();
        }

        return profit;
    }

    private boolean hasPlayerWon(Player player) {
        if (player.hasBlackjack() && !dealer.hasBlackjack()) {
            player.setCurrentBet(player.getCurrentBet() * 3 / 2);
            return true;

        } else if (!player.isBusted() && !dealer.isBusted() && player.getHandSum() > dealer.getHandSum()) {
            return true;

        } else if (!player.isBusted() && !dealer.isBusted() && player.getHandSum() == dealer.getHandSum()) {
            player.setCurrentBet(0);
            return true;

        } else if (!player.isBusted() && dealer.isBusted()) {
            return true;
        }

        return false;
    }

    private void cleanup() {
        for (Player player : players) {
            if (player.isSplit()) {
                player.setPlayingAsSplit(false);

                for (int i = 0; i < player.getSplitHand().getSize(); i++) {
                    cardDeck.discard(player.getSplitHand().getCard(i));
                }
            }

            for (int i = 0; i < player.getHand().getSize(); i++) {
                cardDeck.discard(player.getHand().getCard(i));
            }
            player.cleanHand();
        }

        for (int i = 0; i < dealer.getHand().getSize(); i++) {
            cardDeck.discard(dealer.getHand().getCard(i));
        }
        dealer.cleanHand();
    }
}
