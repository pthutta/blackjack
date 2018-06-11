package project;

import org.junit.jupiter.api.Test;
import project.entities.Card;
import project.enums.CardRank;
import project.enums.CardSuit;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Peter Hutta
 * @version 1.0  11.6.2018
 */
public class CardTests {

    @Test
    void createCard() {
        Card card = new Card(CardRank.JACK, CardSuit.CLUBS);
        assertSame(card.getRank(), CardRank.JACK);
        assertSame(card.getSuit(), CardSuit.CLUBS);

        card.setRank(CardRank.ACE11);
        card.setSuit(CardSuit.DIAMONDS);

        assertSame(card.getRank(), CardRank.ACE11);
        assertSame(card.getSuit(), CardSuit.DIAMONDS);
    }
}
