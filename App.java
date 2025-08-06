import java.awt.GridBagLayout;
//import java.awt.FlowLayout;
import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
    
        int rowCount=25;
        int columnCount=25;
        int tileSize=32;
        int boardWidth= columnCount*tileSize;
        int boardHeight= rowCount*tileSize;

         JFrame frame=new JFrame("Pac Man");
         frame.setVisible(true);
         frame.setSize(boardWidth,boardHeight);
         frame.setLocationRelativeTo(null);
         //frame.setResizable(false);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setLayout(new GridBagLayout());


        PacMan pacmanGame=new PacMan();
        //frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.add(pacmanGame);
        //frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}
