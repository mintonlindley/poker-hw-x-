/**
 * Black Jack Controller
 * Will manage the logic of the Blackjack game itself, acting as bridge between
 * the GUI and the other logic classes (Card, Deck, Hand, etc)
 */

public class BJController
{
    //initialize intial state of variables
    private final Deck deck;
    private final Hand dealerHand;
    private final Hand playerHand;

    //create flags to check when to stop/start game
    private boolean playerTurnActive;
    private boolean gameInProgress;

    public BJController()
    {
        this.deck = new Deck();
        this.dealerHand = new Hand();
        this.playerHand = new Hand();
        this.playerTurnActive = false;
        this.gameInProgress = false;
    }

    public void startNewGame()
    {
        //cleans up prior game hands
        dealerHand.clear();
        playerHand.clear();
        deck.reset();
        deck.shuffle();

        //Initial dealing
        playerHand.addCard(deck.dealOne());
        dealerHand.addCard(deck.dealOne());
        playerHand.addCard(deck.dealOne());
        dealerHand.addCard(deck.dealOne());

        playerTurnActive = true;
        gameInProgress = true;
    }

    public Card playerHit()
    {
        if (!playerTurnActive || !gameInProgress) return null; //if player tries to take invalid action don't do anything

        Card newCard = deck.dealOne();
        playerHand.addCard(newCard);

        if (playerHand.calculateValue() >=21)
        {
            playerTurnActive = false; //if player reaches 21 or over, stop turn
        }
        return newCard;
    }

    public void playerStand()
    {
        playerTurnActive = false;
    }

    public int dealerPlay()
    {
        if (!gameInProgress) return 0;

        //standard dealer logic; will hit on 16 or less, then stand once at 17
        //or greater.
        while (dealerHand.calculateValue() < 17)
        {
            dealerHand.addCard(deck.dealOne());
        }
        gameInProgress = false;
        return dealerHand.calculateValue();
    }

    //calculates values and if either player broke, then determines winner
    public String determineWinner()
    {
        int playerValue = playerHand.calculateValue();
        int dealerValue = dealerHand.calculateValue();

        if (playerValue > 21)
        {
            return "Bust! Dealer wins.";
        }

        if (dealerValue > 21)
        {
            return "Dealer busts! Player wins.";
        }

        if (playerValue == dealerValue)
        {
            return "It's a tie!";
        }

        else if (playerValue > dealerValue)
        {
            return "Player wins!";
        }

        else 
            return "Dealer wins!";
    }

    //accessors in case GUI needs to display info:
    public Hand getPlayerHand() {return playerHand; }
    public Hand getDealerHand() {return dealerHand; }
    public boolean isPlayerTurnActive() {return playerTurnActive; }
    public boolean isGameInProgress() { return gameInProgress; }
}
