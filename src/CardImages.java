/**
 * CardImages.java
 *
 * Loads PNG images from the images folder. 
 * Picks a file whose name contains both the rank and suit.
 *
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardImages {
    private static final Map<String, BufferedImage> cache = new HashMap<>();
    private static BufferedImage backRed = null;
    private static BufferedImage backBlue = null;
    private static BufferedImage backGreen = null;
    private static BufferedImage backOrange = null;
    private static BufferedImage backPurple = null;

    // relative to working directory
    private static final String RESOURCE_PATH = "/images/";

    public static BufferedImage loadCardImage(Card card) 
    {
        if (card == null) return null;
        String key = (card.getRank().name() + "_" + card.getSuit().name()).toLowerCase();

        if (cache.containsKey(key)) return cache.get(key);

        String rankWord = card.getRank().name().toLowerCase();
        String suitWord = card.getSuit().name().toLowerCase();

        String expectedFileName = rankWord + "_of_" + suitWord + ".png";

        try {
            BufferedImage img = ImageIO.read(CardImages.class.getResourceAsStream(RESOURCE_PATH + expectedFileName));
            if (img !=null)
            {
                cache.put(key, img);
                return img;
            }
        } catch (IOException e) {
            System.err.println("Failed to load card image for " + card.toString() + ". Error: " + e.getMessage());
            }

        return null;
    }

    // load the back of card image
    // player can choose the color 
    // default color will be red
    public static BufferedImage loadBackImage(String color) 
    {
        String normalizedColor = color.equalsIgnoreCase("Red") ? "red" :
                                 color.equalsIgnoreCase("Blue") ? "blue" :
                                 color.equalsIgnoreCase("Green") ? "green" :
                                 color.equalsIgnoreCase("Orange") ? "orange" :
                                 color.equalsIgnoreCase("Purple") ? "purple" : "red"; // Default to red

        try {
            switch (normalizedColor) {
                case "red":
                    if(backRed == null) backRed = ImageIO.read(CardImages.class.getResourceAsStream(RESOURCE_PATH + "card back red.png"));
                    return backRed;
                case "blue":
                    if(backBlue == null) backBlue = ImageIO.read(CardImages.class.getResourceAsStream(RESOURCE_PATH + "card back blue.png"));
                    return backBlue;
                case "green":
                    if(backGreen == null) backGreen = ImageIO.read(CardImages.class.getResourceAsStream(RESOURCE_PATH + "card back green.png"));
                    return backGreen;
                case "orange":
                    if(backOrange == null) backOrange = ImageIO.read(CardImages.class.getResourceAsStream(RESOURCE_PATH + "card back orange.png"));
                    return backOrange;
                case "purple":
                    if(backPurple == null) backPurple = ImageIO.read(CardImages.class.getResourceAsStream(RESOURCE_PATH + "card back purple.png"));
                    return backPurple;
                default:
                    return backRed; // Fallback
            }
        } catch(IOException e) {
            System.err.println("Failed to load back image for " + normalizedColor + ". Ensure file 'card back " + normalizedColor + ".png' is in the /images/ folder on the classpath.");
            return null;
        }
    }
}
