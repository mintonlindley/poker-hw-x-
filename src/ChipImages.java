/**
 * ChipImages.java
 *
 * Loads PNG images from the images folder. 
 * Picks a file whose name contains both the word "chip" and the color.
 *
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ChipImages {
    private static final String CHIP_DIR = "images/";

    public static BufferedImage loadChipImage(String color) {
        File direct = new File(CHIP_DIR);
        if (!direct.exists() || !direct.isDirectory()) {
            System.out.println("Chip directory missing: " + CHIP_DIR);
            return null;
        }

        String target = color.toLowerCase();

        // search for matching file
        for (File f : direct.listFiles()) {
            String name = f.getName().toLowerCase();

            if (name.endsWith(".png") &&
                name.contains(target) &&
                name.contains("chip"))
            {
                // error checking
                try {
                    return ImageIO.read(f);
                } catch (Exception e) {
                    System.out.println("Failed to load chip image: " + f.getName());
                }
            }
        }
        System.out.println("Missing chip: " + color);
        return null;
    }
}