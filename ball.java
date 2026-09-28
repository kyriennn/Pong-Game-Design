import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball extends GameObject{
    private static final double SPEED_UP = 1.1;
    private static final double MAX_SPEED = 12;
    private static final Random RANDOM = new Random();

    // ball needs velocity aside from everything GameObject provides
    private final double baseSpeed; // the starting speed 
    private double dx;
    private double dy;
    public Ball(double x, double y, double speed, double size){
        // use size for both width and height because its a ball lol
        super(x, y, speed, size, size);
        this.baseSpeed = speed;
        reset(); // reset the ball at the start of the game;
    }

    @Override
    public void update(){
        //updating movement of the ball
        x += dx;
        y += dy;

        // unlike the paddle, the ball can bounce back if it hits the bounds of the game
        if(y<0 || y > GamePanel.HEIGHT - height){
            dy = -dy;
        }

        if(y > GamePanel.HEIGHT - height){
            y = GamePanel.HEIGHT - height;
            dy = -dy;
        }
        // left and right wont be handled here because it involves scoring, so Score class will settle that with GamePanel
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    @Override
    public void draw(Graphics g){
        g.fillOval((int) x, (int) y, (int) width, (int) height);
        g.setColor(Color.CYAN);
    }

    // reset function to reset the ball at the start of the game, and overload it to reset the ball in the loser's court when a point is scored
    public void reset(){
        //pick a random side to start the rally first
        reset(RANDOM.nextBoolean() ? 1: -1); 
    }
    public void reset(int side){


        y = GamePanel.HEIGHT / 2 - height / 2;
        if(side == -1){
            x = GamePanel.WIDTH / 4 - width / 2;
        }
        if(side == 1){
            x = GamePanel.WIDTH * 3 / 4 - width / 2;
        }

        //reset the speeds again
        speed = baseSpeed;
        dx = 0;
        dy = 0;
    }

    //handling the hitting of the ball by the paddle
    public void hitBy(Paddle paddle, int direction){
        speed = Math.min(speed * SPEED_UP, MAX_SPEED);
        dx = direction * speed;

        // going to try to do reflecting of the ball, so when it bounces it obeys normal physics
        // -1 is paddle top, 0 is paddle center, 1 is paddle bottom
        double paddleCenter = paddle.getY() + paddle.getHeight() / 2;
        double ballCenter = y + height / 2;
        // getting the offset is the difference of paddle and ball centers as a fraction of a paddle half
        double offset = (ballCenter - paddleCenter) / (paddle.getHeight() / 2);
        offset = Math.max(-1, Math.min(1, offset)); // bounding the offset incase the ball hit the corner of the paddle

        dy = offset * speed;
    }
    


    
}
