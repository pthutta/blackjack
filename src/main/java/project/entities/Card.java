package project.entities;

import project.enums.CardRank;
import project.enums.CardSuit;

import java.util.Objects;

/**
 * Class represents card with it's rank and suit.
 *
 * @author Peter Hutta
 * @version 1.0  10.6.2018
 */
public class Card {
    private CardRank rank;
    private CardSuit suit;

    public Card(CardRank rank, CardSuit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public CardRank getRank() {
        return rank;
    }

    public void setRank(CardRank rank) {
        this.rank = rank;
    }

    public CardSuit getSuit() {
        return suit;
    }

    public void setSuit(CardSuit suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return getRank() == card.getRank() &&
                getSuit() == card.getSuit();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getRank(), getSuit());
    }
}
