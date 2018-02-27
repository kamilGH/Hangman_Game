import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class LetterRack extends JPanel {

    private final int RACK_COLS;


    private final int RACK_ROWS;


    private final GridLayout LETTER_RACK_LAYOUT;


    private final int CAPACITY;


    private final String IMAGE_DIRECTORY;


    private final String IMAGE_TYPE;


    private final String password;


    private final ArrayList<LetterTile> rack;


    public LetterRack() {
        this("password", "images/", ".png");
    }


    public LetterRack(String inPassword, String imageDirectory,
                      String imageType) {
        RACK_COLS = 8;
        RACK_ROWS = 2;
        LETTER_RACK_LAYOUT = new GridLayout(RACK_ROWS, RACK_COLS);
        LETTER_RACK_LAYOUT.setVgap(10);
        CAPACITY = RACK_ROWS * RACK_COLS;

        IMAGE_DIRECTORY = imageDirectory;
        IMAGE_TYPE = imageType;

        rack = new ArrayList<LetterTile>();
        password = inPassword;


        setBorder(BorderFactory.createEmptyBorder(10, 17, 10, 10));
        setLayout(LETTER_RACK_LAYOUT);
        loadRack();
    }


    private void loadRack() {
        buildRack();
        for (LetterTile tile : rack)
            add(tile);
    }


    private void buildRack() {
        StringBuilder passwordBuilder =
                new StringBuilder(password.toLowerCase());
        ArrayList<Character> tiles = new ArrayList<Character>();
        Random rand = new Random();
        int i = 0, j = 0;


        while (passwordBuilder.length() > 0) {

            if (!tiles.contains(passwordBuilder.charAt(0))) {
                tiles.add(passwordBuilder.charAt(0));
                i++;
            }
            passwordBuilder.deleteCharAt(0);
        }


        for (; i < CAPACITY; i++) {
            Character c = 'a';
            do {
                c = (char) (rand.nextInt(26) + 'a');
            } while (tiles.contains(c));
            tiles.add(c);
        }


        for (i = 0; i < CAPACITY; i++) {
            j = rand.nextInt(tiles.size());
            rack.add(new LetterTile(tiles.get(j),
                    IMAGE_DIRECTORY,
                    IMAGE_TYPE));
            tiles.remove(j);
        }
    }


    public void attachListeners(MouseListener l) {
        for (LetterTile tile : rack)
            tile.addTileListener(l);
    }


    public void removeListeners() {
        for (LetterTile tile : rack)
            tile.removeTileListener();
    }
}