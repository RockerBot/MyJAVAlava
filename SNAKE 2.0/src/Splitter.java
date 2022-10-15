import java.util.ArrayList;

public class Splitter extends Computer{

	int parentID=-1;
	private static int splitterCount=0;
	Splitter(ArrayList<Snake> snake_list, String nam) {
		super(snake_list, nam);
		splitterCount++;
		isSpliter=true;
		tag=Snake.SPLITTER;
		colourTag=Snake.SPLITTER_SKIN;
	}
	Splitter(ArrayList<Snake> snake_list, Splitter snake) {
		super(snake_list, ((snake.isDuplicate)?snake.name:snake.name+"Copy"));
		isSpliter=true;
		isDuplicate=true;
		isAdult=true;
		for (int i = 0; i < 3; i++)pt(i).setPoint(snake.pt(snake.lenth()-1-i));
		splitterCount++;
		parentID=(snake.isDuplicate)?snake.parentID:snake.ID;
		tag=Snake.SPLITTER;
		colourTag=Snake.SPLITTER_SKIN;
		idiling();
	}

	@Override
	protected void updateDirection() {
		if (Math.random() < 0.03 || !isAdult)idiling();
		else tracker(GameBoard.enemy);
		super.updateDirection();
		if(lenth()>5&&Math.random()<0.5&&splitterCount<1000){
			snakeList.add(new Splitter(snakeList, this));
			for(int i=0;i<3;i++)pos.remove(lenth()-1);
		}
	}
	
	@Override
	public void incFoodCount(){
		if(isDuplicate)snakeList.get(parentID).incFoodCount();
		super.incFoodCount();
	}
}
