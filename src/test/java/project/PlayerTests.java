package project;

import org.junit.jupiter.api.Test;
import project.entities.Card;
import project.entities.Player;
import project.enums.CardRank;
import project.enums.CardSuit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Hutta
 * @version 1.0  11.6.2018
 */
public class PlayerTests {

    @Test
    public void createPlayer() {
        Player player = new Player("Johnny", 1000);

        assertEquals(player.getName(), "Johnny");
        assertEquals(player.getBudget(), 1000);

        assertFalse(player.isSplit());
        assertFalse(player.isBusted());
        assertFalse(player.isPlayingAsSplit());
        assertFalse(player.hasBlackjack());

        assertEquals(player.getCardCount(), 0);
        assertEquals(player.getHandSum(), 0);

        player.setInsurance(10);
        assertEquals(player.getInsurance(), 10);
    }

    @Test
    public void createPlayer_NullName() {
        assertThrows(IllegalArgumentException.class, () -> new Player(null, 1000));
    }

    @Test
    public void createPlayer_EmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Player("", 1000));
    }

    @Test
    public void createPlayer_NegativeBudget() {
        assertThrows(IllegalArgumentException.class, () -> new Player("Johnny", -1));
    }

    @Test
    public void hit_Blackjack() {
        Player player = new Player("Johnny", 1000);

        player.hit(new Card(CardRank.ACE11, CardSuit.DIAMONDS));
        player.hit(new Card(CardRank.JACK, CardSuit.DIAMONDS));
        assertTrue(player.hasBlackjack());
        assertEquals(player.getCardCount(), 2);

        player.hit(new Card(CardRank.QUEEN, CardSuit.DIAMONDS));
        assertFalse(player.hasBlackjack());
        assertEquals(player.getCardCount(), 3);
    }

    @Test
    public void hit_Busted() {
        Player player = new Player("Johnny", 1000);

        player.hit(new Card(CardRank.QUEEN, CardSuit.DIAMONDS));
        player.hit(new Card(CardRank.JACK, CardSuit.DIAMONDS));
        player.hit(new Card(CardRank.ACE11, CardSuit.DIAMONDS));
        player.hit(new Card(CardRank.NUM2, CardSuit.DIAMONDS));

        assertTrue(player.isBusted());
    }

    @Test
    public void getSum() {
        Player player = new Player("Johnny", 1000);

        player.hit(new Card(CardRank.ACE11, CardSuit.DIAMONDS));
        assertEquals(player.getHandSum(), 11);

        player.hit(new Card(CardRank.NUM10, CardSuit.DIAMONDS));
        assertEquals(player.getHandSum(), 21);

        player.hit(new Card(CardRank.NUM5, CardSuit.DIAMONDS));
        assertEquals(player.getHandSum(), 16);
    }

    @Test
    public void surrender() {
        Player player = new Player("Johnny", 1000);
        player.setCurrentBet(100);
        player.surrender();
        assertEquals(player.getBudget(), 950);
    }

    @Test
    public void doubleDown() {
        Player player = new Player("Johnny", 300);
        player.setCurrentBet(100);

        player.doubleDown();
        assertEquals(player.getCurrentBet(), 200);

        player.doubleDown();
        assertEquals(player.getCurrentBet(), 300);
    }

    @Test
    public void split() {
        Player player = new Player("Johnny", 300);
        player.setCurrentBet(200);

        player.hit(new Card(CardRank.NUM5, CardSuit.DIAMONDS));
        player.hit(new Card(CardRank.NUM5, CardSuit.DIAMONDS));

        assertTrue(player.canSplit());
        player.split(new Card(CardRank.NUM5, CardSuit.HEARTS), new Card(CardRank.ACE11, CardSuit.HEARTS));
        assertFalse(player.canSplit());
        assertTrue(player.isSplit());

        player.hit(new Card(CardRank.NUM3, CardSuit.DIAMONDS));
        assertEquals(player.getCardCount(), 3);
        assertEquals(player.getHandSum(), 13);
        assertEquals(player.getCurrentBet(), 200);

        player.setPlayingAsSplit(true);
        assertEquals(player.getCardCount(), 2);
        assertEquals(player.getHandSum(), 16);
        assertEquals(player.getCurrentBet(), 100);
    }
}
