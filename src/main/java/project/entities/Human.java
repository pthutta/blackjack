package project.entities;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Human extends Player {
    private boolean isPlaying = true;
    private boolean hasSplit = false;
    private int currentBet;
    private int insurance;
    private int budget;

    public Human(String name, int budget) {
        super(name);

        this.budget = budget;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setHasSplit(boolean hasSplit) {
        this.hasSplit = hasSplit;
    }

    public boolean canSplit() {
        return !hasSplit && hand.canSplit();
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
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
        if (hand.addCard(card)) {
            setBusted(true);
            isPlaying = false;
            return;
        }
    }

    public void stand() {
        isPlaying = false;
    }

    public void surrender() {
        isPlaying = false;
        addToBudget(-currentBet / 2);
        currentBet = 0;
    }

    public Card split() {
        return hand.split();
    }

    public void doubleDown() {
        currentBet *= 2;
        if (currentBet > budget) {
            currentBet = budget;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(getName())
                .append(System.lineSeparator());

        s.append("Your bet is ")
                .append(currentBet)
                .append(" and your budget is ")
                .append(budget)
                .append(".")
                .append(System.lineSeparator());
        s.append("Your current hand is:")
                .append(System.lineSeparator())
                .append(hand)
                .append("with sum of ")
                .append(hand.getSum())
                .append(".");

        return s.toString();
    }
}
