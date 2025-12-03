/**
 * CasinoApp.java
 * * Main entry point for the card game application.
 * Launches the Menu screen, allowing the player to choose between
 * Blackjack and Texas Hold'em.
 */
import javax.swing.SwingUtilities;

public class CasinoApp {

    public static void main(String[] args) {
        // All GUI setup should be done on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // The Menu class contains the main navigation logic to switch between games.
            new Menu();
        });
    }
}
