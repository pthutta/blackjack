package project.entities;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public abstract class Player {
    private String name;
    private boolean busted = false;

    protected Hand hand = new Hand();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBusted() {
        return busted;
    }

    public void setBusted(boolean busted) {
        this.busted = busted;
    }

    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }

    public int getHandSum() {
        return hand.getSum();
    }

    abstract protected void hit(Card card);
}
