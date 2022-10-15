import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Snake {
	protected int direction, deathCount, snakeType, ID, tag, moves, colourTag;
	private int eatenFoodCount;
	protected final static int FACING_NULL = 0, FACING_UP = 1, FACING_RIGHT = 2, FACING_DOWN = 3, FACING_LEFT = 4,
			UNIDENTIFIED = 0, PLAYER = 1, COMPUTER = 2, GHOST = 3, TWIN = 4, SPLITTER = 5, GLITCH = 6,
			PLACEHOLDER_SKIN=0,PLAYER1_SKIN=1,PLAYER2_SKIN=2,COMPUTER_SKIN=3,GHOST_SKIN=4,LTWIN_SKIN=5,RTWIN_SKIN=6,SPLITTER_SKIN=7,GLITCH_SKIN=8;
	protected boolean isPlayer = false, isComputerized = false, isGhost = false, isJumbo = false, isTwin = false,
			isSpliter = false, isGlitched = false, isAsisted=false,isDuplicate=false, isAdult=true, isDead;
	protected ArrayList<Point> pos = new ArrayList<Point>();
	public ArrayList<Snake> snakeList;
	protected String name;

	Snake(ArrayList<Snake> snake_list, int skintype, String nam) {
		ID = snake_list.size();
		snakeList = snake_list;
		for (int i = 0; i < 3; i++)pos.add(new Point(-200,-200));
		deathCount = 0;
		eatenFoodCount = 0;
		direction = 0;
		snakeType = 0;
		moves = 0;
		tag = Snake.PLAYER;
		colourTag=skintype;
		name = nam;
		falsify();
	}

	protected void updateDirection() {
		idiling();
	}

	public void idiling() {
		int direc = this.checkSurrounding();
		int dir[] = { -1, 7, 2, 5, 3 };
		if (direc % dir[direction] != 0) {
			if (direc % 7 == 0)
				this.direction = FACING_UP;
			else if (direc % 3 == 0)
				this.direction = FACING_LEFT;
			else if (direc % 5 == 0)
				this.direction = FACING_DOWN;
			else if (direc % 2 == 0)
				this.direction = FACING_RIGHT;
		}
	}

	public int checkSurrounding() {
		boolean r = true;// 2
		boolean l = true;// 3
		boolean d = true;// 5
		boolean u = true;// 7
		int direc = 1;
		for (Snake snake : snakeList) {
			if (snake.isGhost || (!GameBoard.pvp && snake.ID != this.ID))
				continue;
			for (Point point : snake.pos) {
				if (point==snake.head())
					continue;
				if (((this.head().x + 1 == point.x || this.head().x == GameBoard.colno - 1 && point.x == 0)
						&& point.y == this.head().y) || (this.head().x == GameBoard.colno - 1 && GameBoard.border))
					r = false;
				if (((this.head().x - 1 == point.x || this.head().x == 0 && point.x == GameBoard.colno - 1)
						&& point.y == this.head().y) || (this.head().x == 0 && GameBoard.border))
					l = false;
				if (((this.head().y + 1 == point.y || this.head().y == GameBoard.rowno - 1 && point.y == 0)
						&& point.x == this.head().x) || (this.head().y == GameBoard.rowno - 1 && GameBoard.border))
					d = false;
				if (((this.head().y - 1 == point.y || this.head().y == 0 && point.y == GameBoard.rowno - 1)
						&& point.x == this.head().x) || (this.head().y == 0 && GameBoard.border))
					u = false;
			}
		}
		if (r)
			direc *= 2;
		if (l)
			direc *= 3;
		if (d)
			direc *= 5;
		if (u)
			direc *= 7;
		return direc;
	}

	public void checkKey(KeyEvent e){}

	public void incFoodCount(){
		eatenFoodCount++;
	}

	public int getFoodc(){
		return eatenFoodCount;
	}

	public int lenth(){
		return pos.size();
	}
	public Point pt(int i){
		return pos.get(i);
	}
	public Point head(){
		return pt(0);
	}
	public Point tail(){
		return pt(lenth()-1);
	}
	public void falsify(){
		isDead = false;
		isPlayer = false;
		isComputerized = false;
		isGhost = false;
		isJumbo = false;
		isTwin = false;
		isSpliter = false;
		isGlitched = false;
		isDuplicate=false;
		isAdult=false;
	}
}
