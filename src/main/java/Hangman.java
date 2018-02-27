import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JLabel;


public class Hangman extends JLabel {
    private final int PREFERRED_WIDTH;
    private final int PREFERRED_HEIGHT;
    private final String IMAGE_BASE_NAME;
    private final String IMAGE_DIRECTORY;
    private final String IMAGE_TYPE;
    private String path;
    private BufferedImage image;

    public Hangman() {
        this("hangman", "images/", ".png");
    }


    public Hangman(String imageBaseName, String imageDirectory, String imageType) {
        PREFERRED_WIDTH = 440;
        PREFERRED_HEIGHT = 255;

        IMAGE_BASE_NAME = imageBaseName;
        IMAGE_DIRECTORY = imageDirectory;
        IMAGE_TYPE = imageType;

        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        path = IMAGE_DIRECTORY + IMAGE_BASE_NAME + "_0" + IMAGE_TYPE;
        image = loadImage(path);
    }


    private BufferedImage loadImage(String imagePath) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            System.err.println("loadImage(): Error: Image at "
                    + imagePath + " could not be found");
            System.exit(1);
        }

        return img;
    }


    public void nextImage(int imageNumber) {
        loadNewImage(String.valueOf(imageNumber));
    }


    public void loseImage() {
        loadNewImage("lose");
    }


    public void winImage() {
        loadNewImage("win");
    }


    private void loadNewImage(String suffix) {
        path = IMAGE_DIRECTORY + IMAGE_BASE_NAME + "_" + suffix + IMAGE_TYPE;
        image = loadImage(path);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,
                0,
                0,
                PREFERRED_WIDTH,
                PREFERRED_HEIGHT,
                null);
    }
}