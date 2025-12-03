/**
 * ChipImages.java
 *
 * Loads PNG images from the images folder based on the color.
 * Caches loaded images for faster access.
 * The expected file format is "<color> poker chip.png" (e.g., "white poker chip.png").
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ChipImages {
    private static final String CHIP_DIR = "images/";
    
    // Cache to store loaded images using the color name as the key
    private static final Map<String, BufferedImage> cache = new HashMap<>();

    /**
     * Loads the image for a specific poker chip color.
     * Assumes the file is named "<color> poker chip.png" (e.g., "white poker chip.png").
     * @param color The color of the chip (e.g., "white", "red").
     * @return The loaded BufferedImage, or null if loading fails.
     */
    public static BufferedImage loadChipImage(String color) {
        // Normalize color to lowercase for consistent file lookup and cache key
        String normalizedColor = color.toLowerCase();
        
        // 1. Check cache first
        if (cache.containsKey(normalizedColor)) {
            return cache.get(normalizedColor);
        }

        // Construct the expected filename based on the user's information
        String filename = normalizedColor + " poker chip.png";
        String filepath = CHIP_DIR + filename;
        
        File f = new File(filepath);
        
        if (!f.exists()) {
            System.out.println("Missing chip image file: " + filepath);
            return null;
        }

        // 2. Load image and cache it
        try {
            BufferedImage img = ImageIO.read(f);
            if (img != null) {
                cache.put(normalizedColor, img);
                return img;
            }
        } catch (Exception e) {
            System.out.println("Failed to read chip image: " + filepath);
            e.printStackTrace();
        }
        
        System.out.println("Failed to load chip for color: " + color);
        return null;
    }
}
