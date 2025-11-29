/**
 * BlackjackWindow.java
 *
 * Blackjack layout including dealer and player cards.
 * Displays hit, Stand, Reset, and Menu buttons.
 * Players can choose the color of the background and the back of the cards.
 * Implements different poker chip colors and their corresponding monetary values.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

// Import necessary class files
import CardImages;
import ChipImages;
import Menu; 
import Deck;
import Card;

public class BlackjackWindow extends JFrame {

    private Deck deck = new Deck();
    private List<Card> dealerCards = new ArrayList<>();
    private List<Card> playerCards = new ArrayList<>();
    private BlackjackPanel tablePanel;

    public BlackjackWindow() {
        super("Blackjack");
        setSize(850, 650); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        deck.shuffle();

        tablePanel = new BlackjackPanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        JButton hit = new JButton("Hit");
        JButton stand = new JButton("Stand");
        JButton reset = new JButton("Reset");
        JButton back = new JButton("Menu");

        controls.add(hit);
        controls.add(stand);
        controls.add(reset);
        controls.add(back);

        // different colored chip buttons
        JButton chipWhite = new JButton("White $1");
        JButton chipRed   = new JButton("Red $5");
        JButton chipBlue  = new JButton("Blue $10");
        JButton chipGreen = new JButton("Green $25");
        JButton chipBlack = new JButton("Black $100");

        controls.add(chipWhite);
        controls.add(chipRed);
        controls.add(chipBlue);
        controls.add(chipGreen);
        controls.add(chipBlack);

        chipWhite.addActionListener(e -> tablePanel.addChip("White"));
        chipRed.addActionListener(e -> tablePanel.addChip("Red"));
        chipBlue.addActionListener(e -> tablePanel.addChip("Blue"));
        chipGreen.addActionListener(e -> tablePanel.addChip("Green"));
        chipBlack.addActionListener(e -> tablePanel.addChip("Black"));

        // pick the background color
        JComboBox<String> bgColorBox = new JComboBox<>(new String[]{"Green","Blue","Red","Orange","Purple"});
        bgColorBox.addActionListener(e -> {
            String color = (String) bgColorBox.getSelectedItem();
            // Use darker green for a better table look
            switch(color) {
                case "Green" -> tablePanel.setBgColor(new Color(0, 90, 20)); 
                case "Blue" -> tablePanel.setBgColor(Color.BLUE);
                case "Red" -> tablePanel.setBgColor(Color.RED);
                case "Orange" -> tablePanel.setBgColor(Color.ORANGE);
                case "Purple" -> tablePanel.setBgColor(Color.MAGENTA);
            }
        });
        controls.add(new JLabel("Table Color:"));
        controls.add(bgColorBox);

        // pick the color of the back of playing cards
        JComboBox<String> backBox = new JComboBox<>(new String[]{"Red","Blue","Green","Orange","Purple"});
        backBox.addActionListener(e -> tablePanel.setBackColor((String) backBox.getSelectedItem()));
        controls.add(new JLabel("Card Back:"));
        controls.add(backBox);

        add(controls, BorderLayout.SOUTH);

        // button actions
        hit.addActionListener(e -> {
            playerCards.add(deck.dealOne()); // using dealOne from Deck.java
            tablePanel.setPlayerCards(playerCards);
            
            // Check for bust
            if (tablePanel.handValue(playerCards) > 21) {
                // Automatically stand if busted
                stand.doClick(); 
            }
        });

        stand.addActionListener(e -> {
            // Disable game controls during dealer turn
            hit.setEnabled(false);
            stand.setEnabled(false);

            // Dealer plays
            while (tablePanel.handValue(dealerCards) < 17) {
                dealerCards.add(deck.dealOne()); // using dealOne from Deck.java
            }
            tablePanel.setDealerCards(dealerCards);
            
            // Determine winner and show result
            String result = determineWinner();
            // Display result as a centralized label on the table
            tablePanel.setResultText(result); 
        });

        reset.addActionListener(e -> {
            deck.reset();
            deck.shuffle();
            dealerCards.clear();
            playerCards.clear();

            // Deal two cards to the dealer
            dealerCards.addAll(deck.deal(2));
            // Deal two cards to the player
            playerCards.addAll(deck.deal(2));

            tablePanel.setDealerCards(dealerCards);
            tablePanel.setPlayerCards(playerCards);
            tablePanel.setResultText("Hit or Stand?");
            
            // Enable game controls
            hit.setEnabled(true);
            stand.setEnabled(true);
            
            // Check for immediate Blackjack
            if (tablePanel.handValue(playerCards) == 21) {
                stand.doClick();
            }
        });

        back.addActionListener(e -> {
            dispose();
            new Menu(); // Uses the real Menu.java class
        });

        // start automatically
        reset.doClick();

        setVisible(true);
    }

    /**
     * Determines the winner and returns the result message.
     */
    private String determineWinner() {
        int dealerValue = tablePanel.handValue(dealerCards);
        int playerValue = tablePanel.handValue(playerCards);

        if (playerValue > 21) {
            return "Player Busts! Dealer Wins.";
        } else if (dealerValue > 21) {
            return "Dealer Busts! Player Wins!";
        } else if (playerValue > dealerValue) {
            return "Player Wins!";
        } else if (dealerValue > playerValue) {
            return "Dealer Wins.";
        } else {
            return "Push (Tie).";
        }
    }


    // inner panel
    private class BlackjackPanel extends JPanel {

        private List<String> placedChips = new ArrayList<>();
        private int betAmount = 0;
        private String resultText = "Welcome to Blackjack!";

        public BlackjackPanel() {
            setLayout(new BorderLayout());
        }

        public void addChip(String chipColor) {
            placedChips.add(chipColor);
            switch (chipColor) {
                case "White" -> betAmount += 1;
                case "Red"   -> betAmount += 5;
                case "Blue"  -> betAmount += 10;
                case "Green" -> betAmount += 25;
                case "Black" -> betAmount += 100;
            }
            repaint();
        }

        private List<Card> dealer = new ArrayList<>();
        private List<Card> player = new ArrayList<>();
        private String backColor = "Red";
        private Color bgColor = new Color(0,90,20);

        public void setDealerCards(List<Card> c) { dealer = c; repaint(); }
        public void setPlayerCards(List<Card> c) { player = c; repaint(); }
        public void setBackColor(String color) { backColor = color; repaint(); }
        public void setBgColor(Color c) { bgColor = c; repaint(); }
        public void setResultText(String text) { resultText = text; repaint(); }


        //redunant of calculateValue in BJController class; can either be
        //adjusted or kept for convenience since it's being used in other
        //methods - Morgan 

        /**
         * Calculates the value of a hand, handling Aces (1 or 11).
         * @param cards The list of cards in the hand.
         * @return The highest legal hand value (<= 21).
         */
        public int handValue(List<Card> cards) {
            int total = 0;
            int aces = 0;
            for (Card c : cards) {
                // Use the getValue() method from the real Card.java
                int value = c.getValue(); 
                
                if (c.getRank() == Card.Rank.ACE) {
                    aces++;
                }
                total += value;
            }
            
            // Adjust for Aces (downgrade 11 to 1 if busted)
            while(total > 21 && aces > 0) { 
                total -= 10; 
                aces--; 
            }
            return total;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // 1. Draw Background
            g2.setColor(bgColor);
            g2.fillRect(0,0,getWidth(),getHeight());

            int w = 110;
            int h = 160;
            int spacing = 15;
            int x = 30;

            // 2. Display Result/Status Text
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.drawString(resultText, (getWidth() / 2) - (g2.getFontMetrics().stringWidth(resultText) / 2), getHeight() / 2);


            // 3. Dealer Cards and Value
            x = 30;
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            
            // Dealer value logic: only show full value if the game is over (resultText != "Hit or Stand?")
            boolean gameOver = !resultText.equals("Hit or Stand?");
            int dealerHandValue = gameOver ? handValue(dealer) : dealer.get(1).getValue(); 
            String dealerValueText = gameOver ? String.valueOf(dealerHandValue) : "?";
            
            g2.drawString("Dealer Hand: " + dealerValueText, 30, 60);

            for(int i=0;i<dealer.size();i++){
                // First card is face-down UNLESS game is over (gameOver is true)
                BufferedImage img = (i==0 && !gameOver) ? CardImages.loadBackImage(backColor)
                                                         : CardImages.loadCardImage(dealer.get(i));
                if(img != null) g2.drawImage(img, x, 80, w, h, this);
                x += w + spacing;
            }

            // 4. Player Cards and Value
            x = 30;
            int y = getHeight() - 200;
            g2.drawString("Your Hand: " + handValue(player), 30, y - 20);
            
            for(Card c : player){
                BufferedImage img = CardImages.loadCardImage(c);
                if(img != null) g2.drawImage(img, x, y, w, h, this);
                x += w + spacing;
            }

            // 5. Chips and Bet Amount
            int cx = getWidth() - 150;
            int cy = getHeight() - 200;

            for (String chip : placedChips) 
            {
                BufferedImage chipImg = ChipImages.loadChipImage(chip);
                if (chipImg != null)
                    g2.drawImage(chipImg, cx, cy, 60, 60, this);
                cy -= 70;
            }

            // amount to bet
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.drawString("Bet: $" + betAmount, cx - 20, cy - 20);
        }
    }
}
