import javax.swing.SwingUtilities;

public class Pong{
    public static void main(String[] args){
        // start GUI on Swing's thread
        SwingUtilities.invokeLater(() -> new GameFrame());

}

}