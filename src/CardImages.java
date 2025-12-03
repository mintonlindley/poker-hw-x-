/**
 * CardImages.java
 *
 * Loads PNG images from the images folder using ClassLoader.getResourceAsStream.
 * This ensures the images can be loaded when the application is packaged as a JAR.
 * Picks a file whose name contains both the rank and suit.
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CardImages {
    // Cache for card faces to prevent repeated disk/resource loading
    private static final Map<String, BufferedImage> cache = new HashMap<>();
    
    // Cache for card backs
    private static BufferedImage backRed = null;
    private static BufferedImage backBlue = null;
    private static BufferedImage backGreen = null;
    private static BufferedImage backOrange = null;
    private static BufferedImage backPurple = null;

    // The root folder for images in the classpath (e.g., in a JAR file)
    private static final String IMAGE_DIR = "images/";

    /**
     * Helper to load an image using a resource stream.
     * @param resourcePath The path to the image relative to the classpath root (e.g., /images/filename.png)
     * @return The loaded BufferedImage, or null if loading failed.
     */
    private static BufferedImage loadImageFromResource(String resourcePath) {
        try (InputStream is = CardImages.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                // System.err.println("Resource not found: " + resourcePath);
                return null;
            }
            return ImageIO.read(is);
        } catch (Exception e) {
            System.err.println("Failed to load image from resource stream: " + resourcePath + " - " + e.getMessage());
            return null;
        }
    }


    public static BufferedImage loadCardImage(Card card) 
    {
        if (card == null) return null;
        
        String rankStr = card.getRank().name().toLowerCase();
        String suitStr = card.getSuit().name().toLowerCase();
        
        // Key for cache lookup
        String key = (rankStr + "_" + suitStr);

        if (cache.containsKey(key)) return cache.get(key);

        // Construct the expected file name based on convention: rank_of_suit.png
        // Example: /images/ace_of_spades.png
        String fileName = rankStr + "_of_" + suitStr + ".png";
        String resourcePath = "/" + IMAGE_DIR + fileName; 

        BufferedImage img = loadImageFromResource(resourcePath);
        
        if (img != null) {
            cache.put(key, img);
        } else {
            // Fallback for debugging if the specific file name wasn't found
            // System.err.println("Could not find standard card image resource: " + resourcePath);
        }
        
        return img;
    }

    // load the back of card image
    // player can choose the color 
    // default color will be red
    public static BufferedImage loadBackImage(String color) 
    {
        String fileName = null;
        BufferedImage cachedImage = null;

        if(color.equalsIgnoreCase("Blue")) {
            cachedImage = backBlue;
            if(cachedImage != null) return cachedImage;
            fileName = "card back blue.png";
        } 
        else if(color.equalsIgnoreCase("Green")) {
            cachedImage = backGreen;
            if(cachedImage != null) return cachedImage;
            fileName = "card back green.png";
        }
        else if(color.equalsIgnoreCase("Orange")) {
            cachedImage = backOrange;
            if(cachedImage != null) return cachedImage;
            fileName = "card back orange.png";
        }
        else if(color.equalsIgnoreCase("Purple")) {
            cachedImage = backPurple;
            if(cachedImage != null) return cachedImage;
            fileName = "card back purple.png";
        }
        else { // Default to Red
            cachedImage = backRed;
            if(cachedImage != null) return cachedImage;
            fileName = "card back red.png";
        }
        
        String resourcePath = "/" + IMAGE_DIR + fileName;
        BufferedImage loadedImage = loadImageFromResource(resourcePath);

        // Cache the newly loaded image
        if(loadedImage != null) {
            if(color.equalsIgnoreCase("Blue")) backBlue = loadedImage;
            else if(color.equalsIgnoreCase("Green")) backGreen = loadedImage;
            else if(color.equalsIgnoreCase("Orange")) backOrange = loadedImage;
            else if(color.equalsIgnoreCase("Purple")) backPurple = loadedImage;
            else backRed = loadedImage;
        }

        return loadedImage;
    }
}
