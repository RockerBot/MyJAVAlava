import java.util.ArrayList;

public class Prog_a {

}
/*
public class Boid {
//public double x,y,r,vx,vy,ax,ay,magnitude,theta,perceptionradius,maxF,maxV;
public double maxF,maxV,perceptionradius
public Vector pos,vel,acl;
public boolean highlight=false;//
Boid(double x,double y){
	this.perceptionradius=75.0;
	this.maxF=0.2;
	this.maxV=1;//4;
	this.x=x;
	this.y=y;
	this.magnitude=Math.random()+0.5;
	this.theta=Math.random()*Math.PI*2;
	this.vx=magnitude*Math.cos(theta);
	this.vy=magnitude*Math.sin(theta);
	this.ax=0.0;
	this.ay=0.0;
	this.r=4.0;//4.0
}
public void move(int x,int y){//delete
	this.x=x;
	this.y=y;
}
public Point steer(ArrayList<Point> otherbirds){
	Point alignVec=new Point(0.0,0.0,null);
	Point separationVec=new Point(0.0,0.0,null);
	Point cohesionVec=new Point(0.0,0.0,null);
	double d,thta,mag;
	for(Point other:otherbirds){
		if(other.data!=this){
			alignVec.x+=other.data.vx;
			alignVec.y+=other.data.vy;
				Point diff=new Point(this.x-other.data.x,this.y-other.data.y,null);
				mag=Math.sqrt(Math.pow(diff.x, 2)+Math.pow(diff.y, 2));
				thta=Math.atan2(diff.y, diff.x);
				diff.x=mag*Math.cos(thta);
				diff.y=mag*Math.sin(thta);
				diff.x/=mag;
				diff.y/=mag;
				separationVec.x+=diff.x;
				separationVec.y+=diff.y;
			cohesionVec.x+=other.data.x;
			cohesionVec.y+=other.data.y;
		}
	}
	if(otherbirds.size()>0){
		alignVec.x/=otherbirds.size();
		alignVec.y/=otherbirds.size();
			separationVec.x/=otherbirds.size();
			separationVec.y/=otherbirds.size();
		cohesionVec.x/=otherbirds.size();
		cohesionVec.y/=otherbirds.size();
		
		cohesionVec.x-=this.x;
		cohesionVec.y-=this.y;
		
		thta=Math.atan2(alignVec.y, alignVec.x);
		alignVec.x=maxV*Math.cos(thta);//set mag
		alignVec.y=maxV*Math.sin(thta);//set mag
			thta=Math.atan2(separationVec.y, separationVec.x);
			separationVec.x=maxV*Math.cos(thta);//set mag
			separationVec.y=maxV*Math.sin(thta);//set mag
		thta=Math.atan2(cohesionVec.y, cohesionVec.x);
		System.out.print(Math.toDegrees(thta)+"");
		cohesionVec.x=maxV*Math.cos(thta);//set mag
		cohesionVec.y=maxV*Math.sin(thta);//set mag
		
		alignVec.x-=this.vx;
		alignVec.y-=this.vy;
			separationVec.x-=this.vx;
			separationVec.y-=this.vy;
		cohesionVec.x-=this.vx;
		cohesionVec.y-=this.vy;
		
		thta=Math.atan2(alignVec.y, alignVec.x);
		mag=Math.min(Math.sqrt(Math.pow(alignVec.x, 2)+Math.pow(alignVec.y, 2)),maxF);
		alignVec.x=mag*Math.cos(thta);
		alignVec.y=mag*Math.sin(thta);
		
		thta=Math.atan2(separationVec.y, separationVec.x);
		mag=Math.min(Math.sqrt(Math.pow(separationVec.x, 2)+Math.pow(separationVec.y, 2)),maxF);
		separationVec.x=mag*Math.cos(thta);
		separationVec.y=mag*Math.sin(thta);
		
		thta=Math.atan2(cohesionVec.y, cohesionVec.x);
		mag=Math.min(Math.sqrt(Math.pow(cohesionVec.x, 2)+Math.pow(cohesionVec.y, 2)),maxF);
		//System.out.print(Math.toDegrees(thta)+":| \n");
		cohesionVec.x=mag*Math.cos(thta);
		cohesionVec.y=mag*Math.sin(thta);
		//System.out.println(cohesionVec.x+" x,y "+cohesionVec.y+"-_-_-");
	}
	Point delt=new Point(0.0,0.0,null);
	delt.x+=alignVec.x;
	delt.y+=alignVec.y;
	//delt.x=cohesionVec.x;
	//delt.y=cohesionVec.y;
	//delt.x+=separationVec.x;
	//delt.y+=separationVec.y;
	return delt;
	//return new Point(alignVec.x+separationVec.x+cohesionVec.x,alignVec.y+separationVec.y+cohesionVec.y,null); TODO
}
/**
public Point align(ArrayList<Point> otherbirds){
int perceptionRadius = 50;
Point steering = new Point();
///////////////int total = 0;
for (Point other: otherbirds) {
  /////////////////////////////float d = dist(this.position.x, this.position.y, other.position.x, other.position.y);
  if (other.data != this) {
    steering.add(other.velocity);
    /////////total++;
  }
}
if (total > 0) {
  steering.div(total);
  steering.setMag(this.maxSpeed);
  steering.sub(this.velocity);
  steering.limit(this.maxForce);
}
return steering;
}

PVector separation(Boid[] boids) {
int perceptionRadius = 50;
PVector steering = new PVector();
int total = 0;
for (Boid other: boids) {
  float d = dist(this.position.x, this.position.y, other.position.x, other.position.y);
  if (other != this && d < perceptionRadius) {
    PVector diff = PVector.sub(this.position, other.position);
    diff.div(d * d);
    steering.add(diff);
    total++;
  }
}
if (total > 0) {
  steering.div(total);
  steering.setMag(this.maxSpeed);
  steering.sub(this.velocity);
  steering.limit(this.maxForce);
}
return steering;
}

PVector cohesion(Boid[] boids) {
int perceptionRadius = 100;
PVector steering = new PVector();
int total = 0;
for (Boid other: boids) {
  float d = dist(this.position.x, this.position.y, other.position.x, other.position.y);
  if (other != this && d < perceptionRadius) {
    steering.add(other.position);
    total++;
  }
}
if (total > 0) {
  steering.div(total);
  steering.sub(this.position);
  steering.setMag(this.maxSpeed);
  steering.sub(this.velocity);
  steering.limit(this.maxForce);
}
return steering;
}
 * @param otherbirds
 
public void flockWith(ArrayList<Point> otherbirds){
	Point steering=new Point(0.0,0.0,null);
	steering=this.steer(otherbirds);
	this.ax=steering.x;
	this.ay=steering.y;
}
public void edges(int W,int H){
	this.x=(this.x>W)?0:(this.x<0)?W:this.x;
	this.y=(this.y>H)?0:(this.y<0)?H:this.y;
}
public void update(){
	this.x+=this.vx;
	this.y+=this.vy;
	this.vx+=this.ax;
	this.vy+=this.ay;
	this.theta=Math.atan2(vy, vx);
	System.out.println(Math.toDegrees(this.theta)+" ");
	this.magnitude=Math.min(Math.sqrt(Math.pow(vx, 2)+Math.pow(vy, 2)),maxV);
	this.vx=this.magnitude*Math.cos(this.theta);
	this.vy=this.magnitude*Math.sin(this.theta);
}
public boolean intersects(Boid other){
	return (Math.sqrt(Math.pow(this.x-other.x,2)+Math.pow(this.y-other.y,2))<this.r+other.r);
}
public void setHighlight(boolean b){
	highlight=b;
}
}
*/