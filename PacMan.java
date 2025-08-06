import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener,KeyListener{
    class Block{
        int x;
        int y;
        int width;
        int height;
        Image image;
        Image originalImage; // Add this line

        int startX;
        int startY;

        char direction='U';
        int velocityX=0;
        int velocityY=0;

        Block(Image image,int x,int y,int width,int height){
            this.image=image;
            this.originalImage=image; // Store original image
            this.x=x;
            this.y=y;
            this.width=width;
            this.height=height; 

            this.startX=x;
            this.startY=y;

            //char direction='U';
            //int velocityX=0;
            //int velocityY=0;

        }

        void updateDirection(char direction){
            char prevDirection=this.direction;
            this.direction=direction;
            updateVelocity();
            this.x+=velocityX;
            this.y+=velocityY;
            for(Block wall:walls){
                if(collision(this,wall)){
                    this.x-=velocityX;
                    this.y-=velocityY;
                    this.direction=prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity(){
            if(this.direction=='U'){
                this.velocityX=0;
                this.velocityY=-tileSize/4;
            }
            else if(this.direction=='D'){
                this.velocityX=0;
                this.velocityY=tileSize/4;
            }
            else if(this.direction=='L'){
                this.velocityX=-tileSize/4;
                this.velocityY=0;
            }
            else {
                this.velocityX=tileSize/4;
                this.velocityY=0;
            }
        }
        void reset(){
            this.x=this.startX;
            this.y=this.startY;
        }

    }
    private int rowCount=21;
    private int columnCount=19;
    private int tileSize=32;
    private int boardWidth= columnCount*tileSize;
    private int boardHeight= rowCount*tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;
    
    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanRightImage;
    private Image pacmanLeftImage;

    private Image cherryImage;
    private Image powerFoodImage;
    private Image scaredGhostImage;
    /*X=wall
    O=skip 
    p=pac man
    ' '=food
    Ghosts: b=blue
    o=orange
    p=pink
    r=red
    */
    // private String[] tileMap = {
    //     "XXXXXXXXXXXXXXXXXXX",
    //     "X        X        X",
    //     "X XX XXX X XXX XX X",
    //     "X                 X",
    //     "X XX X XXXXX X XX X",
    //     "X    X       X    X",
    //     "XXXX XXXX XXXX XXXX",
    //     "OOOX X       X XOOO",
    //     "XXXX X XXrXX X XXXX",
    //     "O       bpo       O",
    //     "XXXX X XXXXX X XXXX",
    //     "OOOX X       X XOOO",
    //     "XXXX X XXXXX X XXXX",
    //     "X        X        X",
    //     "X XX XXX X XXX XX X",
    //     "X  X     P     X  X",
    //     "XX X X XXXXX X X XX",
    //     "X    X   X   X    X",
    //     "X XXXXXX X XXXXXX X",
    //     "X                 X",
    //     "XXXXXXXXXXXXXXXXXXX" 
    // };
//     private String[] tileMap = {
//     "XXXXXXXXXXXXXXXXXXX",
//     "XOOOO     X     OOOX",
//     "X XX XXX XXX XX XX X",
//     "X                 X",
//     "X XX X XXXXX X XX X",
//     "X    X   X   X    X",
//     "XXXX XXX X XXX XXXX",
//     "X    X       X    X",
//     "X XX X XXXXX X XX X",
//     "X    X brpo X    X",
//     "X XX X XXXXX X XX X",
//     "X    X       X    X",
//     "XXXX X XXXXX X XXXX",
//     "X                 X",
//     "X XX XXX X XXX XX X",
//     "X   X         X   X",
//     "XX X X XXXXX X X XX",
//     "X    X   P   X    X",
//     "X XX X XXXXX X XX X",
//     "XOOOO         OOOOX",
//     "XXXXXXXXXXXXXXXXXXX"
// };

/*X=wall
    O=skip 
    p=pac man
    ' '=food
    Ghosts: b=blue
    o=orange
    p=pink
    r=red
    C=Life/Cherry
    E=PowerFood/Energy
    */
    private String[] tileMap = {
    "XXXXXXXXXXXXXXXXXXX",
    "X O     X  E  O   X",
    "X XXX XXXXX XXX X X",
    "X   X   X   X   X X",
    "XXX XXX X XXX XXX X",
    "X   X   C   X     X",
    "X XXX XXXXX XXX XXX",
    "X X   X   X   X   X",
    "X X XXX X XXX XXX X",
    "X X X brpo  X   X X",
    "X XXX XXXXX XXX X X",
    "X     X   X   X X X",
    "XXX XXXXX X XXX X X",
    "X   X     X     X X",
    "X XXX XXXXX XXXXXX",
    "X   X   P   X     X",
    "X XXX XXXXXX XXX XX",
    "X     X    X      X",
    "X XXX XXX XXX XXX X",
    "XC  O    E    O   X",
    "XXXXXXXXXXXXXXXXXXX"
};


    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    HashSet<Block> cherry;
    HashSet<Block> powerFood;
    Block pacman;
    //Block cherry;
    //Block cherry2;

    Timer gameLoop;
    char[] directions={'U','D','L','R'};
    Random random=new Random();
    int score=0;
    int lives=3;
    boolean gameOver=false;

    PacMan(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //loadImages
        wallImage=new ImageIcon(getClass().getResource("/assets/wall.png")).getImage();
        blueGhostImage=new ImageIcon(getClass().getResource("/assets/blueGhost.png")).getImage();
        redGhostImage=new ImageIcon(getClass().getResource("/assets/redGhost.png")).getImage();
        orangeGhostImage=new ImageIcon(getClass().getResource("/assets/orangeGhost.png")).getImage();
        pinkGhostImage=new ImageIcon(getClass().getResource("/assets/pinkGhost.png")).getImage();

        pacmanDownImage=new ImageIcon(getClass().getResource("/assets/pacmanDown.png")).getImage();
        pacmanUpImage=new ImageIcon(getClass().getResource("/assets/pacmanUp.png")).getImage();
        pacmanLeftImage=new ImageIcon(getClass().getResource("/assets/pacmanLeft.png")).getImage();
        pacmanRightImage=new ImageIcon(getClass().getResource("/assets/pacmanRight.png")).getImage();

        cherryImage=new ImageIcon(getClass().getResource("/assets/cherry.png")).getImage();
        powerFoodImage=new ImageIcon(getClass().getResource("/assets/powerFood.png")).getImage();
        scaredGhostImage=new ImageIcon(getClass().getResource("/assets/scaredGhost.png")).getImage();

        loadMap();
        // System.out.println(walls.size());
        // System.out.println(foods.size());
        // System.out.println(ghosts.size());
        for(Block ghost:ghosts){
            char newDirection=directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gameLoop=new Timer(50,this);//20fps(1000/50) 1sec=1000milliseconds
        gameLoop.start();
    }

    public void loadMap(){
        walls=new HashSet<Block>();
        foods=new HashSet<Block>();
        ghosts=new HashSet<Block>();
        cherry=new HashSet<Block>();
        powerFood=new HashSet<Block>();

        for(int i=0;i<tileMap.length;i++){
            for(int j=0;j<tileMap[i].length();j++){
                char tileMapChar=tileMap[i].charAt(j);

                int x=j*tileSize;
                int y=i*tileSize;

                if(tileMapChar=='X'){
                    Block wall=new Block(wallImage, x, y, tileSize, tileSize);
                    walls.add(wall);
                }
                else if(tileMapChar =='b'){
                    Block ghost=new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='o'){
                    Block ghost=new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='r'){
                    Block ghost=new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar =='p'){
                    Block ghost=new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='P'){
                     pacman=new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if(tileMapChar==' '){
                    Block food=new Block(null, x+14, y+14, 4, 4);
                    foods.add(food);
                }
                else if(tileMapChar=='C'){
                    Block chery=new Block(cherryImage, x, y, tileSize, tileSize);
                    cherry.add(chery);
                }
                else if(tileMapChar=='E'){
                    Block pfood=new Block(powerFoodImage, x, y, tileSize, tileSize);
                    powerFood.add(pfood);
                }
                
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width,pacman.height,null) ;
    
        for(Block ghost:ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }
        for(Block wall:walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }
        g.setColor(Color.WHITE);
        for(Block food:foods){
            g.fillRect( food.x, food.y, food.width, food.height);
        }
        for(Block chery:cherry){
            g.drawImage(cherryImage, chery.x, chery.y, chery.width,chery.height, null);
        }
        for(Block pfood:powerFood){
            g.drawImage(powerFoodImage,pfood.x,pfood.y,pfood.width,pfood.height,null);
        }


        g.setFont(new Font("Arial",Font.PLAIN,18));
        if(foods.size()==0){
             g.setColor(Color.BLACK);
            g.fillRect(0, 6 * tileSize, boardWidth,6*tileSize);

            // g.setFont(new Font("Arial",Font.PLAIN,18));
            // g.setColor(Color.WHITE);
            // g.drawString("Game Over : "+String.valueOf(score),tileSize/2,tileSize/2);

            Graphics2D g2=(Graphics2D)g;
            g2.setFont(new Font("Arial", Font.PLAIN, 115));
            GradientPaint gradient = new GradientPaint(0, 0, Color.RED, boardWidth, 0, Color.YELLOW, true);
            g2.setPaint(gradient);
            g2.drawString("You Won", 40, 10 * tileSize);
        }
        else if(gameOver){
            // Fill background with black
            g.setColor(Color.BLACK);
            g.fillRect(0, 6 * tileSize, boardWidth,6*tileSize);

            // g.setFont(new Font("Arial",Font.PLAIN,18));
            // g.setColor(Color.WHITE);
            // g.drawString("Game Over : "+String.valueOf(score),tileSize/2,tileSize/2);

            Graphics2D g2=(Graphics2D)g;
            g2.setFont(new Font("Arial", Font.PLAIN, 115));
            GradientPaint gradient = new GradientPaint(0, 0, Color.RED, boardWidth, 0, Color.YELLOW, true);
            g2.setPaint(gradient);
            g2.drawString("Game Over", 0, 10 * tileSize);
        }
        else{
            g.drawString("x"+String.valueOf(lives)+"Score : "+String.valueOf(score),tileSize/2,tileSize/2);
        }
    }
    public void move(){
        pacman.x+=pacman.velocityX;
        pacman.y+=pacman.velocityY;

        for(Block wall:walls){
            if(collision(pacman,wall)){
                pacman.x-=pacman.velocityX;
                pacman.y-=pacman.velocityY;
                //System.out.println(pacman.x+" "+(wall.x+wall.width)+" "+pacman.y+" "+(wall.y+wall.height));
                break;
            }
        }
        for(Block ghost:ghosts){
            if(collision(pacman,ghost) && ghost.image!=scaredGhostImage){
                lives-=1;
                if(lives==0){
                    gameOver=true;
                    return;
                }
                resetPositions();
            }
            if(ghost.y==tileSize*9 && ghost.direction!='U' && ghost.direction!='D' ){
                //char[] qdirections={'U','D'};
                //char qdirection=qdirections[random.nextInt(2)]
                ghost.updateDirection('D');
            }
            ghost.x+=ghost.velocityX;
            ghost.y+=ghost.velocityY;
            for(Block wall:walls){
                if(collision(ghost,wall) || ghost.x<=0 ||ghost.x+ghost.width>=boardWidth)
                    {
                        ghost.x-=ghost.velocityX;
                        ghost.y-=ghost.velocityY;
                        char newDirection=directions[random.nextInt(4)];
                        ghost.updateDirection(newDirection);
                    }
            }
        }
        for(Block chery:cherry){
            if(collision(chery, pacman)){
                lives+=1;
                cherry.remove(chery);
            }
        }
        for(Block pfood:powerFood){
            if(collision(pacman, pfood)){
                for(Block ghost:ghosts){
                    ghost.image=scaredGhostImage;
                }
                powerFood.remove(pfood);

                // Start timer to revert ghost images after 3 seconds
                new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(Block ghost:ghosts){
                            ghost.image = ghost.originalImage;
                        }
                        ((Timer)e.getSource()).stop();
                    }
                }).start();
                break; // Only trigger once per power food
            }
        }
        Block foodEaten=null;
        for(Block food:foods){
            if(collision(pacman, food)){
               foodEaten=food;
                score+=10;
            }
        }
        foods.remove(foodEaten);
    }

    public boolean collision(Block a,Block b){
        return a.x<b.x +b.width && 
               a.x+ a.width> b.x&&
               a.y<b.y+b.height &&
               a.y+a.height>b.y;
    }

    public void resetPositions(){
        pacman.reset();
        pacman.velocityX=0;
        pacman.velocityY=0;
        for(Block ghost:ghosts){
            ghost.reset();
            char newDirection=directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver || foods.size()==0){
            gameLoop.stop();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {};

    @Override
    public void keyPressed(KeyEvent e) {};

    @Override
    public void keyReleased(KeyEvent e){
        if(gameOver|| foods.size()==0){
            loadMap();
            resetPositions();
            lives=3;
            score=0;
            gameOver=false;
            gameLoop.start();
        }
        if(e.getKeyCode()==KeyEvent.VK_UP)
            pacman.updateDirection('U');
        else if(e.getKeyCode()==KeyEvent.VK_DOWN)
            pacman.updateDirection('D');
        else if(e.getKeyCode()==KeyEvent.VK_LEFT)
            pacman.updateDirection('L');
        else
            pacman.updateDirection('R');

        if(pacman.direction=='U')
            pacman.image=pacmanUpImage;
        else if(pacman.direction=='D')
            pacman.image=pacmanDownImage;
        else if(pacman.direction=='L')
            pacman.image=pacmanLeftImage;
        else
            pacman.image=pacmanRightImage;
    };



}
