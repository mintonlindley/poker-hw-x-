/**
 * 
 * CardPanel.java
 *
 * Reusable panel for drawing a list of cards horizontally.
 * Will be used by both versions of poker (Blackjack and Texas).
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class CardPanel extends JPanel {
    private List<Card> cards;

    public void setCards(List<Card> cards) {
        this.cards = cards;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cards == null) return;

        Graphics2D g2 = (Graphics2D) g;
        int w = 100, h = 145;
        int spacing = 12;

        int x = 20;
        int y = (getHeight() - h) / 2;

        for (Card c : cards) {
            BufferedImage img = CardImages.loadCardImage(c);
            if (img != null)
                g2.drawImage(img, x, y, w, h, this);
            x += w + spacing;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 180);
    }
}