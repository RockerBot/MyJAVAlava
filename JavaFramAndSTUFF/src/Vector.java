
public class Vector {
	public double x,y;
	private double mag,theta;
	Vector(double x,double y){
		this.x=x;
		this.y=y;
		Cart2Pol(x,y);
	}
	Vector(){
		this.x=0.0;
		this.y=0.0;
	}
	public void Pol2Cart(double mag,double thta){this.x=mag*Math.cos(thta);this.y=mag*Math.sin(thta);}
	public void Pol2Cart(){this.x=this.mag*Math.cos(this.theta);this.y=this.mag*Math.sin(this.theta);}
	public void Cart2Pol(double x,double y){this.mag=Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));this.theta=Math.atan2(y, x);}
	public void Cart2Pol(){this.mag=Math.sqrt(Math.pow(this.x, 2)+Math.pow(this.y, 2));this.theta=Math.atan2(this.y, this.x);}
	
	public void randomUnitA(){
		this.theta=Math.random()*Math.PI*2;
		this.mag=1;
		Pol2Cart();}
	
	public static Vector sub(Vector a,Vector b){return new Vector(a.x-b.x,a.y-b.y);}
	public void sub(Vector val){this.x-=val.x;this.y-=val.y;}
	public static Vector add(Vector a,Vector b){return new Vector(a.x+b.x,a.y+b.y);}
	public void add(Vector val){this.x+=val.x;this.y+=val.y;}
	
	public void setmag(double val){//sets the magnitude
		Cart2Pol();
		this.mag=val;
		Pol2Cart();}
	public void setAngRad(double val){
		Cart2Pol();
		this.theta=val;
		Pol2Cart();}
	public void setAngDeg(double val){
		Cart2Pol();
		this.theta=Math.toRadians(val);
		Pol2Cart();}
	public void limit(double val){
		Cart2Pol();
		this.mag=Math.min(this.mag,val);
		Pol2Cart();}
	
	public void multMag(double val){
		Cart2Pol();
		this.mag*=val;
		Pol2Cart();}
	public void divMag(double val){this.multMag(1.0/val);}
	public void mult(double val){this.x*=val;this.y*=val;}
	public void div(double val){this.mult(1.0/val);}
}
