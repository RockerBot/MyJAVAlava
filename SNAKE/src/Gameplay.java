import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
    private int[] X1=new int[750];
    private int[] Y1=new int[750];
    
    private boolean left1=false;
    private boolean right1=true;
    private boolean up1=false;
    private boolean down1=false;
    private boolean gameover=false;
    
    private int snakelen1=3;
    
    private ImageIcon mouthr1;
    private ImageIcon mouthl1;
    private ImageIcon mouthu1;
    private ImageIcon mouthd1;
    
    private ImageIcon snakeimage;
    private ImageIcon enemyimage;
    
    private Random random=new Random();
    private Timer timer;
    private int delay=100;
    private int moves=0;
    
    private int enemyX =25*(random.nextInt(34)+1);
    private int enemyY =25*(random.nextInt(23)+3);
    //private int[] enemyXpos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    //private int[] enemyYpos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,475,500,525,550,575,600,625};
    public Gameplay(){
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer=new Timer(delay,this);
        timer.start();
    }
    public void paint(Graphics g){
        if(moves==0){
            X1[2]=50;
            X1[1]=75;
            X1[0]=100;
            
            Y1[2]=100;
            Y1[1]=100;
            Y1[0]=100;
        }
        //draw title image border
        g.setColor(Color.white);
        g.drawRect(24,10,851,55);// (x,y,width,hi)
        
        //draw title img
        g.setColor(Color.black);
        g.fillRect(25,11,850,53);//(x,y,width,height)
        //titleImage=new ImageIcon("Snakehead.png");
        //titleImage.paintIcon(this,g,25,11);//(this, g(object we inputed),x,y)
        
        //draw border for gameplay
        g.setColor(Color.WHITE);
        g.drawRect(24,74,851,577);
        
        //draw background for gameplay
        g.setColor(Color.black);
        g.fillRect(25,75,850,575);//(x,y,width,height)
        
        //draw score
        g.setColor(Color.white);
        g.setFont(new Font("arial",Font.PLAIN, 14));
        g.drawString("Score: "+(snakelen1-3), 780, 30);
        
        //draw length
        g.setColor(Color.white);
        g.setFont(new Font("arial",Font.PLAIN, 14));
        g.drawString("Length: "+snakelen1, 780, 50);

        for(int i=1;i<snakelen1;i++){
            snakeimage=new ImageIcon("SnakeBody1.png");
            snakeimage.paintIcon(this,g,X1[i],Y1[i]);
        }
        
        if (right1){
            mouthr1=new ImageIcon("SnakeHeadRIGHT1.png");
            mouthr1.paintIcon(this,g,X1[0],Y1[0]);
        }
        else if (left1){
            mouthl1=new ImageIcon("SnakeHeadLEFT1.png");
            mouthl1.paintIcon(this,g,X1[0],Y1[0]);
        }
        else if (up1){
            mouthu1=new ImageIcon("SnakeHeadUP1.png");
            mouthu1.paintIcon(this,g,X1[0],Y1[0]);
        }
        else if (down1){
            mouthd1=new ImageIcon("SnakeHeadDOWN1.png");
            mouthd1.paintIcon(this,g,X1[0],Y1[0]);
        }
        enemyimage=new ImageIcon("FoodApple.png");
        
        if(enemyX==X1[0] && enemyY==Y1[0]){
            snakelen1++;
            enemyX =25*(random.nextInt(34)+1);
            enemyY =25*(random.nextInt(23)+3);
        }
        
        enemyimage.paintIcon(this,g,enemyX,enemyY);
        
        for(int i=1;i<snakelen1;i++){
            if(X1[i]==X1[0] && Y1[i]==Y1[0]){
                gameover=true;
                g.setColor(Color.white);
                g.setFont(new Font("arial",Font.BOLD, 50));
                g.drawString("GAME OVER", 300, 300);
                g.setFont(new Font("arial",Font.BOLD, 20));
                g.drawString("Press SPACE to restart", 350, 340);
            }
        }
        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e){
        timer.start();
        if(right1 && !gameover){
            for(int i=snakelen1-1;i>=0;i--){
                Y1[i+1]=Y1[i];
                X1[i]=(i==0)?X1[i]+25:X1[i-1];
                X1[i]=(X1[i]>850)?25:X1[i];
            }
        }
        else if(left1 && !gameover){
            for(int i=snakelen1-1;i>=0;i--){
                Y1[i+1]=Y1[i];
                X1[i]=(i==0)?X1[i]-25:X1[i-1];
                X1[i]=(X1[i]<25)?850:X1[i];
            }
        }
        else if(down1 && !gameover){
            for(int i=snakelen1-1;i>=0;i--){
                X1[i+1]=X1[i];
                Y1[i]=(i==0)?Y1[i]+25:Y1[i-1];
                Y1[i]=(Y1[i]>625)?75:Y1[i];
            }
        }
        else if(up1 && !gameover){
            for(int i=snakelen1-1;i>=0;i--){
                X1[i+1]=X1[i];
                Y1[i]=(i==0)?Y1[i]-25:Y1[i-1];
                Y1[i]=(Y1[i]<75)?625:Y1[i];
            }
        }
        repaint();
    }
    @Override
    public void keyTyped(KeyEvent e){
        //d
    }
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            moves=0;
            snakelen1=3;
            gameover=false;
            right1=true;
            left1=false;
            up1=false;
            down1=false;
            repaint();
        }
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT && !left1 && !gameover){
            moves++;
            right1=true;
            left1=false;
            up1=false;
            down1=false;
        }
        else if(e.getKeyCode()==KeyEvent.VK_LEFT && !right1 && !gameover){
            moves++;
            right1=false;
            left1=true;
            up1=false;
            down1=false;
        }
        else if(e.getKeyCode()==KeyEvent.VK_UP && !down1 && !gameover){
            moves++;
            right1=false;
            left1=false;
            up1=true;
            down1=false;
        }
        else if(e.getKeyCode()==KeyEvent.VK_DOWN && !up1 && !gameover){
            moves++;
            right1=false;
            left1=false;
            up1=false;
            down1=true;
        }
        else if(e.getKeyCode()==KeyEvent.VK_X && !gameover){
            snakelen1++;
        }
        else if(e.getKeyCode()==KeyEvent.VK_ENTER && !gameover){
            delay-=(delay>1)?1:0;
        }
        else if(e.getKeyCode()==KeyEvent.VK_PLUS && !gameover){
            delay+=(delay<500)?1:0;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
        //d
    }
}