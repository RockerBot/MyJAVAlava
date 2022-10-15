import java.util.ArrayList;

public class Twin extends Computer{//TODO needs reworking, maybe can interact with stuff(eat apple,colide and die, cas other snakes to colide and die) only when joint together

	protected final static int LEFT=1, RIGHT=2;
	protected boolean isLeft=false;
	protected int myTwin;
	Twin(ArrayList<Snake> snake_list, String nam, int side) {
		super(snake_list, nam);
		isTwin=true;
		isLeft=(side==Twin.LEFT)?true:false;
		myTwin=ID+((isLeft)?1:(-1));
		tag=Snake.TWIN;
		colourTag=(isLeft)?Snake.LTWIN_SKIN:Snake.RTWIN_SKIN;
	}

	@Override
	protected void updateDirection() {
		if (!isAdult)idiling();//Math.random() < 0.03 || 
		else {
			if(!head().isSame(snakeList.get(myTwin).head())&&lenth()!=3){
				System.out.print("\nMULA");
				tracker(snakeList.get(myTwin).head());
			}
			else {
				System.out.println("\nENEMY");
				tracker(GameBoard.enemy);
			}
		}
	}
	
	public void idiling() {
		if(isLeft)super.idiling();
		else {
			int direc = this.checkSurrounding();
			int dir[] = { -1, 7, 2, 5, 3 };
			if (direc % dir[direction] != 0) {
				if (direc % 5 == 0)this.direction = FACING_DOWN;
				else if (direc % 2 == 0)this.direction = FACING_RIGHT;
				else if (direc % 7 == 0)this.direction = FACING_UP;
				else if (direc % 3 == 0)this.direction = FACING_LEFT;
			}
		}
	}
}
