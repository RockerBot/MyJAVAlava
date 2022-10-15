import java.util.ArrayList;
import java.util.Scanner;

public class Boid {
	public double maxF,maxV,perceptionradius,r;
	public Vector pos,vel,acl;
	public boolean highlight=false;//
	public static ArrayList<Point> G=new ArrayList<Point>();//?
	Boid(double x,double y){
		this.perceptionradius=75.0;
		this.maxF=0.2;
		this.maxV=4;//4;
		this.pos=new Vector(x,y);
		this.vel=new Vector();
		this.acl=new Vector();
		this.vel.randomUnitA();
		this.vel.setmag(Math.random()+0.5);
		this.r=4.0;//4.0
	}
	public void move(int x,int y){//delete
		this.pos.x=x;
		this.pos.y=y;
	}
	public Vector steer(ArrayList<Point> otherbirds){
		Vector alignVec=new Vector();
		Vector separationVec=new Vector();
		Vector cohesionVec=new Vector();
		Vector steering=new Vector();
		for(Point other:otherbirds){
			if(other.data!=this){
				alignVec.add(other.data.vel);
				Vector diff=Vector.sub(this.pos, other.data.pos);
				diff.div(Math.pow(diff.x, 2)+Math.pow(diff.y, 2));
				separationVec.add(diff);
				cohesionVec.add(other.data.pos);
			}
		}
		cohesionVec.add(this.pos);
		if(otherbirds.size()>1){
			alignVec.div(otherbirds.size());
			separationVec.div(otherbirds.size());
			cohesionVec.div(otherbirds.size());
			Point GG=new Point(cohesionVec.x,cohesionVec.y,this);
			G.add(GG);
			cohesionVec.sub(this.pos);
			
			alignVec.setmag(this.maxV);//set mag
			separationVec.setmag(this.maxV);//set mag
			cohesionVec.setmag(this.maxV);//set mag//
			
			alignVec.sub(this.vel);
			separationVec.sub(this.vel);
			cohesionVec.sub(this.vel);
			
			alignVec.limit(this.maxF);
			separationVec.limit(this.maxF);
			cohesionVec.limit(this.maxF);
		}
		steering.add(alignVec);
		steering.add(separationVec);
		steering.add(cohesionVec);
		return steering;
	}
	public void flockWith(ArrayList<Point> otherbirds){
		this.acl=this.steer(otherbirds);
	}
	public void edges(int W,int H){
		this.pos.x=(this.pos.x>W)?0:(this.pos.x<0)?W:this.pos.x;
		this.pos.y=(this.pos.y>H)?0:(this.pos.y<0)?H:this.pos.y;
	}
	public void update(){
		this.pos.add(this.vel);
		this.vel.add(this.acl);
		this.vel.limit(this.maxV);
		this.acl.mult(0);
	}
	public boolean intersects(Boid other){
		return (Math.sqrt(Math.pow(this.pos.x-other.pos.x,2)+Math.pow(this.pos.y-other.pos.y,2))<this.r+other.r);
	}
	public void setHighlight(boolean b){
		highlight=b;
	}
}
