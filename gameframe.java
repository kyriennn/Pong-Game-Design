import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame(){
        // JFrame's constructor accepts Strings for the title so we can use that
        super("Welcome to Pong");

        //Closing behaviour to allow the program to actual quit task and not just minimised
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // locking the resolution of the program
        setResizable(false);

        // adding the actual game panel (GamePanel object)
        add(new GamePanel());

        //size the program window around game panel's size
        pack();

        // ensure the center is on the screen (okay wtf)
        setLocationRelativeTo(null);

        // make sure its visible ???? shouldnt it be visible by default lol
        setVisible(true);
    }
    
}
