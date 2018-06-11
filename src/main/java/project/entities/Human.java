package project.entities;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Human extends Player {
    private int currentBet;
    private int insurance;
    private int budget;

    private Hand splitHand = null;
    private int splitBet;
    private boolean playingAsSplit = false;
    private boolean splitBusted = false;

    public Human(String name, int budget) {
        super(name);

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

    public boolean isBusted() {
        if (isPlayingAsSplit()) {
            return splitBusted;

        } else {
            return super.isBusted();
        }
    }

    public int getHandSum() {
        if (isPlayingAsSplit()) {
            return splitHand.getSum();

        } else {
            return super.getHandSum();
        }
    }

    public boolean hasBlackjack() {
        if (isPlayingAsSplit()) {
            return splitHand.isBlackjack();

        } else {
            return super.hasBlackjack();
        }
    }

    public void setCurrentBet(int currentBet) {
        if (isPlayingAsSplit()) {
            this.splitBet = currentBet;

        } else {
            this.currentBet = currentBet;
        }
    }

    public int getBudget() {
        return budget;
    }

    public void addToBudget(int value) {
        this.budget += value;
    }

    public int getCardCount() {
        return hand.getCardCount();
    }

    @Override
    public void hit(Card card) {
        Hand playingHand = (isPlayingAsSplit()) ? splitHand : hand;

        if (playingHand.addCard(card)) {
            setBusted(true);
            return;
        }
    }

    public void surrender() {
        if (isPlayingAsSplit()) {
            addToBudget(-splitBet / 2);
            splitBet = 0;

        } else {
            addToBudget(-currentBet / 2);
            currentBet = 0;
        }
    }

    public void split(Deck cardDeck) {
        if (!canSplit()) {
            return;
        }

        Card card = hand.split();
        if (card != null) {
            splitHand = new Hand();
            splitHand.addCard(card);
            splitHand.addCard(cardDeck.draw());

            if (currentBet * 2 > budget) {
                splitBet = budget - currentBet;
            } else {
                splitBet = currentBet;
            }

            hand.addCard(cardDeck.draw());
        }
    }

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
