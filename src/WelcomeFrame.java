import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A graphical user interface displaying the welcome screen of the game.
 * Players enter their nickname to be identified with here.
 */
public class WelcomeFrame extends JFrame implements ActionListener {

    private JPanel content;
    private JPanel jpBattleshipText;
    private JPanel jpSouth;

    private JLabel backround;

    private JLabel title;
    private JLabel prompt;
    private JTextField enterName;
    private JButton connectButton;

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Player player;

    private Color backgroundColor = new Color(44, 62, 80);
    private Color textColor = new Color(236, 240, 241);

    /**
     * Constructs and sets up the widgets of the interface.
     *
     * @param player The Player client running the game
     */
    public WelcomeFrame(Player player) {
        this.player = player;
        backround = new JLabel(new ImageIcon("res/homeScreenImage.png"));

        //initialise UI
        setSize(new Dimension(350, 350));
        setResizable(false);
        setLocationRelativeTo(null); // centers window on screen, must be called after setSize()
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        content = new JPanel(new BorderLayout());
        content.setBackground(backgroundColor);
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setOpaque(true);
        setContentPane(content);

        jpBattleshipText = new JPanel(new GridLayout(2, 1));
        jpSouth = new JPanel(new GridLayout(2, 1, 4, 4));
        jpSouth.setBackground(backgroundColor);
        jpSouth.setOpaque(true);

        title = new JLabel("BATTLESHIP", SwingConstants.CENTER);
        title.setBackground(backgroundColor);
        title.setForeground(textColor);
        title.setOpaque(true);
        title.setFont(new Font("EUROSTILE", Font.BOLD, 50));

        jpBattleshipText.add(title);

        prompt = new JLabel();
        prompt.setBackground(backgroundColor);
        prompt.setForeground(textColor);
        prompt.setOpaque(true);
        prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                + "Enter a nickname for players to identify you with, "
                + "then hit connect!" + "</p></div></html>");
        prompt.setFont(new Font("EUROSTILE", Font.BOLD, 14));

        jpBattleshipText.add(prompt);

        enterName = new JTextField(30);
        enterName.setFont(new Font("EUROSTILE", Font.BOLD, 18));

        jpSouth.add(enterName);

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.setBackground(backgroundColor);
        connectButton.setOpaque(true);
        connectButton.setFont(new Font("EUROSTILE", Font.BOLD, 18));
        jpSouth.add(connectButton);

        content.add(jpSouth, BorderLayout.SOUTH);
        content.add(jpBattleshipText, BorderLayout.CENTER);
        // Enter button actionlistener
        connectButton.addActionListener(this);
        // Text field listener
        enterName.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        String text = enterName.getText();
        String nameToCheck = text.replaceAll("\\s+", "");
        if (nameToCheck.length() < 1) {
            prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                    + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                    + "Enter a nickname for players to identify you with, "
                    + "then hit connect!" + "</p><br><p style=\"color:red\">" +
                    "Please enter a username.</p></div></html>");
            enterName.setText("");

        } else if (nameToCheck.length() > 16) {
            prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                    + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                    + "Enter a nickname for players to identify you with, "
                    + "then hit connect!" + "</p><br><p style=\"color:red\">" +
                    "Your username can't be longer than 16 characters.</p></div></html>");
            enterName.setText("");

        } else {
            boolean isUnique = player.checkName(nameToCheck.toUpperCase());
            if (isUnique) {
                player.closeWelcomeFrame(nameToCheck);
            } else {
                prompt.setText("<html>" + "<div style=\"text-align: center;\">"
                        + "<h2>" + "Welcome to Battleship" + "</h2>" + "<p>"
                        + "Enter a nickname for players to identify you with, "
                        + "then hit connect!" + "</p><br><p style=\"color:red\">This " +
                        "username has been taken. Please pick another.</p></div></html>");
                enterName.setText("");
                //need to catch java.net.SocketException: Socket closed if user closes
                //at this stage without proceeding to lobby.
            }
        }

    }
}
