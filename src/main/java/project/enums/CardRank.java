package project.enums;

/**
 * @author Peter Hutta
 * @version 1.0  10.6.2018
 */
public enum CardRank {
    NUM2(2, "2"),
    NUM3(3, "3"),
    NUM4(4, "4"),
    NUM5(5, "5"),
    NUM6(6, "6"),
    NUM7(7, "7"),
    NUM8(8, "8"),
    NUM9(9, "9"),
    NUM10(10, "10"),
    JACK(10, "jack"),
    QUEEN(10, "queen"),
    KING(10, "king"),
    ACE1(1, "ace (1)"),
    ACE11(11, "ace (11)");

    private final int value;
    private final String name;

    CardRank(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
