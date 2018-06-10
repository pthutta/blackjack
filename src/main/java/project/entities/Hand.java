package project.entities;

import project.entities.Card;
import project.enums.CardRank;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Hutta
 * @version 1.0  10.6.2018
 */
public class Hand {
    public static final int HAND_LIMIT = 21;

    private int sum = 0;
    private List<Card> cards = new ArrayList<>();
    private boolean isSoft = false;

    public boolean addCard(Card card) {
        cards.add(card);

        if (card.getRank() == CardRank.ACE11) {
            isSoft = true;
        }

        int value = card.getRank().getValue();
        if (sum + value > HAND_LIMIT && isSoft) {
            isSoft = false;
            lowerAceValue();
            sum -= 10;
        }

        sum += value;
        return sum > HAND_LIMIT;
    }

    public boolean isSoft() {
        return isSoft;
    }

    public int getSum() {
        return sum;
    }

    public boolean isBlackjack() {
        return cards.size() == 2 && sum == 21;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public boolean canSplit() {
        return cards.size() == 2 && cards.get(0).getRank().getValue() == cards.get(0).getRank().getValue();
    }

    public int getCardCount() {
        return cards.size();
    }

    public Card split() {
        if (canSplit()) {
            return cards.remove(1);
        }

        return null;
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
