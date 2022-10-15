import java.util.ArrayList;
import java.util.Random;

public class Glitch extends Computer{

	Random random=new Random();
	
	Glitch(ArrayList<Snake> snake_list, String nam) {
		super(snake_list, nam);
		isGlitched=true;
		tag=Snake.GLITCH;
		colourTag=Snake.GLITCH_SKIN;
	}

	@Override
	protected void updateDirection() {
		if (Math.random() < 0.03 || !isAdult)
			idiling();//do nothin
		else if(Math.random()<0.3){//swap heads
			Point swaper=snakeList.get(random.nextInt(snakeList.size())).head();
			Point temp=new Point(swaper);
			swaper.setPoint(head());
			head().setPoint(temp);
		}else if(Math.random() < 0.35){//swap body parts
			int lim=random.nextInt(lenth()-1);
			for(int i=0;i<lim;i++){
				int a=random.nextInt(lenth()-1);
				Snake swapSnake=snakeList.get(random.nextInt(snakeList.size()));
				int b=random.nextInt(swapSnake.lenth()-1);
				Point temp=new Point(swapSnake.pt(b));
				swapSnake.pt(b).setPoint(pt(a));
				pt(a).setPoint(temp);
			}
		}else if(Math.random() < 0.5)
			tracker(GameBoard.enemy);//track enemy
		else if(Math.random() < 0.5)// track random snake
			tracker(snakeList.get(random.nextInt(snakeList.size())).head());
		else if(Math.random() < 0.5){//remove head
			int lim=random.nextInt(lenth()-1);
			for(int i=0;i<lim;i++){
				if(Math.random() < 0.4)pt(i).setPoint(-1, -1);
			}
		}else tracker(GameBoard.enemy);//track enemy(basicaly nothin)
	}
}
