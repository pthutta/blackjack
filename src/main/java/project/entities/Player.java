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

    public boolean isBusted() {
        return busted;
    }

    public void setBusted(boolean busted) {
        this.busted = busted;
    }

    abstract protected void hit();
}
