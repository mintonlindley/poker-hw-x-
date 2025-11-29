/**
 * TexasWindow.java
 *
 * Texas layout including community and hole cards.
 * Displays deal, flop, turn, river, and reset buttons.
 * Players can choose the color of the background and the back of the cards.
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TexasGameWindow extends JFrame {

    private Deck deck = new Deck();
    private List<Card> community = new ArrayList<>();
    private List<Card> playerHole = new ArrayList<>();
    private TexasPanel tablePanel;

    public TexasGameWindow() {
        super("Texas Hold'em");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        deck.shuffle();

        tablePanel = new TexasPanel();
        add(tablePanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        JButton deal = new JButton("Deal Hole Cards");
        JButton flop = new JButton("Flop");
        JButton turn = new JButton("Turn");
        JButton river = new JButton("River");
        JButton reset = new JButton("Reset");
        JButton back = new JButton("Menu");

        flop.setEnabled(false);
        turn.setEnabled(false);
        river.setEnabled(false);

        controls.add(deal);
        controls.add(flop);
        controls.add(turn);
        controls.add(river);
        controls.add(reset);
        controls.add(back);

        // pick the background color
        JComboBox<String> bgColorBox = new JComboBox<>(new String[]{"Green", "Blue", "Red", "Orange", "Purple"});
        bgColorBox.addActionListener(e -> {
            String color = (String) bgColorBox.getSelectedItem();
            switch (color) {
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
        deal.addActionListener(e -> {
            playerHole = deck.deal(2);
            tablePanel.setPlayerHole(playerHole);
            flop.setEnabled(true);
            deal.setEnabled(false);
        });

        flop.addActionListener(e -> {
            community.addAll(deck.deal(3));
            tablePanel.setCommunity(community);
            flop.setEnabled(false);
            turn.setEnabled(true);
        });

        turn.addActionListener(e -> {
            community.addAll(deck.deal(1));
            tablePanel.setCommunity(community);
            turn.setEnabled(false);
            river.setEnabled(true);
        });

        river.addActionListener(e -> {
            community.addAll(deck.deal(1));
            tablePanel.setCommunity(community);
            river.setEnabled(false);
        });

        reset.addActionListener(e -> {
            deck.reset();
            deck.shuffle();
            community.clear();
            playerHole.clear();
            tablePanel.setCommunity(community);
            tablePanel.setPlayerHole(playerHole);
            deal.setEnabled(true);
            flop.setEnabled(false);
            turn.setEnabled(false);
            river.setEnabled(false);
        });

        back.addActionListener(e -> {
            dispose();
            new Menu();
        });

        setVisible(true);
    }

    // inner panel
    private class TexasPanel extends JPanel {
        private List<Card> communityCards = new ArrayList<>();
        private List<Card> holeCards = new ArrayList<>();
        private String backColor = "Red";
        private Color bgColor = new Color(5, 100, 25);

        public void setCommunity(List<Card> c) { communityCards = c; repaint(); }
        public void setPlayerHole(List<Card> c) { holeCards = c; repaint(); }
        public void setBackColor(String color) { backColor = color; repaint(); }
        public void setBgColor(Color c) { bgColor = c; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(bgColor);
            g2.fillRect(0, 0, getWidth(), getHeight());

            int w = 120; 
            int h = 170; 
            int spacing = 15;

            // community cards
            // face-up
            int totalW = communityCards.size() * (w + spacing) - spacing;
            int x = (getWidth() - totalW) / 2;
            int y = getHeight() / 3;
            for (int i = 0; i < communityCards.size(); i++) {
                BufferedImage img = CardImages.loadCardImage(communityCards.get(i));
                if (img != null) g2.drawImage(img, x + i * (w + spacing), y, w, h, this);
            }

            // hole cards
            // face-down
            int hx = 40;
            int hy = getHeight() - h - 40;
            for (int i = 0; i < holeCards.size(); i++) {
                BufferedImage img = CardImages.loadBackImage(backColor);
                if (img != null) g2.drawImage(img, hx + i * (w / 1.5), hy, w / 1.3, h / 1.3, this);
            }
        }
    }
}