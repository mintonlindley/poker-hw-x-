/**
 * CardImages.java
 *
 * Loads PNG images from the images folder. 
 * Picks a file whose name contains both the rank and suit.
 *
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private static final String IMAGE_DIR = "poker hw x/images";

    public static BufferedImage loadCardImage(Card card) 
    {
        if (card == null) return null;
        String key = (card.getRank().name() + "_" + card.getSuit().name()).toLowerCase();

        if (cache.containsKey(key)) return cache.get(key);

        File direct = new File(IMAGE_DIR);
        if (!direct.exists() || !direct.isDirectory()) return null;

        String rankWord = card.getRank().name().toLowerCase();
        String suitWord = card.getSuit().name().toLowerCase();

        // search for file containing both rank and suit "keywords"
        for (File f : direct.listFiles()) 
        {
            String name = f.getName().toLowerCase();
            if (name.endsWith(".png")) {
                if (name.contains(rankWord) && name.contains(suitWord)) 
                {
                    try { 
                        BufferedImage img = ImageIO.read(f);
                        cache.put(key, img);
                        return img;
                    } catch (Exception e) { // error checking
                        System.err.println("Failed to load image: " + f.getPath() + " -> " + e);
                    }
                }
            }
        }
        // image not found in directory
        return null;
    }

    // load the back of card image
    // player can choose the color 
    // default color will be red
    public static BufferedImage loadBackImage(String color) 
    {
        try {
        if(color.equalsIgnoreCase("Blue")) {
            if(backBlue != null) return backBlue;
            backBlue = ImageIO.read(new File(IMAGE_DIR + "/card back blue.png"));
            return backBlue;
        } 
        else if(color.equalsIgnoreCase("Green")) {
            if(backGreen != null) return backGreen;
            backGreen = ImageIO.read(new File(IMAGE_DIR + "/card back green.png"));
            return backGreen;
        }
        else if(color.equalsIgnoreCase("Orange")) {
            if(backOrange != null) return backOrange;
            backOrange = ImageIO.read(new File(IMAGE_DIR + "/card back orange.png"));
            return backOrange;
        }
        else if(color.equalsIgnoreCase("Purple")) {
            if(backPurple != null) return backPurple;
            backPurple = ImageIO.read(new File(IMAGE_DIR + "/card back purple.png"));
            return backPurple;
        }
        else { 
            if(backRed != null) return backRed;
            backRed = ImageIO.read(new File(IMAGE_DIR + "/card back red.png"));
            return backRed;
        }
    } catch(Exception e) {
        System.err.println("Failed to load back image: " + e);
        return null;
    }
    }
}