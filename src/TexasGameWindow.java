import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class TexasGameWindow extends JFrame 
{
    private TexasHoldemGame game;
    private TexasPanel tablePanel;
    private Betting bettingPanel;
    private JTextArea chipInfoArea;
    private JButton startHandButton;

    public TexasGameWindow() 
    {
        super("Texas Hold'em Poker");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        game = new TexasHoldemGame();
        tablePanel = new TexasPanel();
        add(tablePanel, BorderLayout.CENTER);

        //right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 700));
        
        chipInfoArea = new JTextArea(25, 25);
        chipInfoArea.setEditable(false);
        chipInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane chipScroll = new JScrollPane(chipInfoArea);
        rightPanel.add(chipScroll);
        add(rightPanel, BorderLayout.EAST);

        //bottom panel with buttons
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        startHandButton = new JButton("Start Hand");
        startHandButton.addActionListener(e -> startNewHand());
        controls.add(startHandButton);

        bettingPanel = new Betting(this);
        
        //add button listeners
        bettingPanel.getBetButton().addActionListener(e -> handleBet());
        bettingPanel.getCallButton().addActionListener(e -> handleCall());
        bettingPanel.getFoldButton().addActionListener(e -> handleFold());
        bettingPanel.getRaiseButton().addActionListener(e -> handleRaise());
        
        controls.add(bettingPanel);

        //table color options
        JComboBox<String> bgColorBox = new JComboBox<>(new String[]{"Green", "Blue", "Red", "Orange", "Magenta"});
        bgColorBox.addActionListener(e -> 
        {
            String color = (String) bgColorBox.getSelectedItem();
            if(color.equals("Green"))
                tablePanel.setBgColor(Color.GREEN);
            else if(color.equals("Blue"))
                tablePanel.setBgColor(Color.BLUE);
            else if(color.equals("Red"))
                tablePanel.setBgColor(Color.RED);
            else if(color.equals("Orange"))
                tablePanel.setBgColor(Color.ORANGE);
            else if(color.equals("Magenta"))
                tablePanel.setBgColor(Color.MAGENTA);
        });
        controls.add(new JLabel("Table Color:"));
        controls.add(bgColorBox);

        //card back color options
        //INSERT CARD BACK COLOR OPTIONS HERE
        JComboBox<String> backBox = new JComboBox<>(new String[]{"Red","Blue","Green","Orange","Purple"});
        backBox.addActionListener(e -> tablePanel.setBackColor((String) backBox.getSelectedItem()));
        controls.add(new JLabel("Card Back:"));
        controls.add(backBox);

        add(controls, BorderLayout.SOUTH);

        updateChipDisplay();
        setVisible(true);
    }

    //start a new hand
    private void startNewHand() 
    {
        game.startNewHand();
        tablePanel.setPlayerHole(game.getPlayerHole());
        tablePanel.setComputerHole(game.getComputerHole(), false);
        tablePanel.setCommunity(game.getCommunity());
        tablePanel.repaint();
        updateChipDisplay();
        bettingPanel.resetPot();
    }

    //player actions: bet, call, fold, raise
    private void handleBet() 
    {
        String result = game.raise(5);
        chipInfoArea.append("You bet $5\n");
        updateChipDisplay();
        computerTurn();
    }

    private void handleCall() 
    {
        String result = game.call();
        chipInfoArea.append("You called\n");
        updateChipDisplay();
        computerTurn();
    }

    public void handleFold() 
    {
        game.fold();
        tablePanel.setComputerHole(game.getComputerHole(), true);
        tablePanel.repaint();
        updateChipDisplay();
        JOptionPane.showMessageDialog(this, "You folded! Computer wins $" + game.getPot());
    }

    private void handleRaise() 
    {
        String result = game.raise(10);
        chipInfoArea.append("You raised by $10\n");
        updateChipDisplay();
        computerTurn();
    }

    //computer turn
    private void computerTurn() 
    {
        String computerAction = game.computerAction();
        updateChipDisplay();
        chipInfoArea.append("\nComputer: " + computerAction + "\n");
        
        if (computerAction.contains("folded")) 
        {
            tablePanel.setComputerHole(game.getComputerHole(), true);
            tablePanel.repaint();
            JOptionPane.showMessageDialog(this, computerAction);
            return;
        }
        
        if (game.raised()) 
            return; 
        
        //move to next stage
        String stage = game.advanceStage();
        chipInfoArea.append(stage + "\n");
        
        if (stage.equals("SHOWDOWN")) 
            showdown();
        else 
        {
            tablePanel.setCommunity(game.getCommunity());
            tablePanel.repaint();
        }
    }

    //showdown to see who wins
    private void showdown() 
    {
        tablePanel.setComputerHole(game.getComputerHole(), true);
        tablePanel.repaint();
        
        String result = game.showdown();
        updateChipDisplay();
        bettingPanel.updatePotDisplay(0);
        
        JOptionPane.showMessageDialog(this, result);
        
        if (game.isGameOver()) 
        {
            if (game.getPlayerTotalChips() <= 0) 
                JOptionPane.showMessageDialog(this, "Game Over! You're out of chips!");
            else 
                JOptionPane.showMessageDialog(this, "You win! Computer is out of chips!");
        }
    }

    //update the chip display on the right side
    private void updateChipDisplay() 
    {
        String display = "";
        
        display = display + "YOUR CHIPS\n";
        display = display + "White ($1):   " + game.getPlayerWhiteChips() + " = $" + (game.getPlayerWhiteChips() * 1) + "\n";
        display = display + "Red ($5):     " + game.getPlayerRedChips() + " = $" + (game.getPlayerRedChips() * 5) + "\n";
        display = display + "Blue ($10):   " + game.getPlayerBlueChips() + " = $" + (game.getPlayerBlueChips() * 10) + "\n";
        display = display + "Green ($25):  " + game.getPlayerGreenChips() + " = $" + (game.getPlayerGreenChips() * 25) + "\n";
        display = display + "Black ($100): " + game.getPlayerBlackChips() + " = $" + (game.getPlayerBlackChips() * 100) + "\n";
        display = display + "TOTAL: $" + game.getPlayerTotalChips() + "\n\n";
        
        display = display + "COMPUTER CHIPS\n";
        display = display + "White ($1):   " + game.getComputerWhiteChips() + " = $" + (game.getComputerWhiteChips() * 1) + "\n";
        display = display + "Red ($5):     " + game.getComputerRedChips() + " = $" + (game.getComputerRedChips() * 5) + "\n";
        display = display + "Blue ($10):   " + game.getComputerBlueChips() + " = $" + (game.getComputerBlueChips() * 10) + "\n";
        display = display + "Green ($25):  " + game.getComputerGreenChips() + " = $" + (game.getComputerGreenChips() * 25) + "\n";
        display = display + "Black ($100): " + game.getComputerBlackChips() + " = $" + (game.getComputerBlackChips() * 100) + "\n";
        display = display + "TOTAL: $" + game.getComputerTotalChips() + "\n\n";
        
        display = display + "POT INFO\n";
        display = display + "Pot: $" + game.getPot() + "\n";
        display = display + "Current Bet: $" + game.getCurrentBet() + "\n";
        display = display + "Your Bet: $" + game.getPlayerBetThisRound() + "\n";
        
        int toCall = game.getAmountToCall();
        display = display + "To Call: $" + toCall + "\n";
        display = display + "\nStage: " + game.getCurrentStage();
        
        chipInfoArea.setText(display);
        bettingPanel.updatePotDisplay(game.getPot());
    }

    private class TexasPanel extends JPanel 
    {
        private List<Card> communityCards = new ArrayList<>();
        private List<Card> playerCards = new ArrayList<>();
        private List<Card> computerCards = new ArrayList<>();
        private boolean showComputerCards = false;
        private String backColor = "Red";
        private Color bgColor = new Color(5, 100, 25);

        public void setCommunity(List<Card> c) 
        { 
            communityCards = new ArrayList<>(c); 
            repaint(); 
        }
        
        public void setPlayerHole(List<Card> c) 
        { 
            playerCards = new ArrayList<>(c); 
            repaint(); 
        }
        
        public void setComputerHole(List<Card> c, boolean show) 
        { 
            computerCards = new ArrayList<>(c);
            showComputerCards = show;
            repaint();
        }
        
        public void setBackColor(String color) 
        { 
            backColor = color; 
            repaint(); 
        }
        
        public void setBgColor(Color c) 
        { 
            bgColor = c; 
            repaint(); 
        }

        public void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(bgColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            int cardWidth = 120;
            int cardHeight = 170;
            int spacing = 15;

            //draw community cards in the middle
            if (communityCards.size() > 0) 
            {
                int totalWidth = communityCards.size() * (cardWidth + spacing) - spacing;
                int x = (getWidth() - totalWidth) / 2;
                int y = getHeight() / 3;
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.drawString("COMMUNITY CARDS", x, y - 15);
                
                for (int i = 0; i < communityCards.size(); i++) 
                {
                    BufferedImage img = CardImages.loadCardImage(communityCards.get(i));
                    if (img != null)
                        g2.drawImage(img, x + i * (cardWidth + spacing), y, cardWidth, cardHeight, this);
                }
            }

            //draw player cards at the bottom
            if (playerCards.size() > 0) 
            {
                int x = 40;
                int y = getHeight() - cardHeight - 40;
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString("YOUR CARDS", x, y - 10);
                
                for (int i = 0; i < playerCards.size(); i++) {
                    BufferedImage img = CardImages.loadCardImage(playerCards.get(i));
                    if (img != null)
                        g2.drawImage(img, x + i * (cardWidth + spacing), y, cardWidth, cardHeight, this);
                }
            }

            //draw computer cards at the top
            if (computerCards.size() > 0) 
            {
                int x = 40;
                int y = 50;
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString("COMPUTER'S CARDS", x, y - 10);
                
                for (int i = 0; i < computerCards.size(); i++) 
                {
                    BufferedImage img;
                
                    if (showComputerCards)
                        img = CardImages.loadCardImage(computerCards.get(i));   // face-up
                    else
                        img = CardImages.loadBackImage(backColor);               // face-down
                
                    if (img != null)
                        g2.drawImage(img, x + i * (cardWidth + spacing), y, cardWidth, cardHeight, this);
                }
            }
        }
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new TexasGameWindow());
    }
}



