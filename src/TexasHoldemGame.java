import java.util.*;
//this is the game logic for texas holdem
public class TexasHoldemGame 
{
    private Deck deck = new Deck();
    private List<Card> community = new ArrayList<>();
    private List<Card> playerHole = new ArrayList<>();
    private List<Card> computerHole = new ArrayList<>();
    
    //start w 5 of each so total is $705
    private int playerWhiteChips = 5;
    private int playerRedChips = 5;
    private int playerBlueChips = 5;
    private int playerGreenChips = 5;
    private int playerBlackChips = 5;
    
    private int computerWhiteChips = 5;
    private int computerRedChips = 5;
    private int computerBlueChips = 5;
    private int computerGreenChips = 5;
    private int computerBlackChips = 5;
    
    //each game records
    private int pot = 0;
    private int currentBet = 0;
    private int playerBetThisRound = 0;
    private int computerBetThisRound = 0;
    
    //starting declarations
    public enum Stage { PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, GAME_OVER }
    private Stage currentStage = Stage.PRE_FLOP;
    private String lastAction = "";

    public TexasHoldemGame() 
    {
        deck.shuffle();
    }

    // Start a new hand
    public void startNewHand() 
    {
        deck.reset();
        deck.shuffle();
        community.clear();
        playerHole.clear();
        computerHole.clear();
        
        pot = 0;
        playerBetThisRound = 0;
        computerBetThisRound = 0;
        currentBet = 0;
        currentStage = Stage.PRE_FLOP;
        lastAction = "";
        
        playerHole = deck.deal(2);
        computerHole = deck.deal(2);
    }

    //human players actions: fold, call, and raise
    public String fold() 
    {
        lastAction = "You folded. Computer wins $" + pot;
        addComputerChips(pot);
        
        if (getPlayerTotalChips() <= 0)
            currentStage = Stage.GAME_OVER;
        else if (getComputerTotalChips() <= 0)
            currentStage = Stage.GAME_OVER;
        
        return lastAction;
    }

    public String call() 
    {
        int amountToCall = currentBet - playerBetThisRound;
        
        //if player doesnt have enough chips
        if (amountToCall > getPlayerTotalChips()) 
        {
            lastAction = "Not enough chips!";
            return lastAction;
        }
        
        removePlayerChips(amountToCall);
        playerBetThisRound += amountToCall;
        pot+=amountToCall;
        
        if (amountToCall > 0) 
            lastAction = "You called $" + amountToCall;
        else 
            lastAction = "You checked";
        
        return lastAction;
    }

    public String raise(int raiseAmount) 
    {
        int totalNeeded = (currentBet - playerBetThisRound) + raiseAmount;
        
        if (totalNeeded > getPlayerTotalChips()) 
        {
            lastAction = "Not enough chips to raise!";
            return lastAction;
        }
        
        removePlayerChips(totalNeeded);
        playerBetThisRound+=totalNeeded;
        pot+=totalNeeded;
        currentBet = playerBetThisRound;
        
        lastAction = "You raised to $" + currentBet;
        return lastAction;
    }

    //computer player actions: based on complete randomness
    public String computerAction() {
        Random rand = new Random();
        int decision = rand.nextInt(3);
        int amountToCall = currentBet - computerBetThisRound;
        
        switch (decision) 
        {
            //fold
            case 0:
                if (amountToCall > 0 && rand.nextDouble() < 0.2) 
                {
                    lastAction = "Computer folded. You win $" + pot + "!";
                    addPlayerChips(pot);
                    if (getPlayerTotalChips() <= 0 || getComputerTotalChips() <= 0)
                        currentStage = Stage.GAME_OVER;
                    return lastAction;
                }
            //call
            case 1:
                if (amountToCall > 0) 
                {
                    if (getComputerTotalChips() >= amountToCall) 
                    {
                        removeComputerChips(amountToCall);
                        computerBetThisRound += amountToCall;
                        pot += amountToCall;
                        lastAction = "Computer called";
                    } 
                    else 
                    {
                        lastAction = "Computer is all-in!";
                        pot += getComputerTotalChips();
                        computerBetThisRound += getComputerTotalChips();
                        removeComputerChips(getComputerTotalChips());
                    }
                } 
                else
                    lastAction = "Computer checked";

                return lastAction;

            //raise...always raises by 10 or whatever computer has left if less than 10
            case 2:
                int raiseAmt = 10;
                int computerChips = getComputerTotalChips();

                int actualRaise = Math.min(raiseAmt, computerChips);

                removeComputerChips(actualRaise);
                computerBetThisRound += actualRaise;
                pot += actualRaise;
                currentBet = computerBetThisRound;

                lastAction = "Computer raised by $" + actualRaise;
                return lastAction;
        }
        
        return lastAction;
    }

    //moving on to next step
    public String advanceStage() 
    {
        playerBetThisRound = 0;
        computerBetThisRound = 0;
        currentBet = 0;
        
        switch (currentStage) 
        {
            case PRE_FLOP:
                community.addAll(deck.deal(3));
                currentStage = Stage.FLOP;
                return "FLOP";
                
            case FLOP:
                community.addAll(deck.deal(1));
                currentStage = Stage.TURN;
                return "TURN";
                
            case TURN:
                community.addAll(deck.deal(1));
                currentStage = Stage.RIVER;
                return "RIVER";
                
            case RIVER:
                currentStage = Stage.SHOWDOWN;
                return "SHOWDOWN";
                
            default:
                return "";
        }
    }

