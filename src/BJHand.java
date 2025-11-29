/** 
 * This file will have the logic for a BlackJack hand; involves adding a card,
 * listing what cards are in the hand, etc.
 * **/ 
import java.util.ArrayList;
import java.util.List;

public class Hand 
{
    private final List<Card> cards;

    public Hand()
    {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card)
    {
        cards.add(card);
    }
    
    public List<Card> getCards()
    {
        return cards;
    }

    public int calculateValue() //calculates value of hand, using the 'best' value if there are Aces in hand
    {   
        int total = 0;
        int aces = 0;
        for (Card c : cards)
        {
            int value = c.getValue();
            if (c.getRank() == Card.Rank.ACE)
            {
                aces++;
            }
            total += value;
        }

        //here is where aces get tested for 'best' value

        while (total > 21 && aces > 0) //if an ace is causing a bust
        {
            total -=10; //changes ace value from 11 to 1
            aces--;
        }
        return total;
    }

    public void clear() //clears deck in case of new round
    {
        cards.clear();
    }
}
