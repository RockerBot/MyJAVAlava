
public class Point {
	public double x,y;
	public Boid data;
	Point(double x,double y,Boid d){
		this.x=x;
		this.y=y;
		this.data=d;
	}
	Point(){
		this.x=0.0;
		this.y=0.0;
		this.data=null;
	}
}
