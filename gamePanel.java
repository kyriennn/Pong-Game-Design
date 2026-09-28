import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements KeyListener {
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
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);

        //allow key presses in the game
        setFocusable(true);
        
        //constructing the paddles
        this.leftPaddle = new Paddle(20, HEIGHT/2 - PADDLE_HEIGHT / 2, PADDLE_SPEED, PADDLE_WIDTH, PADDLE_HEIGHT, 0, WIDTH / 2 - PADDLE_WIDTH);
        this.rightPaddle = new Paddle(WIDTH - 20 - PADDLE_WIDTH, HEIGHT / 2 - PADDLE_HEIGHT / 2, PADDLE_SPEED, PADDLE_WIDTH, PADDLE_HEIGHT, WIDTH / 2, WIDTH - PADDLE_WIDTH);

        //construct the ball
        this.ball = new Ball(WIDTH / 2, HEIGHT / 2, BALL_SPEED, BALL_SIZE);
        //construct the score
        this.score = new Score();

        //constructing the timer 
        this.timer = new Timer(DELAY, e -> gameLoop());
        timer.start();

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
            ball.draw(g);
            score.draw(g);
        }

        //gameloop function, this runs every tick (60 times per second)
        private void gameLoop(){
            if(gameOver){
                repaint();
                return;
            }

            updateDirections();
            leftPaddle.update();
            rightPaddle.update();
            ball.update();
            checkCollisions();

            repaint();
        }

        // implementing the abstract methods from keyEvents, to enable inputs
        @Override 
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_W: wPressed = true;
                                    break;
                case KeyEvent.VK_S: sPressed = true;
                                    break;
                case KeyEvent.VK_A: aPressed = true;
                                    break;
                case KeyEvent.VK_D: dPressed = true;
                                    break;
                case KeyEvent.VK_UP: upPressed = true;
                                    break;
                case KeyEvent.VK_DOWN: downPressed = true;
                                    break;
                case KeyEvent.VK_LEFT: leftPressed = true;
                                    break;
                case KeyEvent.VK_RIGHT: rightPressed = true;
                                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_W: wPressed = false;
                                    break;
                case KeyEvent.VK_S: sPressed = false;
                                    break;
                case KeyEvent.VK_A: aPressed = false;
                                    break;
                case KeyEvent.VK_D: dPressed = false;
                                    break;
                case KeyEvent.VK_UP: upPressed = false;
                                    break;
                case KeyEvent.VK_DOWN: downPressed = false;
                                    break;
                case KeyEvent.VK_LEFT: leftPressed = false;
                                    break;
                case KeyEvent.VK_RIGHT: rightPressed = false;
                                    break;
            }
        }

        //keyTyped function isnt needed, so just leave it as a blank function
        @Override
        public void keyTyped(KeyEvent e){

        }

        // convert the flags to directions for the paddle
        private void updateDirections(){
            int leftX = (dPressed ? 1 : 0) - (aPressed ? 1: 0);
            int leftY = (sPressed ? 1 : 0) - (wPressed ? 1 : 0);
            leftPaddle.setDirection(leftX, leftY);

            int rightX = (rightPressed ? 1 : 0) - (leftPressed ? 1 : 0);
            int rightY = (downPressed ? 1 : 0) - (upPressed ? 1 : 0);
            rightPaddle.setDirection(rightX, rightY);
        }

        // check how the collisions between the paddle and the ball arise, use in conjunction with hitBy function in Ball class
        private void checkCollisions(){
            // left paddle: check that ball moving left or still, and they hit the paddle
            if(ball.getDx() <= 0 && ball.getBounds().intersects(leftPaddle.getBounds())){
                ball.hitBy(leftPaddle, 1);
            }

            //right paddle: check that ball is moving left or still, and they hit the paddle
            if(ball.getDx() >= 0 && ball.getBounds().intersects(rightPaddle.getBounds())){
                ball.hitBy(rightPaddle, -1);
            }
        }
}
