/**
 * ChipImages.java
 *
 * Loads PNG images from the images folder.
 * Now specifically expects the chip files to be named in the format: "color poker chip.png".
 *
 */
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ChipImages {
    private static final String CHIP_DIR = new File("images").getAbsolutePath() + File.separator;

    /**
     * Loads the chip image based on the specified color, using the expected file naming convention.
     * Expected file name format: "[color] poker chip.png" (e.g., "white poker chip.png").
     *
     * @param color The color of the chip (e.g., "White", "Red", "Black").
     * @return The loaded BufferedImage, or null if loading fails or the file is missing.
     */
    public static BufferedImage loadChipImage(String color) {
        // Construct the expected file name based on the user's requested format.
        // e.g., "white" + " poker chip.png" = "white poker chip.png"
        String expectedFileName = color.toLowerCase() + " poker chip.png";
        
        // Construct the file object using the CHIP_DIR
        File chipFile = new File(CHIP_DIR + expectedFileName);

        if (!chipFile.exists()) {
            System.out.println("Missing chip image: Expected file at " + chipFile.getPath());
            return null;
        }

        try {
            // Directly read the constructed file path
            return ImageIO.read(chipFile);
        } catch (Exception e) {
            System.out.println("Failed to read chip image file: " + chipFile.getName());
            e.printStackTrace(); // Print stack trace for debugging
            return null;
        }
    }
}
