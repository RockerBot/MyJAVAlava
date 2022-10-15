
public class Region {
	public double x,y,w,h,r;
	public int type;
	public static final int RECTANGLE=0,CIRCLE=1;
	Region(double x, double y, double w, double h,int type){
		this.x=x;//center
		this.y=y;//center
		this.type=type;
		if(type==RECTANGLE){
			this.w=w;//not really width(actually width=w*2)
			this.h=h;//not really hight(actually hight=h*2)
		}else{
			System.out.println("WRONG initialization(dis4Rectangle)");
		}
	}
	Region(double x, double y, double r,int type){
		this.x=x;//center
		this.y=y;//center
		this.type=type;
		if(type==CIRCLE){
			this.r=r;//radius
		}else{
			System.out.println("WRONG initialization(dis4Circle)");
		}
	}
	public boolean contains(Point p){
		if(this.type==RECTANGLE){
			return (p.x>=this.x-this.w && 
					p.x<=this.x+this.w && 
					p.y>=this.y-this.h && 
					p.y<=this.y+this.h);
		}else if(this.type==CIRCLE){
			return ( Math.pow(p.x-this.x,2)+Math.pow(p.y-this.y,2)<=this.r*this.r);
		}else{
			System.out.println("WRONG TYPE");
			return false;
		}
	}
	public boolean intersects(Region range){
		if(this.type==RECTANGLE){
			if(range.type==RECTANGLE){
				return !( range.x-range.w>this.x+this.w||
				range.x+range.w<this.x-this.w||
				range.y-range.h>this.y+this.h||
				range.y+range.h<this.y-this.h);
			}else if(range.type==CIRCLE){
				return (this.contains(new Point(range.x,range.y,null))||
						range.contains(new Point(this.x-this.w,this.y-this.h,null))||
						range.contains(new Point(this.x-this.w,this.y+this.h,null))||
						range.contains(new Point(this.x+this.w,this.y-this.h,null))||
						range.contains(new Point(this.x+this.w,this.y+this.h,null))||
						(Math.abs(range.x-this.x-this.w)<=range.r&&(range.y<=this.y+this.h)&&(range.y>=this.y+this.h))||
						(Math.abs(range.x-this.x+this.w)<=range.r&&(range.y<=this.y+this.h)&&(range.y>=this.y+this.h))||
						(Math.abs(range.y-this.y-this.h)<=range.r&&(range.x<=this.x+this.w)&&(range.x>=this.x+this.w))||
						(Math.abs(range.y-this.y+this.h)<=range.r&&(range.x<=this.x+this.w)&&(range.x>=this.x+this.w)));
			}else{
				System.out.println("WRONG TYPE for range");
				return false;
			}
		}else if(this.type==CIRCLE){
			if(range.type==RECTANGLE){
				return (range.contains(new Point(this.x,this.y,null))||
						this.contains(new Point(range.x-range.w,range.y-range.h,null))||
						this.contains(new Point(range.x-range.w,range.y+range.h,null))||
						this.contains(new Point(range.x+range.w,range.y-range.h,null))||
						this.contains(new Point(range.x+range.w,range.y+range.h,null))||
						(Math.abs(this.x-range.x-range.w)<=this.r&&(this.y<=range.y+range.h)&&(this.y>=range.y+range.h))||
						(Math.abs(this.x-range.x+range.w)<=this.r&&(this.y<=range.y+range.h)&&(this.y>=range.y+range.h))||
						(Math.abs(this.y-range.y-range.h)<=this.r&&(this.x<=range.x+range.w)&&(this.x>=range.x+range.w))||
						(Math.abs(this.y-range.y+range.h)<=this.r&&(this.x<=range.x+range.w)&&(this.x>=range.x+range.w)));
			}else if(range.type==CIRCLE){
				return (Math.pow(this.x-range.x,2)+Math.pow(this.y-range.y,2)<=Math.pow(range.r+this.r,2));
			}else{
				System.out.println("WRONG TYPE for range");
				return false;
			}
		}else{
			System.out.println("WRONG TYPE for this");
			return false;
		}
	}
}
