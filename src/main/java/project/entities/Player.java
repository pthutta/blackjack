package project.entities;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Player extends Human {
    private int currentBet;
    private int insurance;
    private int budget;

    private Hand splitHand = null;
    private int splitBet;
    private boolean playingAsSplit = false;
    private boolean splitBusted = false;

    public Player(String name, int budget) {
        super(name);

        if (budget <= 0) {
            throw new IllegalArgumentException("Budget must be positive value: " + budget);
        }

        this.budget = budget;
    }

    public Hand getSplitHand() {
        return splitHand;
    }

    public boolean isPlayingAsSplit() {
        return playingAsSplit;
    }

    public void setPlayingAsSplit(boolean playing) {
        playingAsSplit = playing;
    }

    public boolean isSplit() {
        return splitHand != null;
    }

    public boolean canSplit() {
        return !isSplit() && hand.canSplit();
    }

    @Override
    public void setBusted(boolean busted) {
        if (isPlayingAsSplit()) {
            splitBusted = busted;

        } else {
            super.setBusted(busted);
        }
    }

    @Override
    public boolean isBusted() {
        if (isPlayingAsSplit()) {
            return splitBusted;

        } else {
            return super.isBusted();
        }
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getCurrentBet() {
        if (isPlayingAsSplit()) {
            return splitBet;

        } else {
            return currentBet;
        }
    }

    public void setCurrentBet(int currentBet) {
        if (isPlayingAsSplit()) {
            this.splitBet = currentBet;

        } else {
            this.currentBet = currentBet;
        }
    }

    public int getCardCount() {
        if (isPlayingAsSplit()) {
            return splitHand.getCardCount();

        } else {
            return hand.getCardCount();
        }
    }

    @Override
    public int getHandSum() {
        if (isPlayingAsSplit()) {
            return splitHand.getSum();

        } else {
            return super.getHandSum();
        }
    }

    @Override
    public boolean hasBlackjack() {
        if (isPlayingAsSplit()) {
            return splitHand.isBlackjack();

        } else {
            return super.hasBlackjack();
        }
    }

    public int getBudget() {
        return budget;
    }

    public void addToBudget(int value) {
        this.budget += value;
    }

    /**
     * Add card to current hand.
     * @param card card to add
     */
    @Override
    public void hit(Card card) {
        Hand playingHand = (isPlayingAsSplit()) ? splitHand : hand;

        if (playingHand.addCard(card)) {
            setBusted(true);
        }
    }

    /**
     * Surrender and loose half of your current bet.
     */
    public void surrender() {
        if (isPlayingAsSplit()) {
            addToBudget(-splitBet / 2);
            splitBet = 0;

        } else {
            addToBudget(-currentBet / 2);
            currentBet = 0;
        }
    }

    /**
     * Split current hand
     * @param orig card to add to original hand
     * @param split card to add to split hand
     */
    public void split(Card orig, Card split) {
        if (!canSplit()) {
            return;
        }

        Card card = hand.split();
        if (card != null) {
            splitHand = new Hand();
            splitHand.addCard(card);
            splitHand.addCard(split);

            if (currentBet * 2 > budget) {
                splitBet = budget - currentBet;
            } else {
                splitBet = currentBet;
            }

            hand.addCard(orig);
        }
    }

    /**
     * Double current bet.
     */
    public void doubleDown() {
        if (isPlayingAsSplit()) {
            splitBet *= 2;
            if (splitBet > budget + currentBet) {
                splitBet = budget;
            }

        } else {
            currentBet *= 2;
            if (currentBet > budget + splitBet) {
                currentBet = budget;
            }
        }
    }

    public void cleanHand() {
        hand.clean();
        splitHand = null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(getName())
                .append(System.lineSeparator());

        s.append("Your bet is ")
                .append(getCurrentBet())
                .append(" and your budget is ")
                .append(budget)
                .append(".")
                .append(System.lineSeparator());

        if (!isPlayingAsSplit()) {
            s.append("Your current hand is:")
                    .append(System.lineSeparator())
                    .append(hand)
                    .append("with sum of ")
                    .append(hand.getSum())
                    .append(".");

        } else {
            s.append("Your split hand is:")
                    .append(System.lineSeparator())
                    .append(splitHand)
                    .append("with sum of ")
                    .append(splitHand.getSum())
                    .append(".");
        }

        return s.toString();
    }
}
