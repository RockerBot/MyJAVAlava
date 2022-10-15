import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends JPanel implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1L;
	private ArrayList<Snake> snakeList = new ArrayList<Snake>();
	private Point[] hole = new Point[2];

	protected static boolean gamestart = false;
	protected static boolean grid = false;
	protected static boolean pvp = false;
	protected static boolean fastspawn = false;
	protected static boolean border = false;
	protected static boolean portal = false;
	protected static boolean multiportal = false;

	private boolean enemyeaten = false;
	private int timePassed = 0, babyID=-1, delay = 100, holeType = 0; // 0=normal, 1=clockwise, 2=anticlockwise,3=reverse

	private Toolkit tkit = Toolkit.getDefaultToolkit();
	private Font namef=new Font("arial", Font.BOLD, 14),normalf=new Font("arial", Font.PLAIN, 14);
	private static Random random = new Random();
	private Timer timer;

	protected static int pix = 25;// 25
	static int colno = 34;// 850/25=34-1(removing the first25pixels which arn't part of the columns)+1(remember 0th column)=34 <>
	static int rowno = 23;// 625/25=25-3(removing the first75pixels which arn't part of the columns)+1(remember 0th column)=23 ^v
	protected static Point gap = new Point(25,25);//(25, 75);
	protected static Point width = new Point(colno * pix + gap.x, rowno * pix + gap.y);// 34*25+25=850+25=875,23*25+75=575+75=650
	static Point enemy = new Point(random.nextInt(colno), random.nextInt(rowno));// (34)=0-33,(23)=0-22

	public GameBoard() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		hole[0] = new Point();
		hole[1] = new Point();
		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, Main.frameW, Main.frameH);

		g.setColor(Color.WHITE);
		g.drawRect(width.x+15-1, gap.y-1, 100+1, width.y-gap.y+1+pix*3);//draw border for scores
		g.drawRect(width.x+15+110-1, gap.y-1, 150+1, width.y-gap.y+1+pix*3);//draw border for scores
		g.drawRect(gap.x - 1, gap.y - 1, width.x - gap.x + 1, width.y - gap.y);// 24,74,851,577 draw border for gameplay
		

		g.setColor(Color.BLACK);
		g.fillRect(width.x+15, gap.y, 100, width.y-gap.y+pix*3);//draw background for scores
		g.fillRect(width.x+15+110, gap.y, 150, width.y-gap.y+pix*3);//draw background for scores
		g.fillRect(gap.x, gap.y, width.x - gap.x, width.y - gap.y);// (x,y,width,height) 25,75,850,575 draw background for gameplay
		
		g.setColor(Color.WHITE);
		g.fillRect(gap.x-1, width.y-1, width.x - gap.x+2, pix*3+2);//draw border for spawn
		g.setColor(Color.BLACK);
		g.fillRect(gap.x, width.y, width.x - gap.x, pix*3);//draw background for spawn

		//draw scorings
		drawScores(g);		
		int type = 0;
		for (Snake snake : snakeList) {
			if(snake.isDead)continue;
			type = (timePassed % 20 < 7 && snake.colourTag == Snake.GHOST_SKIN) ? 1 : 0;
			type=(timePassed%2==0&&snake.colourTag==Snake.GLITCH_SKIN)?1:0;
			// type=(timePassed%2==0&&k!=3)?0:((timePassed%4==1)?1:2);
			drawSnakes(g, snake, type);
		}
		// enemy or apple
		for (Snake snake : snakeList) {
			if (enemy.isSame(snake.head())&&snake.isAdult) {
				snake.pos.add(new Point(snake.tail()));
				snake.incFoodCount();
				enemyeaten = true;
			}
		}
		if (enemyeaten) {
			enemy.randomizeInt(colno, rowno);
			holeType = (multiportal) ? random.nextInt(4) : 0;
			hole[0].randomizeInt(colno, rowno);
			hole[1].randomizeInt(colno, rowno);
			enemyeaten = false;
		}
		imagenizer("res/enemy/FoodApple.png", g, enemy);

		if (portal)portalpaint(g, holeType);
		for (Snake snake : snakeList)checkCollision(g, snake);
		for(int i=0;i<snakeList.size();i++)if(snakeList.get(i).isDuplicate&&snakeList.get(i).isDead)snakeList.remove(i--);
		if (grid)grid(g);// grid
		for(Snake snake:snakeList)if(snake.isAsisted)furthestLRUD(g, snake.head());
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		do {
			timer.restart();
		} while (!gamestart);
		timePassed++;
		for (Snake snake : snakeList) {
			if (snake.isDead)continue;
			Point head=snake.head();
			for (int i = snake.lenth() - 1; i > 0; i--)snake.pt(i).setPoint(snake.pt(i - 1));
			head.y += (snake.direction == Snake.FACING_UP) ? (-1): (snake.direction == Snake.FACING_DOWN) ? 1 : 0;
			head.x += (snake.direction == Snake.FACING_LEFT) ? (-1): (snake.direction == Snake.FACING_RIGHT) ? 1 : 0;
			if(!border&&snake.isAdult)head.y = (head.y < 0) ? rowno - 1: (head.y > rowno - 1) ? 0 : head.y;
			if(!border||!snake.isAdult)head.x = (head.x < 0) ? colno - 1: (head.x > colno - 1) ? 0 : head.x;
			if (!snake.isAdult && head.y > rowno + 2)head.y = 0;
			snake.moves++;
		}
		for (int i=0;i<snakeList.size();i++)snakeList.get(i).updateDirection();
		if(portal)for (Snake snake : snakeList)blackhole(snake);
		for(Snake snake:snakeList)if(!snake.isAdult)babySit(snake);
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SLASH) {
			for (Snake snake : snakeList) {
				snake.isAdult=false;
				while (snake.lenth() > 3)snake.pos.remove(snake.lenth() - 1);
				snake.isDead = false;
				snake.direction = Snake.FACING_NULL;
			}
			repaint();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			for (Snake snake : snakeList) {
				if (snake.isDead) {
					snake.isAdult=false;
					while (snake.lenth() > 3)snake.pos.remove(snake.lenth() - 1);
					snake.isDead = false;
					snake.direction = Snake.FACING_NULL;
				}
			}
			repaint();
		} else if(e.getKeyCode() == KeyEvent.VK_P)snakeList.add(new Computer(snakeList, "Blip"));//extra snakes
		else for(Snake snake:snakeList)if(snake.isPlayer)snake.checkKey(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	public void blackhole(Snake snake) {
		if (snake.head().isSame(hole[0])) {
			snake.head().setPoint(hole[1]);
			switch (holeType) {
			case 1:
				snake.direction += (snake.direction == 4) ? (-3) : 1;
				break;
			case 2:
				snake.direction -= (snake.direction == 1) ? (-3) : 1;
				break;
			case 3:
				snake.direction += (snake.direction > 2) ? (-2) : 2;
				break;
			}
		} else if (snake.head().isSame(hole[1])) {
			snake.head().setPoint(hole[0]);
			switch (holeType) {
			case 1:
				snake.direction += (snake.direction == 4) ? (-3) : 1;
				break;
			case 2:
				snake.direction -= (snake.direction == 1) ? (-3) : 1;
				break;
			case 3:
				snake.direction += (snake.direction > 2) ? (-2) : 2;
				break;
			}
		}
	}

	public void furthestLRUD(Graphics g, Point head) {
		int ymin = (Math.abs(enemy.y - head.y) <= rowno / 2 || border) ? Math.abs(enemy.y - head.y)
				: rowno - Math.abs(enemy.y - head.y);
		int xmin = (Math.abs(enemy.x - head.x) <= colno / 2 || border) ? Math.abs(enemy.x - head.x)
				: colno - Math.abs(enemy.x - head.x);
		g.setColor(((xmin<ymin&&xmin!=0)||ymin==0)?Color.red:Color.cyan);
		if (enemy.x < head.x) {
			if (Math.abs(enemy.x - head.x) < colno / 2 || border) {
				g.drawRect(enemy.x * pix + gap.x, head.y * pix + gap.y, pix + pix / 2, pix);
				// img1=new ImageIcon("res/"+"LEFT.png");
			} else {
				g.drawRect(enemy.x * pix + gap.x - pix / 2, head.y * pix + gap.y, pix + pix / 2, pix);
				// img1=new ImageIcon("res/"+"RIGHT.png");
			}
			// img1.paintIcon(this,g,enemy.x,Y[0][k]);
		} else if (enemy.x > head.x) {
			if (Math.abs(enemy.x - head.x) < colno / 2 || border) {
				g.drawRect(enemy.x * pix + gap.x - pix / 2, head.y * pix + gap.y, pix + pix / 2, pix);
				// img1=new ImageIcon("res/"+"RIGHT.png");
			} else {
				g.drawRect(enemy.x * pix + gap.x, head.y * pix + gap.y, pix + pix / 2, pix);
				// img1=new ImageIcon("res/"+"LEFT.png");
			}
			// img1.paintIcon(this,g,enemy.x,Y[0][k]);
		}
		g.setColor(((ymin<xmin&&ymin!=0)||xmin==0)?Color.red:Color.cyan);
		if (enemy.y < head.y) {
			if (Math.abs(enemy.y - head.y) < rowno / 2 || border) {
				g.drawRect(head.x * pix + gap.x, enemy.y * pix + gap.y, pix, pix + pix / 2);
				// img2=new ImageIcon("res/"+"UP.png");
			} else {
				g.drawRect(head.x * pix + gap.x, enemy.y * pix + gap.y - pix / 2, pix, pix + pix / 2);
				// img2=new ImageIcon("res/"+"DOWN.png");
			}
			// img2.paintIcon(this,g,X[0][k],enemy.y);
		} else if (enemy.y > head.y) {
			if (Math.abs(enemy.y - head.y) < rowno / 2 || border) {
				g.drawRect(head.x * pix + gap.x, enemy.y * pix + gap.y - pix / 2, pix, pix + pix / 2);
				// img2=new ImageIcon("res/"+"DOWN.png");
			} else {
				g.drawRect(head.x * pix + gap.x, enemy.y * pix + gap.y, pix, pix + pix / 2);
				// img2=new ImageIcon("res/"+"UP.png");
			}
			// img2.paintIcon(this,g,X[0][k],enemy.y);
		}
	}

	public void checkCollision(Graphics g, Snake currSnake) {
		if(!currSnake.isAdult||currSnake.isDead)return;
		Point currHead = currSnake.head();
		for (Snake snake : snakeList) {
			if (!snake.isAdult || snake.tag != Snake.GHOST && (snake.ID != currSnake.ID && !pvp || currSnake.tag == Snake.GHOST))
				continue;
			for (int i=1;i<snake.lenth();i++){
				if (snake.pt(i).isSame(currHead)|| ((colno - 1 < currHead.x || rowno - 1 < currHead.y || 0 > currHead.x || 0 > currHead.y)&& border)) {
					if (snake.tag == Snake.GHOST && currSnake.tag != Snake.GHOST && currSnake.lenth() > 3) {
						currSnake.pos.remove(currSnake.lenth() - 1);
						snake.pos.add(new Point(snake.tail()));
					} else {
						if (!pvp && snake.tag == Snake.GHOST && currSnake.tag != Snake.GHOST)continue;
						currSnake.isDead = true;
						currSnake.deathCount++;
						while (currSnake.lenth() > 3)currSnake.pos.remove(currSnake.lenth() - 1);
						for (Point pt:currSnake.pos)pt.setPoint(-200,-200);
						if (fastspawn || currSnake.tag != Snake.PLAYER)currSnake.isAdult=false;
						return;
					}
				}
			}
		}
	}

	public void portalpaint(Graphics g, int holetype) {// TODO holes array (maybe more portals than just 2)
		String str = "res/portals/BlackHole"+holetype+"_"+((holetype==0)?(timePassed%6)/2:timePassed%4)+".png";
		imagenizer(str, g, hole[0]);
		imagenizer(str, g, hole[1]);
	}

	public void drawSnakes(Graphics g, Snake snake, int type) {
		String imgname[] = { "PLACEHOLDERS", "snake1", "snake2", "csnake", "gsnake", "lsnake", "rsnake", "splitsnake", "glitchsnake" };
		String imgstr = "img_missing.png";
		int i=0;
		Point prept=new Point(-1,-1), postpt=new Point(-1,-1);
		for(Point pt:snake.pos){
			if(pt==snake.head()){
				imgstr="/SnakeHead";
				if (snake.direction == Snake.FACING_RIGHT)imgstr+=("RIGHT");
				else if (snake.direction == Snake.FACING_LEFT)imgstr+=("LEFT");
				else if (snake.direction == Snake.FACING_UP)imgstr+=("UP");
				else if (snake.direction == Snake.FACING_DOWN)imgstr+=("DOWN");
			}else if(pt==snake.tail())imgstr="/SnakeTail"+((pt.y==prept.y)?((pt.x>prept.x)?"L":"R"):((pt.y>prept.y)?"U":"D"));
			else{
				imgstr="/SnakeBody";
				if(postpt.y==prept.y)imgstr+="LR";
				else if(postpt.x==prept.x)imgstr+="UD";
				else if((pt.x<prept.x&&pt.y>postpt.y)||(pt.x<postpt.x&&pt.y>prept.y))imgstr+="UR";
				else if((pt.x>prept.x&&pt.y<postpt.y)||(pt.x>postpt.x&&pt.y<prept.y))imgstr+="DL";
				else if((pt.x<prept.x&&pt.y<postpt.y)||(pt.x<postpt.x&&pt.y<prept.y))imgstr+="DR";
				else if((pt.x>prept.x&&pt.y>postpt.y)||(pt.x>postpt.x&&pt.y>prept.y))imgstr+="UL";
			}
			imagenizer("res/"+imgname[snake.colourTag]+imgstr+snake.colourTag+"_"+type+".png", g, pt);
			prept=pt;
			i++;
			postpt=(i<snake.lenth()-1)?snake.pt(i+1):null;
		}
	}

	public void drawScores(Graphics g){
		g.setColor(Color.WHITE);
		g.drawString("NAMES", width.x+45, gap.y-1);
		g.drawString("Apples", width.x+135, gap.y-1);
		g.drawString("Length", width.x+180, gap.y-1);
		g.drawString("Deaths", width.x+225, gap.y-1);
		for(int i=0;i<snakeList.size();i++){
			Snake snake=snakeList.get(i);
			g.setColor((snake.isPlayer)?Color.GREEN:(snake.isGhost||snake.isSpliter)?Color.WHITE:Color.ORANGE);
			g.setFont(normalf);
			g.drawString(snake.getFoodc()+"", width.x+150, gap.y+15+i*14);//apples
			g.drawString(snake.lenth()+"", width.x+195, gap.y+15+i*14);//length
			g.drawString(snake.deathCount+"", width.x+240, gap.y+15+i*14);//Deaths
			g.setFont(namef);
			g.drawString(snake.name, width.x+15, gap.y+15+i*14);//name
			g.setColor(Color.RED);
			if(snake.isDead){
				g.drawString("DEAD", width.x+15+60, gap.y+15+i*14);//shows if the snake is dead
				if (snake.tag == Snake.PLAYER&&snake.isAdult){
					g.setFont(new Font("arial",Font.BOLD,20));
					g.drawString("Press Space", gap.x, width.y+pix*4);
				}
			}
		}
	}

	public void babySit(Snake currSnake){// initializing start locations of the snake
		if(currSnake.ID==babyID){
			if(currSnake.tail().y<rowno){
				currSnake.isAdult=true;
				babyID=-1;
			}
			return;
		}
		for(Snake snake: snakeList){
			if(snake.isAdult||snake==currSnake)continue;
			if(snake.tail().y>=rowno)return;
		}
		if(babyID>=0&&snakeList.get(babyID).tail().y<rowno)snakeList.get(babyID).isAdult=true;
		babyID=currSnake.ID;
		for(int i=0;i<3;i++)currSnake.pt(i).setPoint(i+1,rowno+1);
		currSnake.isDead = false;
		currSnake.direction = Snake.FACING_LEFT;
	}

	public void grid(Graphics g) {
		g.setColor(Color.white);
		for (int i = 1; i < colno; i++)g.drawRect(gap.x - 1 + i * pix, gap.y - 1, pix, rowno * pix);
		g.setColor(Color.yellow);
		if (colno % 2 == 0)g.drawRect((colno / 2) * pix + gap.x - 1, gap.y - 1, 1, rowno * pix);
		else g.drawRect((colno / 2) * pix + gap.x - 1, gap.y - 1, pix, rowno * pix);
		g.setColor(Color.white);
		for (int i = 1; i < rowno; i++)g.drawRect(gap.x - 1, gap.y - 1 + i * pix, colno * pix + 1, pix);
		g.setColor(Color.yellow);
		if (rowno % 2 == 0)g.drawRect(gap.x - 1, (rowno / 2) * pix + gap.y - 1, colno * pix + 1, 1);
		else g.drawRect(gap.x - 1, (rowno / 2) * pix + gap.y - 1, colno * pix + 1, pix);
		if (border) {
			g.setColor(Color.orange);
			g.drawRect(gap.x - 1, gap.y - 1, colno * pix+1, rowno * pix);
		}
	}

	public void imagenizer(String imgstr, Graphics g, Point pos) {
		g.drawImage(tkit.getImage(imgstr), pos.x * pix + gap.x, pos.y * pix + gap.y, pix, pix, this);
	}

	protected void addSnake(int type, int skintype, String name, int[] keylings, boolean asist) {
		switch (type) {
		case Snake.PLAYER:
			snakeList.add(new Player(snakeList, skintype, name, keylings));
			break;
		case Snake.COMPUTER:
			snakeList.add(new Computer(snakeList, name));
			break;
		case Snake.GHOST:
			snakeList.add(new Ghost(snakeList, name));
			break;
		case Snake.TWIN:
			snakeList.add(new Twin(snakeList, name, Twin.LEFT));
			snakeList.add(new Twin(snakeList, name, Twin.RIGHT));
			break;
		case Snake.SPLITTER:
			snakeList.add(new Splitter(snakeList, name));
			break;
		case Snake.GLITCH:
			snakeList.add(new Glitch(snakeList, name));
			break;
		default:
			snakeList.add(new Snake(snakeList, skintype, name));
		}
		snakeList.get(snakeList.size()-1).isAsisted=asist;
	}
}