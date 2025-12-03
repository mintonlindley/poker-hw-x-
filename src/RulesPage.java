/**
 * RulesPage.java
 *
 * Window to show rules for the games. 
 * 
 */
import javax.swing.*;
import java.awt.*;

public class RulesPage extends JFrame {
    public RulesPage () {
        super("Poker Rules (Texas and Blackjack)");
        setSize(600, 500);
        setLocationRelativeTo(null);
        JTextArea txt = new JTextArea();
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setText("TEXAS HOLD'EM RULES\n" +
            "Each player is dealt two private cards (hole cards).\n" +
            "Five community cards are dealt face up in three stages (flop, turn, and river).\n" +
            "Players make the best five-card hand using any combination of community cards and their hole cards.\n" +
            "Betting rounds occur.\n" +
            "BLACKJACK RULES\n" +
            "Each player is dealt two cards. Dealer gets two cards.\n" +
            "The goal is to get as close to 21 as possible without going over.\n" +
            "Dealer must hit until at least 17.\n" +
            "If you exceed 21, you bust and lose immediately.\n" +
            "If neither busts, higher total wins.\n"); 
        JScrollPane sp = new JScrollPane(txt);
        add(sp, BorderLayout.CENTER);
        setVisible(true);
    }
}
