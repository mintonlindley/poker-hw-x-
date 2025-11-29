/**
 * BlackjackWindow.java
 *
 * Blackjack layout including dealer and player cards.
 * Displays hit, Stand, Reset, and Menu buttons.
 * Players can choose the color of the background and the back of the cards.
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlackjackWindow extends JFrame {

    private Deck deck = new Deck();
    private List<Card> dealerCards = new ArrayList<>();
    private List<Card> playerCards = new ArrayList<>();
    private BlackjackPanel tablePanel;

    public BlackjackWindow() {
        super("Blackjack");
        setSize(850, 600);
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

        // pick the background color
        JComboBox<String> bgColorBox = new JComboBox<>(new String[]{"Green","Blue","Red","Orange","Purple"});
        bgColorBox.addActionListener(e -> {
            String color = (String) bgColorBox.getSelectedItem();
            switch(color) {
                case "Green" -> tablePanel.setBgColor(Color.GREEN);
                case "Blue" -> tablePanel.setBgColor(Color.BLUE);
                case "Red" -> tablePanel.setBgColor(Color.RED);
                case "Orange" -> tablePanel.setBgColor(Color.ORANGE);
                case "Purple" -> tablePanel.setBgColor(Color.MAGENTA);
            }
        });
        controls.add(bgColorBox);

        // pick the color of the back of playing cards
        JComboBox<String> backBox = new JComboBox<>(new String[]{"Red","Blue","Green","Orange","Purple"});
        backBox.addActionListener(e -> tablePanel.setBackColor((String) backBox.getSelectedItem()));
        controls.add(backBox);

        add(controls, BorderLayout.SOUTH);

        // button actions
        hit.addActionListener(e -> {
            playerCards.add(deck.deal(1).get(0));
            tablePanel.setPlayerCards(playerCards);
        });

        stand.addActionListener(e -> {
            while (tablePanel.handValue(dealerCards) < 17) {
                dealerCards.add(deck.deal(1).get(0));
            }
            tablePanel.setDealerCards(dealerCards);
        });

        reset.addActionListener(e -> {
            deck.reset();
            deck.shuffle();
            dealerCards.clear();
            playerCards.clear();

            dealerCards.add(deck.deal(1).get(0));
            dealerCards.add(deck.deal(1).get(0));
            playerCards.add(deck.deal(1).get(0));
            playerCards.add(deck.deal(1).get(0));

            tablePanel.setDealerCards(dealerCards);
            tablePanel.setPlayerCards(playerCards);
        });

        back.addActionListener(e -> {
            dispose();
            new Menu();
        });

        // start automatically
        reset.doClick();

        setVisible(true);
    }

    // inner panel
    private class BlackjackPanel extends JPanel {
        private List<Card> dealer = new ArrayList<>();
        private List<Card> player = new ArrayList<>();
        private String backColor = "Red";
        private Color bgColor = new Color(0,90,20);

        public void setDealerCards(List<Card> c) { dealer = c; repaint(); }
        public void setPlayerCards(List<Card> c) { player = c; repaint(); }
        public void setBackColor(String color) { backColor = color; repaint(); }
        public void setBgColor(Color c) { bgColor = c; repaint(); }

        public int handValue(List<Card> cards) {
            int total = 0;
            int aces = 0;
            for (Card c : cards) {
                switch(c.getRank()) {
                    case ACE -> { aces++; total += 11; }
                    case KING, QUEEN, JACK -> total += 10;
                    default -> total += c.getRank().ordinal() + 2;
                }
            }
            while(total > 21 && aces > 0) { total -= 10; aces--; }
            return total;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(bgColor);
            g2.fillRect(0,0,getWidth(),getHeight());

            int w = 110;
            int h = 160;
            int spacing = 15;
            int x = 30;

            // dealer cards 
            // the first one is face-down
            for(int i=0;i<dealer.size();i++){
                BufferedImage img = (i==0) ? CardImages.loadBackImage(backColor)
                                           : CardImages.loadCardImage(dealer.get(i));
                if(img != null) g2.drawImage(img, x, 80, w, h, this);
                x += w + spacing;
            }

            // player cards
            x = 30;
            int y = getHeight() - 200;
            for(Card c : player){
                BufferedImage img = CardImages.loadCardImage(c);
                if(img != null) g2.drawImage(img, x, y, w, h, this);
                x += w + spacing;
            }
        }
    }
}