/**
 * Card.java
 *
 * Models a playing card with both a suit and rank.
 *
 * Images are provided for the front of the 52-card deck, as well as the back.
 * These will be implemented in another file.
 * 
 */

public class Card {
    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit s, Rank r) {
        this.suit = s;
        this.rank = r;
    }

    public Suit getSuit() 
    { 
        return suit; 
    }
    public Rank getRank() 
    { 
        return rank; 
    }

    public int getValue() 
    {
        return switch(rank) 
        {
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
            case EIGHT -> 8;
            case NINE -> 9;
            case TEN, JACK, QUEEN, KING -> 10;
            case ACE -> 11;
        };
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    } // will return "ace of spades" for example (rather than typical gibberish)
}