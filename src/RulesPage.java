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
        txt.setText("Rules will go here."); 
        JScrollPane sp = new JScrollPane(txt);
        add(sp, BorderLayout.CENTER);
        setVisible(true);
    }
}