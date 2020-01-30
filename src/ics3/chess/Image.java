package ics3.chess;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

/**
 *
 * This class is used for rendering an Image. It has a BufferedImage object as one of its properties,
 * and it extends JComponent, which allows it to render itself.
 *
 * @author Sukhveer Sahota
 * @version 1.0
 * @since March 6th, 2017
 */
public class Image {

    // BufferedImage object storing the data for rendering the image
    private BufferedImage img;

    /**
     * Constructor for the image class, requires the URL or file location of the image as a parameter
     * @param fileLocOrURL  The location of the image (local file location or URL)
     */
    public Image(String fileLocOrURL) {
        // Try reading the Image by interpreting the location as a file location. If an exception is thrown, try reading
        // the Image by interpreting the location as a URL. If neither one works, then print out the exception and
        // set the image null
        try {
            img = ImageIO.read(new File(fileLocOrURL));
        } catch (IOException localFileReadException) {
            try {
                img = ImageIO.read(new URL(fileLocOrURL));
            }
            catch (IOException URLFileReadException) {
                // Print the exception, and set the image null
                localFileReadException.printStackTrace(System.err);
                URLFileReadException.printStackTrace(System.err);
                img = null;
            }
        }
    }

    /**
     * Method used to change the image that is displayed
     *
     * @param fileLocOrURL  The location of the image (local file location or URL)
     */
    public void changeImage(String fileLocOrURL) {
        try {
            img = ImageIO.read(new File(fileLocOrURL));
        } catch (IOException localFileReadException) {
            try {
                img = ImageIO.read(new URL(fileLocOrURL));
            }
            catch (IOException URLFileReadException) {
                // Print the exception, and set the image null
                localFileReadException.printStackTrace(System.err);
                URLFileReadException.printStackTrace(System.err);
                img = null;
            }
        }
    }

    /**
     * Getter method for the BufferedImage object
     *
     * @return BufferedImage  The BufferedImage object storing the image data
     */
    public BufferedImage getImage() {
        return img;
    }
}