    //this is last round where winner is determined
    public String showdown() 
    {
        ChecksHand.HandEvaluation playerEval = ChecksHand.evaluateHand(playerHole, community);
        ChecksHand.HandEvaluation computerEval = ChecksHand.evaluateHand(computerHole, community);
        
        String result = "Your hand: " + playerEval.handName + "\n";
        result += "Computer's hand: " + computerEval.handName + "\n\n";
        
        if (playerEval.score > computerEval.score) 
        {
            result += "YOU WIN $" + pot + "!";
            addPlayerChips(pot);
        } 
        else if (computerEval.score > playerEval.score) 
        {
            result += "COMPUTER WINS $" + pot;
            addComputerChips(pot);
        } 
        else 
        {
            result += "TIE! Pot split";
            int half = pot / 2;
            addPlayerChips(half);
            addComputerChips(pot - half);
        }
        
        if (getPlayerTotalChips() <= 0 || getComputerTotalChips() <= 0) 
        {
            currentStage = Stage.GAME_OVER;
        }
        
        return result;
    }

    // if raised
    public boolean raised() 
    {
        return currentBet > playerBetThisRound;
    }

    //getters
    public List<Card> getCommunity() { return new ArrayList<>(community); }
    public List<Card> getPlayerHole() { return new ArrayList<>(playerHole); }
    public List<Card> getComputerHole() { return new ArrayList<>(computerHole); }
    public int getPot() { return pot; }
    public int getCurrentBet() { return currentBet; }
    public int getPlayerBetThisRound() { return playerBetThisRound; }
    public int getComputerBetThisRound() { return computerBetThisRound; }
    public Stage getCurrentStage() { return currentStage; }
    public int getPlayerWhiteChips() { return playerWhiteChips; }
    public int getPlayerRedChips() { return playerRedChips; }
    public int getPlayerBlueChips() { return playerBlueChips; }
    public int getPlayerGreenChips() { return playerGreenChips; }
    public int getPlayerBlackChips() { return playerBlackChips; }
    public int getComputerWhiteChips() { return computerWhiteChips; }
    public int getComputerRedChips() { return computerRedChips; }
    public int getComputerBlueChips() { return computerBlueChips; }
    public int getComputerGreenChips() { return computerGreenChips; }
    public int getComputerBlackChips() { return computerBlackChips; }
    
    public int getPlayerTotalChips() 
    {
        return playerWhiteChips * 1 + playerRedChips * 5 + playerBlueChips * 10 + playerGreenChips * 25 + playerBlackChips * 100;
    }

    public int getComputerTotalChips()
    { 
        return computerWhiteChips * 1 + computerRedChips * 5 + computerBlueChips * 10 + computerGreenChips * 25 + computerBlackChips * 100;
    }

    public int getAmountToCall() 
    {
        return currentBet - playerBetThisRound;
    }

    public boolean isGameOver() 
    {
        return currentStage == Stage.GAME_OVER;
    }

    //removing and adding chips
    private boolean removePlayerChips(int amount) 
    {
        int remaining = amount;
        while (remaining >= 100 && playerBlackChips > 0) 
        {
            playerBlackChips--;
            remaining -= 100;
        }
        while (remaining >= 25 && playerGreenChips > 0) 
        {
            playerGreenChips--;
            remaining -= 25;
        }
        while (remaining >= 10 && playerBlueChips > 0) 
        {
            playerBlueChips--;
            remaining -= 10;
        }
        while (remaining >= 5 && playerRedChips > 0) 
        {
            playerRedChips--;
            remaining -= 5;
        }
        while (remaining >= 1 && playerWhiteChips > 0) 
        {
            playerWhiteChips--;
            remaining -= 1;
        }
        return remaining == 0;
    }

    private boolean removeComputerChips(int amount) 
    {
        int remaining = amount;
        while (remaining >= 100 && computerBlackChips > 0) 
        {
            computerBlackChips--;
            remaining -= 100;
        }
        while (remaining >= 25 && computerGreenChips > 0) 
        {
            computerGreenChips--;
            remaining -= 25;
        }
        while (remaining >= 10 && computerBlueChips > 0) 
        {
            computerBlueChips--;
            remaining -= 10;
        }
        while (remaining >= 5 && computerRedChips > 0) 
        {
            computerRedChips--;
            remaining -= 5;
        }
        while (remaining >= 1 && computerWhiteChips > 0) 
        {
            computerWhiteChips--;
            remaining -= 1;
        }
        return remaining == 0;
    }

    private void addPlayerChips(int amount) 
    {
        playerBlackChips += amount / 100;
        amount %= 100;
        playerGreenChips += amount / 25;
        amount %= 25;
        playerBlueChips += amount / 10;
        amount %= 10;
        playerRedChips += amount / 5;
        amount %= 5;
        playerWhiteChips += amount;
    }

    private void addComputerChips(int amount) 
    {
        computerBlackChips += amount / 100;
        amount %= 100;
        computerGreenChips += amount / 25;
        amount %= 25;
        computerBlueChips += amount / 10;
        amount %= 10;
        computerRedChips += amount / 5;
        amount %= 5;
        computerWhiteChips += amount;
    }
}
