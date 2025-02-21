import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        JFrame Frame = new JFrame("Flappy Bird ");
        int boardWidth = 360;
        int boardLength=640;
        
        Frame.setSize(boardWidth , boardLength);
        Frame.setLocationRelativeTo(null);//centre of screen
        Frame.setResizable(false);// user cannot resize
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will terminate the project 


        flappyBird flappyBird = new flappyBird(); // instance of JPanel
        Frame.add(flappyBird); //adding panel on frame
        Frame.pack();
        Frame.requestFocus(); //to check key input
        Frame.setVisible(true);  //visiblity on screen


    }
}
