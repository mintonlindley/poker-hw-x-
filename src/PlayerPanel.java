/**
 * PlayerPanel.java
 *
 * Panel representing a player that includes their name, chip count, etc.
 *
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerPanel extends JPanel 
{
    private String name;
    private int chips;
    private java.util.List<Card> holeCards;

    public PlayerPanel(String name, int chips) {
        this.name = name;
        this.chips = chips;
        setPreferredSize(new Dimension(180, 120));
    }

    public void setHoleCards(java.util.List<Card> hole) {
        this.holeCards = hole;
        repaint();
    }

    public void setChips(int c) { this.chips = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0,0,0,0));
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.WHITE);
        g.drawString(name, 8, 18);
        g.drawString("Chips: $" + chips, 8, 36);
        if (holeCards != null) {
            int x = 8;
            int w = 60, h = 90;
            for (Card c : holeCards) {
                BufferedImage img = CardImages.loadCardImage(c);
                if (img != null) {
                    g.drawImage(img, x, 40, w, h, this);
                }
                x += w + 6;
            }
        }
    }
}