import java.util.ArrayList;

public class Ghost extends Computer{

	Ghost(ArrayList<Snake> snake_list, String nam) {
		super(snake_list, nam);
		falsify();
		tag=Snake.GHOST;
		colourTag=Snake.GHOST_SKIN;
		isGhost=true;
	}

	@Override
	protected void updateDirection() {
		if (!isAdult)idiling();
		else{
			boolean flag=false;
			for(Snake snake:snakeList){
				if(snake.lenth()-30<Math.random()*10||snake==this)continue;
				tracker(snake.head());
				flag=true;
				break;
			}
			if (!flag) {
				int xmin=GameBoard.enemy.x-head().x,ymin=GameBoard.enemy.y-head().y;
				if (((Math.abs(xmin) <= lenth() / 5 + 1)|| (Math.abs(xmin - GameBoard.colno) <= lenth() / 5 + 1)|| (Math.abs(xmin + GameBoard.colno) <= lenth() / 5 + 1))&& ((Math.abs(ymin) <= lenth() / 5 + 1)|| (Math.abs(ymin - GameBoard.rowno) <= lenth() / 5 + 1)|| (Math.abs(ymin + GameBoard.rowno) <= lenth() / 5 + 1))) // (xmin<=snakelen[3]/5+1)&&(ymin<=snakelen[3]/5+1)
					sharkMode();
				else
					tracker(GameBoard.enemy);
			}
		}
	}

	protected void sharkMode() {// TODO //TODO move
		int dir = checkSurrounding();
		if (Math.abs(GameBoard.enemy.y - head().y) < lenth() / 5 + 1 && Math.abs(GameBoard.enemy.x - head().x) < lenth() / 5 + 1&& dir % 7 == 0)
			return;
		else {
			if (head().x - GameBoard.enemy.x == lenth() / 5 + 1|| ((head().x + GameBoard.colno) - GameBoard.enemy.x == lenth() / 5 + 1 && !GameBoard.border)) { // R
				if (head().y - GameBoard.enemy.y == lenth() / 5 + 1 && dir % 3 == 0|| ((head().y + GameBoard.rowno) - GameBoard.enemy.y == lenth() / 5 + 1 && !GameBoard.border) && dir % 3 == 0) // RDcorner
					direction = Snake.FACING_LEFT;
				else if (GameBoard.enemy.y - head().y == lenth() / 5 + 1 && dir % 5 == 0|| (GameBoard.enemy.y - (head().y - GameBoard.rowno) == lenth() / 5 + 1 && !GameBoard.border) && dir % 5 == 0) // RUcorner
					direction = Snake.FACING_DOWN;
				else if (Math.abs(GameBoard.enemy.y - head().y) < lenth() / 5 + 1 && dir % 5 == 0|| (Math.abs(GameBoard.enemy.y - (head().y + GameBoard.rowno)) < lenth() / 5 + 1 && !GameBoard.border) && dir % 5 == 0|| (Math.abs(GameBoard.enemy.y - (head().y - GameBoard.rowno)) < lenth() / 5 + 1 && !GameBoard.border) && dir % 5 == 0) // Rline
					direction = Snake.FACING_DOWN;
				else
					idiling();
			} else if (GameBoard.enemy.x - head().x == lenth() / 5 + 1|| (GameBoard.enemy.x - (head().x - GameBoard.colno) == lenth() / 5 + 1 && !GameBoard.border)) { // L
				if ((head().y - GameBoard.enemy.y == lenth() / 5 + 1 && dir % 7 == 0)|| (((head().y + GameBoard.rowno) - GameBoard.enemy.y == lenth() / 5 + 1 && !GameBoard.border) && dir % 7 == 0)) // LDcorner
					direction = Snake.FACING_UP;
				else if ((GameBoard.enemy.y - head().y == lenth() / 5 + 1 && dir % 2 == 0)|| ((GameBoard.enemy.y - (head().y - GameBoard.rowno) == lenth() / 5 + 1 && !GameBoard.border) && dir % 2 == 0)) // LUcorner
					direction = Snake.FACING_RIGHT;
				else if ((Math.abs(GameBoard.enemy.y - head().y) < lenth() / 5 + 1 && dir % 7 == 0)|| ((Math.abs(GameBoard.enemy.y - (head().y + GameBoard.rowno)) < lenth() / 5 + 1 && !GameBoard.border) && dir % 7 == 0)|| ((Math.abs(GameBoard.enemy.y - (head().y - GameBoard.rowno)) < lenth() / 5 + 1 && !GameBoard.border) && dir % 7 == 0)) // Lline
					direction = Snake.FACING_UP;
				else
					idiling();
			} else if ((Math.abs(GameBoard.enemy.x - head().x) <= lenth() / 5 + 1)|| ((Math.abs(GameBoard.enemy.x - (head().x + GameBoard.colno)) < lenth() / 5 + 1 && !GameBoard.border))|| ((Math.abs(GameBoard.enemy.x - (head().x - GameBoard.colno)) < lenth() / 5 + 1 && !GameBoard.border))) { // middle
				if (GameBoard.enemy.y - head().y == lenth() / 5 + 1 && dir % 2 == 0|| (GameBoard.enemy.y - (head().y - GameBoard.rowno) == lenth() / 5 + 1 && !GameBoard.border) && dir % 2 == 0) // Umiddle
					direction = Snake.FACING_RIGHT;
				else if (head().y - GameBoard.enemy.y == lenth() / 5 + 1 && dir % 3 == 0|| ((head().y + GameBoard.rowno) - GameBoard.enemy.y == lenth() / 5 + 1 && !GameBoard.border) && dir % 3 == 0) // Dmiddle
					direction = Snake.FACING_LEFT;
				else
					idiling();
			} else
				idiling();
		}
	}
}
