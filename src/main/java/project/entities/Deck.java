package project.entities;

import project.entities.Card;
import project.enums.CardRank;
import project.enums.CardSuit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Peter Hutta
 * @version 1.0  10.6.2018
 */
public class Deck {
    private List<Card> cards = new LinkedList<>();
    private List<Card> discardPile = new LinkedList<>();

    public Deck(int deckCount) {
        for (int i = 0; i < deckCount; i++) {
            addDeck();
        }

        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.size() == 0) {
            shuffleDiscardToCards();
        }
        return cards.remove(cards.size() - 1);
    }

    public void discard(Card card) {
        discardPile.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    private void shuffleDiscardToCards() {
        Collections.shuffle(discardPile);
        cards.addAll(discardPile);
        discardPile.clear();
    }

    private void addDeck() {
        CardSuit[] suits = new CardSuit[] {CardSuit.CLUBS, CardSuit.DIAMONDS, CardSuit.HEARTS, CardSuit.SPADES};
        CardRank[] ranks = new CardRank[] {CardRank.NUM2, CardRank.NUM3, CardRank.NUM4, CardRank.NUM5,
                CardRank.NUM6, CardRank.NUM7, CardRank.NUM8, CardRank.NUM9, CardRank.NUM10, CardRank.JACK,
                CardRank.QUEEN, CardRank.KING, CardRank.ACE11};

        for (CardSuit suit : suits) {
            for (CardRank rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
    }
}
