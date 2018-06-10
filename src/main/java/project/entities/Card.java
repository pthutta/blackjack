package project.entities;

import project.enums.CardRank;
import project.enums.CardSuit;

/**
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
}
