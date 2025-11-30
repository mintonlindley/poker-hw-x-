import java.util.*;

public class ChecksHand 
{
    public static HandEvaluation evaluateHand(List<Card> hole, List<Card> board) {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(hole);
        allCards.addAll(board);
        
        Map<Card.Rank, Integer> rankCounts = new HashMap<>();
        Map<Card.Suit, Integer> suitCounts = new HashMap<>();
        
        for (int i = 0; i < allCards.size(); i++) 
        {
            Card card = allCards.get(i);

            rankCounts.put(card.getRank(), rankCounts.getOrDefault(card.getRank(), 0) + 1);
            suitCounts.put(card.getSuit(), suitCounts.getOrDefault(card.getSuit(), 0) + 1);
        }
        
        boolean hasFlush = suitCounts.values().stream().anyMatch(count -> count >= 5);
        boolean hasStraight = checkStraight(allCards);
        int maxOfKind = rankCounts.values().stream().max(Integer::compare).orElse(0);
        long pairs = rankCounts.values().stream().filter(count -> count == 2).count();
        boolean hasThreeOfKind = rankCounts.values().stream().anyMatch(count -> count == 3);
        
        // Determine hand ranking
        if (hasStraight && hasFlush) 
            return new HandEvaluation(8000, "Straight Flush");
        else if (maxOfKind == 4)
            return new HandEvaluation(7000, "Four of a Kind");
        else if (hasThreeOfKind && pairs >= 1)
            return new HandEvaluation(6000, "Full House");
        else if (hasFlush)
            return new HandEvaluation(5000, "Flush");
        else if (hasStraight)
            return new HandEvaluation(4000, "Straight");
        else if (maxOfKind == 3)
            return new HandEvaluation(3000, "Three of a Kind");
        else if (pairs >= 2)
            return new HandEvaluation(2000, "Two Pair");
        else if (pairs == 1)
            return new HandEvaluation(1000, "One Pair");
        else 
        {
            int highCard = 0;
            for (Card card : allCards)
                highCard = Math.max(highCard, getRankValue(card.getRank()));
            return new HandEvaluation(highCard, "High Card");
        }
    }

    private static boolean checkStraight(List<Card> cards) 
    {
        Set<Integer> values = new HashSet<>();
        for (int i = 0; i < cards.size(); i++) 
        {
            Card card = cards.get(i);
            values.add(getRankValue(card.getRank()));
        }
        
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        
        // this is for a normal straight
        int consecutive = 1;
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i) == sorted.get(i-1) + 1) {
                consecutive++;
                if (consecutive >= 5) return true;
            } else {
                consecutive = 1;
            }
        }
        
        // and this is if ace also counts
        if (values.contains(12) && values.contains(0) && values.contains(1) && values.contains(2) && values.contains(3)) 
                return true;
        return false;
    }

    
    private static int getRankValue(Card.Rank rank) 
    {
        return switch(rank) 
        {
            case TWO -> 0;
            case THREE -> 1;
            case FOUR -> 2;
            case FIVE -> 3;
            case SIX -> 4;
            case SEVEN -> 5;
            case EIGHT -> 6;
            case NINE -> 7;
            case TEN -> 8;
            case JACK -> 9;
            case QUEEN -> 10;
            case KING -> 11;
            case ACE -> 12;
        };
    }

    //only to hold the result really...
    public static class HandEvaluation 
    {
        public int score;
        public String handName;
        
        public HandEvaluation(int score, String handName) 
        {
            this.score = score;
            this.handName = handName;
        }
    }
}
