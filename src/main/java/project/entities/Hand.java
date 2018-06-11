package project.entities;

import project.enums.CardRank;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents all cards a player is currently holding.
 *
 * @author Peter Hutta
 * @version 1.0  10.6.2018
 */
public class Hand {
    public static final int HAND_LIMIT = 21;

    private List<Card> cards = new ArrayList<>();
    private boolean isSoft = false;

    /**
     * Adds card to hand. Changes hand to hard if it is already soft and it would bust.
     *
     * @param card card to add
     * @return true if hand busted, false otherwise
     */
    public boolean addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card is null");
        }

        cards.add(card);

        if (card.getRank() == CardRank.ACE11) {
            isSoft = true;
        }

        if (getSum() > HAND_LIMIT && isSoft()) {
            isSoft = false;
            lowerAceValue();
        }

        return getSum() > HAND_LIMIT;
    }

    public boolean isSoft() {
        return isSoft;
    }

    public int getSum() {
        int sum = 0;
        for (Card card : cards) {
            sum += card.getRank().getValue();
        }
        return sum;
    }

    public int getSize() {
        return cards.size();
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && getSum() == 21;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getRank().getValue() == cards.get(1).getRank().getValue();
    }

    public int getCardCount() {
        return cards.size();
    }

    /**
     * Splits hand and returns second card,
     * @return second card
     */
    public Card split() {
        if (canSplit()) {
            return cards.remove(1);
        }

        return null;
    }

    public void clean() {
        cards.clear();
    }

    private void lowerAceValue() {
        for (Card card : cards) {
            if (card.getRank() == CardRank.ACE11) {
                card.setRank(CardRank.ACE1);
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (Card card : cards) {
            s.append("\t")
                    .append(card)
                    .append(System.lineSeparator());
        }

        return s.toString();
    }
}
