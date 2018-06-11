package project;

import org.junit.jupiter.api.Test;
import project.entities.Card;
import project.entities.Deck;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Hutta
 * @version 1.0  11.6.2018
 */
public class DeckTests {

    @Test
    public void emptyDeck() {
        Deck deck = new Deck(1);
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < 52; i++) {
            cards.add(deck.draw());
        }

        assertThrows(IndexOutOfBoundsException.class, deck::draw);
        assertTrue(distinctCards(cards));

        Card discarded = cards.get(0);
        deck.discard(discarded);
        assertEquals(deck.draw(), discarded);
    }

    public static boolean distinctCards(List<Card> cards){
        Set<Card> foundCards = new HashSet<>();
        for (Card card : cards) {
            if(foundCards.contains(card)){
                return false;
            }
            foundCards.add(card);
        }
        return true;
    }
}
