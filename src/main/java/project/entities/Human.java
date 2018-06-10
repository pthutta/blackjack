package project.entities;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Human extends Player {
    private boolean isPlaying = true;
    private int currentBet;
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

    @Override
    public void hit() {

    }

    public void stand() {

    }

    public void surrender() {

    }

    public void split() {

    }

    public void doubeDown() {

    }
}
