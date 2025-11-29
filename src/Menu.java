/**
 * Menu.java
 *
 * Displays a menu allowing the player to choose
 * between Blackjack, Texas, or view Rules Page. Pressing begin starts a Blackjack game.
 *
 */
import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    public Menu() {
        super("Poker HW X - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Card Games", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 36));
        title.setForeground(new Color(212, 175, 55)); // gold color
        add(title, BorderLayout.NORTH);

        // centering the buttons in the menu
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(4,1, 10, 10));
        JButton btnTexas = new JButton("Texas Hold'em");
        JButton btnBlackjack = new JButton("Blackjack");
        JButton btnRules = new JButton("Rules");
        JButton btnBegin = new JButton("Begin"); // will default to blackjack version
        center.add(btnTexas); center.add(btnBlackjack); center.add(btnRules); center.add(btnBegin);
        add(center, BorderLayout.CENTER);

        // button actions
        btnTexas.addActionListener(e -> { dispose(); new TexasGameWindow(); });
        btnBlackjack.addActionListener(e -> { dispose(); new BlackjackWindow(); });
        btnRules.addActionListener(e -> { new RulesPage(); });
        btnBegin.addActionListener(e -> { dispose(); new BlackjackWindow(); });

        setVisible(true);
    }

    public static void main(String[] args) {
        // entry point (how we start the program)
        SwingUtilities.invokeLater(() -> new Menu());
    }
}