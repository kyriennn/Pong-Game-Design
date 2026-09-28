import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel{
    // setting fields. lenght and width are in pixels, so used something like a resolution from video games
    public static final int LENGTH = 1280;
    public static final int WIDTH = 760;
    
    // game objects
    private Paddle leftPaddle;
    private Paddle rightPaddle;
    private Ball ball;
    private Score score;

    //timer imported from GUI library
    private Timer timer;

    //player inputs, going to try inputting left and right as well
    private boolean wPressed;
    private boolean sPressed;
    private boolean aPressed;
    private boolean dPressed;

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;

    public GamePanel(){

        leftPaddle = new Paddle();
        rightPaddle = new Paddle();
        ball = new Ball();
        score = new Score()

        timer = 100;

    }

}
