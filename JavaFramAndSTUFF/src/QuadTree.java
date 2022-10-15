import java.util.ArrayList;

public class QuadTree {
	private Region boundary;
	private ArrayList<Point> points;
	private QuadTree NE,NW,SE,SW;
	private int cap;
	private boolean divided;
	public static int count=0;
	public static ArrayList<Double> a=new ArrayList<Double>();//?
	public static ArrayList<Double> b=new ArrayList<Double>();//?
	public static ArrayList<Double> c=new ArrayList<Double>();//?
	public static ArrayList<Double> d=new ArrayList<Double>();//?
	public static ArrayList<Double> XXX=new ArrayList<Double>();//?
	public static ArrayList<Double> YYY=new ArrayList<Double>();//?
	
	QuadTree(Region b,int c){
		this.boundary=b;
		this.cap=c;
		this.points=new ArrayList<Point>();
		this.divided=false;
	}
	public boolean insert(Point p){
		if(!this.boundary.contains(p))
			return false;
		else if(this.points.size()<this.cap){
			this.points.add(p);
			return true;
		}else{
			if(!this.divided)
				this.subdivide();
			
			if(this.NE.insert(p))
				return true;
			else if(this.NW.insert(p))
				return true;
			else if(this.SE.insert(p))
				return true;
			else if(this.SW.insert(p))
				return true;
			else{
				System.out.println("QUADTREE INSERT ERROR \n"+p.x+" "+p.y+"\t");
				return false;
			}
		}
	}
	private void subdivide(){
		double xx=this.boundary.x;
		double yy=this.boundary.y;
		double ww=this.boundary.w;
		double hh=this.boundary.h;
		int type=this.boundary.type;
		this.NE=new QuadTree(new Region(xx+ww/2,yy-hh/2,ww/2,hh/2,type),this.cap);
		this.NW=new QuadTree(new Region(xx-ww/2,yy-hh/2,ww/2,hh/2,type),this.cap);
		this.SE=new QuadTree(new Region(xx+ww/2,yy+hh/2,ww/2,hh/2,type),this.cap);
		this.SW=new QuadTree(new Region(xx-ww/2,yy+hh/2,ww/2,hh/2,type),this.cap);
		this.divided=true;
	}
	public ArrayList<Point> query(Region range){
		ArrayList<Point> found=new ArrayList();
		if(!this.boundary.intersects(range)){
			return found;
		}else{
			for(Point p:this.points){
				count++;
				if(range.contains(p)){
					found.add(p);
				}
			}
			if(this.divided){
				found.addAll(this.NE.query(range));
				found.addAll(this.NW.query(range));
				found.addAll(this.SE.query(range));
				found.addAll(this.SW.query(range));
			}
			return found;
		}
	}
	public void clearSTUFF(){//delete
		a.clear();
		b.clear();
		c.clear();
		d.clear();
		XXX.clear();
		YYY.clear();
	}
	public void MEshow(){//delete
		a.add(this.boundary.x-this.boundary.w);
		b.add(this.boundary.y-this.boundary.h);
		c.add(this.boundary.w*2);
		d.add(this.boundary.h*2);
		if(this.divided){
			this.NE.MEshow();
			this.NW.MEshow();
			this.SE.MEshow();
			this.SW.MEshow();
		}
		for(Point p:this.points){
			XXX.add(p.x);
			YYY.add(p.y);
		}
	}
}
