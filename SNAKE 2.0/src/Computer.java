import java.util.ArrayList;

public class Computer extends Snake{
	
	
	Computer(ArrayList<Snake> snake_list, String nam) {
		super(snake_list, Snake.COMPUTER_SKIN, nam);
		tag=Snake.COMPUTER;
		falsify();
		isComputerized = true;
	}

	@Override
	protected void updateDirection() {
		if (Math.random() < 0.03 )idiling();//made AI a little dumb
		else tracker(GameBoard.enemy);
	}

	public void tracker(Point target) {
		int direc = checkSurrounding();
		int ymin = (Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border) ? Math.abs(target.y - head().y)
				: GameBoard.rowno - Math.abs(target.y - head().y);
		int xmin = (Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border) ? Math.abs(target.x - head().x)
				: GameBoard.colno - Math.abs(target.x - head().x);
		if (((ymin < xmin && ymin != 0) || xmin == 0)
				&& ((((direc % 5 == 0 && target.y > head().y) || (direc % 7 == 0 && target.y < head().y))
						&& (Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border))
						|| (((direc % 5 == 0 && target.y < head().y) || (direc % 7 == 0 && target.y > head().y))
								&& Math.abs(target.y - head().y) > GameBoard.rowno / 2 && !GameBoard.border)))
			direction = (target.y > head().y) ? ((Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border) ? 3 : 1)
					: ((Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border) ? 1 : 3);
		else if (((xmin < ymin && xmin != 0) || ymin == 0)
				&& ((((direc % 2 == 0 && target.x > head().x) || (direc % 3 == 0 && target.x < head().x))
						&& (Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border))
						|| (((direc % 2 == 0 && target.x < head().x) || (direc % 3 == 0 && target.x > head().x))
								&& Math.abs(target.x - head().x) > GameBoard.colno / 2 && !GameBoard.border)))
			direction = (target.x > head().x) ? ((Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border) ? 2 : 4)
					: ((Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border) ? 4 : 2);
		else if ((((direc % 5 == 0 && target.y > head().y) || (direc % 7 == 0 && target.y < head().y))
				&& (Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border))
				|| (((direc % 5 == 0 && target.y < head().y) || (direc % 7 == 0 && target.y > head().y))
						&& Math.abs(target.y - head().y) > GameBoard.rowno / 2 && !GameBoard.border))
			direction = (target.y > head().y) ? ((Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border) ? 3 : 1)
					: ((Math.abs(target.y - head().y) <= GameBoard.rowno / 2 || GameBoard.border) ? 1 : 3);
		else if ((((direc % 2 == 0 && target.x > head().x) || (direc % 3 == 0 && target.x < head().x))
				&& (Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border))
				|| (((direc % 2 == 0 && target.x < head().x) || (direc % 3 == 0 && target.x > head().x))
						&& Math.abs(target.x - head().x) > GameBoard.colno / 2 && !GameBoard.border))
			direction = (target.x > head().x) ? ((Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border) ? 2 : 4)
					: ((Math.abs(target.x - head().x) <= GameBoard.colno / 2 || GameBoard.border) ? 4 : 2);
		else 
			idiling();
	}
}
