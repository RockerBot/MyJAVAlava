import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Snake {
	private int up, down, right, left, dir=1;

	Player(ArrayList<Snake> snake_list, int skintype, String nam, int[] keylings) {
		super(snake_list, skintype, nam);
		tag=Snake.PLAYER;
		falsify();
		isPlayer=true;
		assignKeylings(keylings);
	}

	@Override
	protected void updateDirection() {
		direction=(dir==0)?direction:dir;
		dir=0;
	}

	public void checkKey(KeyEvent e){
		if (e.getKeyCode() == right && direction != Snake.FACING_LEFT && !isDead)
			dir = Snake.FACING_RIGHT;
		else if (e.getKeyCode() == left && direction != Snake.FACING_RIGHT && !isDead)
			dir = Snake.FACING_LEFT;
		else if (e.getKeyCode() == up && direction != Snake.FACING_DOWN && !isDead)
			dir = Snake.FACING_UP;
		else if (e.getKeyCode() == down && direction != Snake.FACING_UP && !isDead)
			dir = Snake.FACING_DOWN;
	}

	private void assignKeylings(int[] keylings){
		up=keylings[Snake.FACING_UP-1];
		down=keylings[Snake.FACING_DOWN-1];
		right=keylings[Snake.FACING_RIGHT-1];
		left=keylings[Snake.FACING_LEFT-1];
	}
}
