import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GameBoard extends JFrame
{

    private final int WIDTH;
    private final int HEIGHT;
    private final int MAX_INCORRECT;
    private final int MAX_PASSWORD_LENGTH;
    private final String HANGMAN_IMAGE_DIRECTORY;
    private final String HANGMAN_IMAGE_TYPE;
    private final String HANGMAN_IMAGE_BASE_NAME;
    private final String LETTER_IMAGE_DIRECTORY;
    private final String LETTER_IMAGE_TYPE;
    private LetterRack gameRack;
    private Hangman gameHangman;
    private int numIncorrect;
    private JLabel correct;
    private JLabel incorrect;
    private String password;
    private StringBuilder passwordHidden;

    public GameBoard()
    {
        WIDTH = 500;
        HEIGHT = 500;
        MAX_INCORRECT = 6;
        MAX_PASSWORD_LENGTH = 10;
        HANGMAN_IMAGE_DIRECTORY = LETTER_IMAGE_DIRECTORY = "images/";
        HANGMAN_IMAGE_TYPE = LETTER_IMAGE_TYPE = ".png";
        HANGMAN_IMAGE_BASE_NAME = "hangman";

        setTitle("Phantom Hangman");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        addCloseWindowListener();

        initialize();
    }


    private void initialize()
    {
        numIncorrect = 0;

        correct = new JLabel("Word: ");
        incorrect = new JLabel("Incorrect: " + numIncorrect);
        password = new String();
        passwordHidden = new StringBuilder();

        getPassword();
        addTextPanel();
        addLetterRack();
        addHangman();


        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 2 - getSize().width / 2,
                dim.height / 2 - getSize().height / 2 - 200);
        setVisible(true);
    }


    private void addCloseWindowListener()
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                int prompt = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to quit?",
                        "Quit?",
                        JOptionPane.YES_NO_OPTION);

                if (prompt == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
    }


    private void addTextPanel()
    {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(1,2));
        textPanel.add(correct);
        textPanel.add(incorrect);

        add(textPanel, BorderLayout.NORTH);
    }


    private void addLetterRack()
    {
        gameRack = new LetterRack(password,
                LETTER_IMAGE_DIRECTORY,
                LETTER_IMAGE_TYPE);
        gameRack.attachListeners(new TileListener());
        add(gameRack, BorderLayout.SOUTH);
    }


    private void addHangman()
    {
        JPanel hangmanPanel = new JPanel();
        gameHangman = new Hangman(HANGMAN_IMAGE_BASE_NAME,
                HANGMAN_IMAGE_DIRECTORY,
                HANGMAN_IMAGE_TYPE);
        hangmanPanel.add(gameHangman);
        add(hangmanPanel, BorderLayout.CENTER);
    }


    private void getPassword()
    {
        String[] options = {"Let's Play", "Quit"};
        JPanel passwordPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Enter Password to Be Guessed: ");
        JTextField passwordText = new JTextField(MAX_PASSWORD_LENGTH);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordText);
        int confirm = -1;


        while (password.isEmpty())
        {
            confirm = JOptionPane.showOptionDialog(null,
                    passwordPanel,
                    "Enter Password",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (confirm == 0)
            {
                password = passwordText.getText();


                if (!password.matches("[a-zA-Z]+") ||
                        password.length() > MAX_PASSWORD_LENGTH)
                {
                    JOptionPane.showMessageDialog(null,
                            "Password must be less than 10 characters and " +
                                    "only contain letters A-Z.",
                            "Invalid Password",
                            JOptionPane.ERROR_MESSAGE);
                    password = ""; // empty password if error occurs
                }
            }

            else if (confirm == 1)
                System.exit(0);
        }


        passwordHidden.append(password.replaceAll(".", "*"));
        correct.setText(correct.getText() + passwordHidden.toString());
    }


    private void newGameDialog()
    {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "The password was: " + password +
                        "\nWould You Like to Start a New Game?",
                "Play Again?",
                JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION)
            initialize(); // re-initialize the GameBoard
        else
            System.exit(0);
    }


    private class TileListener implements MouseListener
    {

        public void mousePressed(MouseEvent e)
        {
            Object source = e.getSource();
            if(source instanceof LetterTile)
            {
                char c = ' ';
                int index = 0;
                boolean updated = false;


                LetterTile tilePressed = (LetterTile) source;
                c = tilePressed.guess();


                while ((index = password.toLowerCase().indexOf(c, index)) != -1)
                {
                    passwordHidden.setCharAt(index, password.charAt(index));
                    index++;
                    updated = true;
                }


                if (updated)
                {
                    correct.setText("Word: " + passwordHidden.toString());

                    if (passwordHidden.toString().equals(password))
                    {
                        gameRack.removeListeners();
                        gameHangman.winImage();
                        newGameDialog();
                    }
                }


                else
                {
                    incorrect.setText("Incorrect: " + ++numIncorrect);

                    if (numIncorrect >= MAX_INCORRECT)
                    {
                        gameHangman.loseImage();
                        gameRack.removeListeners();
                        newGameDialog();
                    }

                    else
                        gameHangman.nextImage(numIncorrect);
                }
            }
        }




        public void mouseClicked(MouseEvent e) {}

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}
    }
}