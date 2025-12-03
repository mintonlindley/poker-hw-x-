/**
 * Betting.java
 *
 * Provides betting controls (bet, call, fold, raise) in button form and displays the pot.
 * 
 */
import java.awt.*;
import javax.swing.*;

public class Betting extends JPanel {
    private JButton betBtn, callBtn, foldBtn, raiseBtn;
    private JLabel potLabel;
    private int pot = 0;
    private TexasGameWindow gameWindow;

    public Betting(TexasGameWindow gameWindow) 
    {
        this.gameWindow = gameWindow;
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));
        betBtn = new JButton("Bet"); callBtn = new JButton("Call"); foldBtn = new JButton("Fold"); raiseBtn = new JButton("Raise");
        potLabel = new JLabel("Pot: $0");
        add(betBtn); add(callBtn); add(foldBtn); add(raiseBtn); add(potLabel);

        // replace once actual logic is implemented 
        betBtn.addActionListener(e -> { pot += 5; updatePot(); });
        callBtn.addActionListener(e -> { pot += 2; updatePot(); });
        raiseBtn.addActionListener(e -> { pot += 10; updatePot(); });
        // foldBtn.addActionListener(e -> gameWindow.handleFold());
    }

    private void updatePot() 
    { 
        potLabel.setText("Pot: $" + pot); 
    }
    public int getPot() 
    { 
        return pot; 
    }
    public void resetPot() 
    { 
        pot = 0; 
        updatePot(); 
    }
    public JButton getBetButton()
    {
        return betBtn;
    }
    public JButton getCallButton()
    {
        return callBtn;
    }
    public JButton getFoldButton()
    {
        return foldBtn;
    }
    public JButton getRaiseButton()
    {
        return raiseBtn;
    }
    public void updatePotDisplay(int potAmount)
    {
        pot=potAmount;
        potLabel.setText("Pot: $" +pot);
    }
}
