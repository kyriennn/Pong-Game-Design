import java.awt.Graphics;
import java.awt.Color;

public class Paddle extends GameObject{
    // every paddle has the same initial direction, so we dont need to include in constructor
    private int xdirection = 0;
    private int ydirection = 0;
    private double minX;
    private double maxX;

    public Paddle(double x, double y, double speed, double width, double height, double minX, double maxX){
        super(x, y, speed, width, height);
        this.minX = minX;
        this.maxX = maxX;
    }

    //setDirection function: sets the direction of the paddle based on the input keys of the player
    public void setDirection(int xdirection, int ydirection){
        this.xdirection = xdirection;
        this.ydirection = ydirection;
    }

    //update function: updates the direction of the paddle constantly? idk
    @Override
    public void update(){
        // normal moving
        y += ydirection * speed;
        x += xdirection * speed;
        
        // upper bounds vertical clamping 
        if(y < 0){
            y = 0;
        }

        //lower bounds vertical clamping
        // subtract paddle's height because y is the top part of the paddle
        if(y > GamePanel.HEIGHT - height){
            y= GamePanel.HEIGHT - height;
        }

        //horizontal clamping
        if(x<minX){
            x = minX;
        }
        if(x >maxX){
            x = maxX;
        }

    }

    @Override 
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, (int) width, (int) height);
    }


    }

