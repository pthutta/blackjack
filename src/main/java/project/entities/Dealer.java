package project.entities;

/**
 * @author Peter Hutta
 * @version 1.0  9.6.2018
 */
public class Dealer extends Player {

    public Dealer(String name) {
        super(name);
    }

    public Card firstCard() {
        return hand.getCard(0);
    }

    @Override
    public void hit(Card card) {
        if (hand.addCard(card)) {
            setBusted(true);
            return;
        }
    }
}
