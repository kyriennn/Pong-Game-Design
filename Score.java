import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class Score {
    private int leftScore = 0;
    private int rightScore = 0;
    private static final int WIN_SCORE = 5;

    public Score(){}

    public void addScore(int loserSide){
        if(loserSide == -1){
            rightScore++;
        }
        else if(loserSide == 1){
            leftScore++;
        }
    }

    // separate function for detecting winner after addScore
    public boolean hasWinner(){
        return (leftScore >= WIN_SCORE || rightScore >= WIN_SCORE);
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }
    
    //reset function for the score
    public void reset(){
        leftScore = 0;
        rightScore = 0;
    }

    //draw function for the score graphics
    public void draw(Graphics g){
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 48));
        g.drawString(String.valueOf(leftScore), GamePanel.WIDTH / 4, 60);
        g.drawString(String.valueOf(rightScore), GamePanel.WIDTH * 3 / 4, 60);
    }
}
