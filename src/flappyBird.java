import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;

public class flappyBird extends JPanel implements ActionListener , KeyListener { // extends JPanel (CANVAS)
        int boardWidth=360;
        int boardLength=640;

        //IMAGES
        Image backgroundImg;
        Image birdImg ;
        Image topPipeImg;
        Image bottomPipeImg;

        
        int birdX = boardWidth/8;
        int birdY= boardLength/2;
        int birdHeight= 24;
        int birdWidth=34;
        int pipeX= boardWidth;
        int pipeY=0;
        int pipeWidth=64;
        int pipeHeight=512;

        class bird{
            int x = birdX;
            int y=birdY;
            int height=birdHeight;
            int width = birdWidth;
            Image img;
            bird(Image img){
                this.img=img;
            }
        }
        class pipe{
            int x = pipeX;
            int y =pipeY;
            int height=pipeHeight;
            int width= pipeWidth;
            Image img;
            boolean passed = false;

            pipe(Image img){
                this.img = img;
            }
        }

        //game logic 
        bird bird;
        Timer gameloop; //to make a loop 
        Timer pipePassed; // loop of pipes;
        int velocityX=-4; //speed of pipes 
        int velocityY=0; //bird upward speed
        int gravity =1;// to pull the bird downwards
        ArrayList<pipe> pipes;
        Random random = new Random();
        boolean gameOver = false;
        double score = 0;
        

        flappyBird(){
            setPreferredSize(new Dimension(boardWidth , boardLength));
            setBackground(Color.CYAN);
            setFocusable(true); // check the tap 
            addKeyListener(this); // check the abstract function for this class

            // load IMAGES

            backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
            birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
            topPipeImg= new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
            bottomPipeImg= new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
            
            
            //pipes
             bird = new bird(birdImg);
             pipes= new ArrayList<>();

             //pipe timer
             pipePassed = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    passedPipes(); //calls pipe
                }
                
             });
             pipePassed.start();
             gameloop= new Timer(1000/60, this); //1000/60 = 16.6ms (per frame)
             gameloop.start(); //start the timeloop


             
        }
        public void paintComponent (Graphics g){ //JPanel function 
            super.paintComponent(g); // refers to parent class (JPanel)
            draw(g);
        }
        public void draw(Graphics g){
            //BG Image
            g.drawImage(backgroundImg, 0 , 0 , boardWidth , boardLength , null); // (0,0) -> (320,640)

            //Bird img
            g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

            //PIPES IMAGE 
            for(int i=0;i<pipes.size();i++){
                pipe pipe = pipes.get(i);
                g.drawImage(pipe.img, pipe.x, pipe.y, pipeWidth, pipeHeight, null);
            }

            //score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            if(gameOver){
                g.drawString("Game Over :" + String.valueOf((int)score), boardWidth/5, boardLength/2);
            }
            else g.drawString(String.valueOf((int)score), 10, 32);
        }
        public void move(){
            //bird
            velocityY+=gravity;
            bird.y+=velocityY;
            bird.y=Math.max(bird.y, 0);

            //pipes
            for(int i=0;i<pipes.size();i++){
                pipe pipe = pipes.get(i);
                pipe.x+=velocityX;
                if(!pipe.passed && bird.x> pipe.x+pipe.width){
                    pipe.passed=true;
                    score+=0.5;
                }
                if(collision(bird, pipe)){
                    gameOver=true;
                }
            }
            if(bird.y > boardLength) gameOver=true;
        }
        public void passedPipes(){
            int randomPipeY= (int) Math.max(-512,(pipeY - (pipeHeight/4)- Math.random()*pipeHeight));
            int openeingSpace = pipeHeight/4;
            pipe toppipe = new pipe(topPipeImg);
            toppipe.y=randomPipeY;
            pipes.add(toppipe);

            pipe bottompipe = new pipe(bottomPipeImg);
            bottompipe.y = toppipe.y+toppipe.height+openeingSpace;
            pipes.add(bottompipe);
        }

        /* action listener abstract function  */
        @Override
        public void actionPerformed(ActionEvent e) {
            move();
            repaint(); //calls paint function repeatedly
            if(gameOver){
                pipePassed.stop();
                gameloop.stop();
            }
        }
         public boolean collision (bird a , pipe b){
            return a.x<b.x+b.width && // a TLC dont react B TRC
                   a.x+a.width>b.x &&// a TRC passes b TLC
                   a.y<b.y+b.height && //a TLC dont reach b BRC
                   a.y+a.height>b.y; // a BRC passes b TLC
         }
         /*key listner abstract functions */

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_SPACE)
            velocityY=-12; // bird moves up on every tap
            if(gameOver){
                bird.y = birdY;
                velocityX=-4;
                velocityY=0;
                pipes.clear();
                score=0;
                gameOver=false;
                gameloop.start();
                pipePassed.start();
            }
        }
        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyReleased(KeyEvent e) {}
}
