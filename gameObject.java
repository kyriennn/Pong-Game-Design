import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected double speed;
    protected double width;
    protected double height;

    public GameObject(double x, double y, double speed, double width, double height){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public abstract void draw(Graphics g);

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, (int) width, (int) height);
    
    }

    //getter functions, generated from VSCode automatically wow thats cool
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
