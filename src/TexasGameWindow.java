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

    //basic gui setup here
    public TexasGameWindow() 
    {
        super("Texas Hold'em Poker");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        game = new TexasHoldemGame();
        tablePanel = new TexasPanel();
        add(tablePanel, BorderLayout.CENTER);

        // Right panel for chip info
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(300, 700));
        
        chipInfoArea = new JTextArea(20, 25);
        chipInfoArea.setEditable(false);
        chipInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane chipScroll = new JScrollPane(chipInfoArea);
        rightPanel.add(chipScroll);
        add(rightPanel, BorderLayout.EAST);

        // Control panel
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        startHandButton = new JButton("Start Hand");
        startHandButton.addActionListener(e -> startNewHand());
        controls.add(startHandButton);

        bettingPanel = new Betting();
        
        //buttons to action listener
        bettingPanel.getBetButton().addActionListener(e -> handleBet());
        bettingPanel.getCallButton().addActionListener(e -> handleCall());
        bettingPanel.getFoldButton().addActionListener(e -> handleFold());
        bettingPanel.getRaiseButton().addActionListener(e -> handleRaise());
        
        controls.add(bettingPanel);

        JComboBox<String> bgColorBox = new JComboBox<>(new String[]{"Green", "Blue", "Red", "Gray", "Purple"});
        //need to add implementation for background color change
        
        controls.add(new JLabel("Table Color:"));
        controls.add(bgColorBox);

        add(controls, BorderLayout.SOUTH);

        updateChipDisplay();
        setVisible(true);
    }

    //handling all new button logic
    //starts hand
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

    private void handleBet() 
    {
        game.raise(5);
        updateChipDisplay();
        computerTurn();
    }

    private void handleCall() 
    {
        game.call();
        updateChipDisplay();
        computerTurn();
    }

    private void handleFold() 
    {
        game.fold();
        tablePanel.setComputerHole(game.getComputerHole(), true);
        tablePanel.repaint();
        updateChipDisplay();
        JOptionPane.showMessageDialog(this, "You folded! Computer wins.");
    }

    //raise is always by 10
    private void handleRaise() 
    {
        game.raise(10);
        updateChipDisplay();
        computerTurn();
    }

    private void computerTurn() 
    {
        //comp does random action
        game.computerAction();
        updateChipDisplay();
        
        //whatever comp does, the game logic:
        if (game.raised()) 
        {
            return; //player turn if raised
        }
        
        String stage = game.advanceStage();
        
        if (stage.equals("SHOWDOWN")) 
        {
            showdown();
        } 
        else 
        {
            //if not reset to next stage
            tablePanel.setCommunity(game.getCommunity());
            tablePanel.repaint();
            updateChipDisplay();
        }
    }

    //last round 
    private void showdown() 
    {
        tablePanel.setComputerHole(game.getComputerHole(), true);
        tablePanel.repaint();
        
        String result = game.showdown();
        updateChipDisplay();
        bettingPanel.updatePotDisplay(game.getPot());
        
        JOptionPane.showMessageDialog(this, result);
        
        if (game.isGameOver()) 
        {
            if (game.getPlayerTotalChips() <= 0) 
                JOptionPane.showMessageDialog(this, "Game Over! You're out of chips!");
            else 
                JOptionPane.showMessageDialog(this, "You win! Computer is out of chips!");
        }
    }

    //sidebar info
    private void updateChipDisplay() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("YOUR CHIPS\n");
        sb.append(String.format("White ($1):   %3d = $%d\n", game.getPlayerWhiteChips(), game.getPlayerWhiteChips() * 1));
        sb.append(String.format("Red ($5):     %3d = $%d\n", game.getPlayerRedChips(), game.getPlayerRedChips() * 5));
        sb.append(String.format("Blue ($10):   %3d = $%d\n", game.getPlayerBlueChips(), game.getPlayerBlueChips() * 10));
        sb.append(String.format("Green ($25):  %3d = $%d\n", game.getPlayerGreenChips(), game.getPlayerGreenChips() * 25));
        sb.append(String.format("Black ($100): %3d = $%d\n", game.getPlayerBlackChips(), game.getPlayerBlackChips() * 100));
        sb.append(String.format("TOTAL: $%d\n\n", game.getPlayerTotalChips()));
        
        sb.append("\nCOMPUTER CHIPS\n");
        sb.append(String.format("White ($1):   %3d = $%d\n", game.getComputerWhiteChips(), game.getComputerWhiteChips() * 1));
        sb.append(String.format("Red ($5):     %3d = $%d\n", game.getComputerRedChips(), game.getComputerRedChips() * 5));
        sb.append(String.format("Blue ($10):   %3d = $%d\n", game.getComputerBlueChips(), game.getComputerBlueChips() * 10));
        sb.append(String.format("Green ($25):  %3d = $%d\n", game.getComputerGreenChips(), game.getComputerGreenChips() * 25));
        sb.append(String.format("Black ($100): %3d = $%d\n", game.getComputerBlackChips(), game.getComputerBlackChips() * 100));
        sb.append(String.format("TOTAL: $%d\n\n", game.getComputerTotalChips()));
        
        sb.append("\nPOT INFO\n");
        sb.append(String.format("Pot: $%d\n", game.getPot()));
        sb.append(String.format("Current Bet: $%d\n", game.getCurrentBet()));
        sb.append(String.format("Your Bet: $%d\n", game.getPlayerBetThisRound()));
        
        int toCall = game.getAmountToCall();
        sb.append(String.format("\nTo Call: $%d\n", toCall));
        sb.append("\nStage: " + game.getCurrentStage());
        
        chipInfoArea.setText(sb.toString());
        bettingPanel.updatePotDisplay(game.getPot());
    }

    private class TexasPanel extends JPanel 
    {
        private List<Card> communityCards = new ArrayList<>();
        private List<Card> playerCards = new ArrayList<>();
        private List<Card> computerCards = new ArrayList<>();
        private boolean showComputerCards = false;
        private Color bgColor = new Color(5, 100, 25);

        //card setters using lists... have to link the lists to the card images
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
        
        public void setBgColor(Color c) 
        { 
            bgColor = c; 
            repaint(); 
        }

        //main display method
        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(bgColor);
            g2.fillRect(0, 0, getWidth(), getHeight());

            int w = 120;
            int h = 170;
            int spacing = 15;

            //community cards
            if (!communityCards.isEmpty()) 
            {
                int totalW = communityCards.size() * (w + spacing) - spacing;
                int x = (getWidth() - totalW) / 2;
                int y = getHeight() / 3;
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.drawString("COMMUNITY CARDS", x, y - 15);
                
                //need to add card images here
            }

            //player cards
            if (!playerCards.isEmpty()) 
            {
                int x = 40;
                int y = getHeight() - h - 40;
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString("YOUR CARDS", x, y - 10);
                
                //need to add cards here
            }

            //comp cards
            if (!computerCards.isEmpty()) 
            {
                int x = 40;
                int y = 50;
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 18));
                g2.drawString("COMPUTER'S CARDS", x, y - 10);
                
                //add computer cards here
            }
        }
    }

    //calling
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new TexasGameWindow();
            }
        });
    }
}
