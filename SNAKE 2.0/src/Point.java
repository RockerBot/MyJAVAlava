import java.util.Random;

public class Point {
	private Random random = new Random();
	int x, y;

	Point(int x, int y) {
		setPoint(x,y);
	}

	Point() {
		x = 0;
		y = 0;
	}

	Point(Point a) {
		setPoint(a);
	}

	void setPoint(Point a) {
		setPoint(a.x,a.y);
	}

	void setPoint(int x, int y){
		this.x = x;
		this.y = y;
	}

	boolean isSame(Point a) {
		if (this.x == a.x && this.y == a.y)
			return true;
		return false;
	}

	void randomizeInt(int x, int y){
		setPoint(random.nextInt(x), random.nextInt(y));
	}

	String disp() {
		return ("(" + this.x + ":" + this.y + ")");
	}
}
