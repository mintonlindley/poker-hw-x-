/**
 * Deck.java
 *
 * 52-card deck implementation. 
 * Contains shuffle and deal operations.
 * 
 */
import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private int index = 0; // keeps track of which card should be dealt next from the deck

    public Deck() {
        reset();
    }

    // adding the 52 cards into the deck
    public final void reset() 
    {
        cards.clear();
        for (Card.Suit s : Card.Suit.values()) {
            for (Card.Rank r : Card.Rank.values()) {
                cards.add(new Card(s, r));
            }
        }
        index = 0;
    }

    // shuffle deck and reset index
    public void shuffle() 
    {
        Collections.shuffle(cards);
        index = 0;
    }

    // deal up to n amount of cards
    // fewer cards will be returned as the deck runs out 
    public List<Card> deal(int n) 
    {
        List<Card> hand = new ArrayList<>();
        for (int i = 0; i < n && index < cards.size(); i++) 
        {
            hand.add(cards.get(index++));
        }
        return hand;
    }

    public int remaining() 
    {
        return cards.size() - index;
    }

    // helper to simplify code
    public Card dealOne() 
    {
        List<Card> hand = deal(1);
        return hand.isEmpty() ? null : hand.get(0);
    }
}