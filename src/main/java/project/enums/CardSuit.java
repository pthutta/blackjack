package project.enums;

/**
 * @author Peter Hutta
 * @version 1.0  10.6.2018
 */
public enum CardSuit {
    CLUBS("clubs"),
    SPADES("spades"),
    DIAMONDS("diamonds"),
    HEARTS("hearts");

    private final String name;

    CardSuit(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
