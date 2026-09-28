import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GamePanel extends JPanel{
    // setting fields. height and width are in pixels, so used something like a resolution from video games
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 760;

    // all these are for 60FPS 
    public static final int PADDLE_SPEED = 16;
    public static final int PADDLE_WIDTH = 16;
    public static final int PADDLE_HEIGHT = 80;
    public static final int BALL_SIZE = 16;
    public static final int BALL_SPEED = 16;
    public static final int DELAY = 16;
    
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

    // to find out if the game is over
    private boolean gameOver = false;

    public GamePanel(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        //allow key presses in the game
        setFocusable(true);
        
        //constructing the paddles
        this.leftPaddle = new Paddle(20, HEIGHT/2 - PADDLE_HEIGHT / 2, PADDLE_SPEED, PADDLE_WIDTH, PADDLE_HEIGHT, 0, WIDTH / 2 - PADDLE_WIDTH);
        this.rightPaddle = new Paddle(WIDTH - 20 - PADDLE_WIDTH, HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_SPEED, PADDLE_WIDTH, PADDLE_HEIGHT, WIDTH / 2, WIDTH - PADDLE_WIDTH);

        //construct the ball
        this.ball = new Ball(WIDTH / 2, HEIGHT / 2, BALL_SPEED, BALL_SIZE);
        this.score = new Score();

        

    }
    
        public void paintComponent(Graphics g){
            //clears last frame
            super.paintComponent(g);

            // draw the net: short dashes down the center
            g.setColor(Color.GRAY);
            for (int y = 0; y < HEIGHT; y += 30) {    // every 30px, start a new dash
                g.fillRect(WIDTH / 2 - 2, y, 4, 15);  // 4px wide, 15px tall, centered on WIDTH/2
            }

            //draw the graphics for paddles
            leftPaddle.draw(g);
            rightPaddle.draw(g);
        }

}
