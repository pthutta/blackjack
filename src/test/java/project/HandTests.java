package project;

import org.junit.jupiter.api.Test;
import project.entities.Card;
import project.entities.Hand;
import project.enums.CardRank;
import project.enums.CardSuit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Hutta
 * @version 1.0  11.6.2018
 */
public class HandTests {

    @Test
    public void addCard_CardIsNull() {
        Hand hand = new Hand();

        assertThrows(IllegalArgumentException.class, () -> hand.addCard(null));
    }

    @Test
    public void isSoft() {
        Hand hand = new Hand();

        assertFalse(hand.addCard(new Card(CardRank.ACE11, CardSuit.DIAMONDS)));
        assertTrue(hand.isSoft());

        assertFalse(hand.addCard(new Card(CardRank.KING, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(new Card(CardRank.NUM3, CardSuit.DIAMONDS)));
        assertFalse(hand.isSoft());
    }

    @Test
    public void isBlackjack() {
        Hand hand = new Hand();
        assertEquals(hand.getSize(), 0);

        assertFalse(hand.addCard(new Card(CardRank.ACE11, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(new Card(CardRank.JACK, CardSuit.DIAMONDS)));
        assertTrue(hand.isBlackjack());
        assertEquals(hand.getSize(), 2);

        assertFalse(hand.addCard(new Card(CardRank.QUEEN, CardSuit.DIAMONDS)));
        assertFalse(hand.isBlackjack());
        assertEquals(hand.getSize(), 3);
    }

    @Test
    public void getSum() {
        Hand hand = new Hand();

        assertFalse(hand.addCard(new Card(CardRank.ACE11, CardSuit.DIAMONDS)));
        assertEquals(hand.getSum(), 11);

        assertFalse(hand.addCard(new Card(CardRank.NUM10, CardSuit.DIAMONDS)));
        assertEquals(hand.getSum(), 21);

        assertFalse(hand.addCard(new Card(CardRank.NUM5, CardSuit.DIAMONDS)));
        assertEquals(hand.getSum(), 16);
    }

    @Test
    public void getCard() {
        Hand hand = new Hand();

        Card card1 = new Card(CardRank.ACE11, CardSuit.DIAMONDS);
        Card card2 = new Card(CardRank.ACE1, CardSuit.DIAMONDS);

        assertFalse(hand.addCard(card1));
        assertFalse(hand.addCard(card2));

        assertEquals(hand.getCard(0), card1);
        assertEquals(hand.getCard(1), card2);
    }

    @Test
    public void split() {
        Hand hand = new Hand();

        assertFalse(hand.addCard(new Card(CardRank.NUM5, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(new Card(CardRank.NUM5, CardSuit.CLUBS)));
        assertTrue(hand.canSplit());

        assertFalse(hand.addCard(new Card(CardRank.NUM9, CardSuit.CLUBS)));
        assertFalse(hand.canSplit());

        hand.clean();
        assertEquals(hand.getSize(), 0);
        assertEquals(hand.getSum(), 0);

        assertFalse(hand.addCard(new Card(CardRank.NUM5, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(new Card(CardRank.ACE11, CardSuit.CLUBS)));
        assertFalse(hand.canSplit());

        hand.clean();

        Card card = new Card(CardRank.QUEEN, CardSuit.CLUBS);
        assertFalse(hand.addCard(new Card(CardRank.KING, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(card));
        assertTrue(hand.canSplit());

        assertEquals(hand.split(), card);
        assertFalse(hand.canSplit());
        assertEquals(hand.getSize(), 1);
        assertEquals(hand.getSum(), 10);
    }

    @Test
    public void addCard_Busted() {
        Hand hand = new Hand();

        assertFalse(hand.addCard(new Card(CardRank.QUEEN, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(new Card(CardRank.JACK, CardSuit.DIAMONDS)));
        assertFalse(hand.addCard(new Card(CardRank.ACE11, CardSuit.DIAMONDS)));
        assertTrue(hand.addCard(new Card(CardRank.NUM2, CardSuit.DIAMONDS)));
    }
}
