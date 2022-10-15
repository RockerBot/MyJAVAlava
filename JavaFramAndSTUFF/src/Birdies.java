import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Birdies extends JPanel implements ComponentListener,ActionListener,KeyListener,MouseListener,MouseMotionListener,WindowListener{
	private JFrame frame;
	private Dimension screenSize,windowSize;
	private Image frameicon;
	private Timer timer;
	private String errmsg;
	private int frameW,frameH,OX,OY,T,mouseX,mouseY,birdNo;
	private QuadTree qtree;
	private Region range,circRange;
	private ArrayList<Boid> birds=new ArrayList<Boid>();
	private ArrayList<Obstacle> block=new ArrayList<Obstacle>();
	private ArrayList<Integer>ROKTINGLS_a=new ArrayList<Integer>();
	private ArrayList<Integer>ROKTINGLS_b=new ArrayList<Integer>();
	private ArrayList<Integer>ROKTINGLS_c=new ArrayList<Integer>();
	private ArrayList<Integer>ROKTINGLS_d=new ArrayList<Integer>();
	private ArrayList<Integer>POITINS_x=new ArrayList<Integer>();
	private ArrayList<Integer>POITINS_y=new ArrayList<Integer>();
	private ArrayList<Point>POITINS_G=new ArrayList<Point>();
	private Boid mousep;
	private boolean running, displayqtree=false;
	
	public Birdies(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frameW=screenSize.width/2;
		frameH=screenSize.height/2;
		OX=frameW/2;
        OY=frameH/2;
        errmsg="[]";
        T=10;//10,100
        birdNo=100;//1000
        displayqtree=true;
	}
	public void setup(){
		frame=new JFrame("BIRDIES");
		frame.setSize(frameW,frameH);
		frame.setBounds(screenSize.width/2-frameW/2, screenSize.height/2-frameH/2, frameW, frameH);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameicon=Toolkit.getDefaultToolkit().getImage("BIRDIESICON.png");
		frame.setIconImage(frameicon);
		frame.addComponentListener(this);
		frame.addWindowListener(this);
		addKeyListener(this);
		addMouseListener(this);
    	addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
		setLayout(null);
		for(int i=0;i<birdNo;i++){
			birds.add(new Boid(Math.random()*frameW,Math.random()*frameH));
		}
		bounder();
		frame.add(this);
		frame.setVisible(true);
		running=true;
		timer=new Timer(T,this);
    	timer.start();
	}
	public void bounder(){
		;//TODO
	}
	public void paint(Graphics g){
		super.paint(g);
		//TODO posPlacer();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, frameW, frameH);
		
		for(Boid p:birds){
			g.setColor((p==mousep)?Color.GREEN:(p.highlight)?Color.ORANGE:Color.WHITE);
			g.fillOval((int)p.pos.x-(int)p.r,(int)p.pos.y-(int)p.r,(int)p.r*2,(int)p.r*2);
		}
		range=new Region(mouseX,mouseY,25,25,Region.RECTANGLE);
//		g.setColor(Color.RED);
//		for(int i=0;i<POITINS_x.size();i++){
//			g.drawOval(POITINS_x.get(i)-5, POITINS_y.get(i)-5, 10, 10);
//		}
		if(displayqtree)
			drawQtree(g);
		
		g.setColor(Color.GREEN);
		g.drawRect((int)(Math.round(range.x-range.w)), (int)(Math.round(range.y-range.h)), (int)(Math.round(range.w*2)), (int)(Math.round(range.h*2)) );
		for(int i=0;i<POITINS_G.size();i++){
			if(POITINS_G.get(i).data==mousep)
				g.drawOval((int)(Math.round(POITINS_G.get(i).x))-2,(int)(Math.round(POITINS_G.get(i).y))-2,4,4);
		}
		g.drawOval((int)(Math.round(circRange.x-circRange.r)) ,(int)(Math.round(circRange.y-circRange.r)) ,(int)(Math.round(circRange.r*2)) ,(int)(Math.round(circRange.r*2)) );//delete
		
		g.setColor(Color.WHITE);
		if(errmsg.length()>2)
			g.drawString(errmsg, (int)(frameW/2-errmsg.length()*2.5), 65);//error message
		errmsg="[]";
		if(true)//TODO posX_txtfld.hasFocus()||posY_txtfld.hasFocus()||vel_txtfld.hasFocus()||ang_txtfld.hasFocus()||rv_txtfld.hasFocus())
			errmsg="[Press TAB to Register Key clicks]";
		g.dispose();
	}
	public void drawQtree(Graphics g){
		g.setColor(Color.WHITE);
		for(int i=0;i<ROKTINGLS_a.size();i++){
			g.drawRect(ROKTINGLS_a.get(i), ROKTINGLS_b.get(i), ROKTINGLS_c.get(i), ROKTINGLS_d.get(i));
		}
	}
	public void getDemPts(){//delete
		qtree.clearSTUFF();
		qtree.MEshow();
		ROKTINGLS_a.clear();
		ROKTINGLS_b.clear();
		ROKTINGLS_c.clear();
		ROKTINGLS_d.clear();
		POITINS_x.clear();
		POITINS_y.clear();
		POITINS_G.clear();
		for(int i=0;i<QuadTree.a.size();i++){
			ROKTINGLS_a.add((int)(Math.round(QuadTree.a.get(i))));
			ROKTINGLS_b.add((int)(Math.round(QuadTree.b.get(i))));
			ROKTINGLS_c.add((int)(Math.round(QuadTree.c.get(i))));
			ROKTINGLS_d.add((int)(Math.round(QuadTree.d.get(i))));
		}
		for(int i=0;i<QuadTree.XXX.size();i++){
			POITINS_x.add((int)(Math.round(QuadTree.XXX.get(i))));
			POITINS_y.add((int)(Math.round(QuadTree.YYY.get(i))));
		}
		POITINS_G.addAll(Boid.G);
		Boid.G.clear();
	}
	public void calculate(){
		qtree=new QuadTree(new Region(frameW/2,frameH/2,frameW/2,frameH/2,Region.RECTANGLE),4);
		for(Boid p:birds){
			qtree.insert(new Point((int)p.pos.x, (int)p.pos.y ,p));
			p.setHighlight(false);
		}
//		for(Obstacle obs:block){//TODO
//			qtree.insert(new Point((int)p.pos.x, (int)p.pos.y ,p));
//		}
		for (Boid p:birds){
			circRange=new Region((int)p.pos.x,(int)p.pos.y,p.perceptionradius,Region.CIRCLE);
			ArrayList<Point> otherbirds=new ArrayList<Point>();
			otherbirds.addAll(qtree.query(circRange));
			p.edges(frameW,frameH);
			p.flockWith(otherbirds);
			p.update();
			for(Point other:otherbirds){
				if(p!=other.data&&p.intersects(other.data)){
					p.setHighlight(true);
				}
			}
		}
		getDemPts();
	}
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(timer)){
			if(running){
				calculate();
				repaint();
			}
		}
		else
			errmsg="[ActionListener ERROR "+e.getID()+"]";
	}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent e) {
		windowSize = e.getComponent().getBounds().getSize();
        frameW=windowSize.width;
        frameH=windowSize.height;
        OX=frameW/2;
        OY=frameH/2;
        bounder();
	}
	public void componentShown(ComponentEvent arg0) {}
	public void mouseClicked(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
//		if(e.getButton()==MouseEvent.BUTTON1){
			mousep=new Boid(mouseX,mouseY);
			birds.add(mousep);
//		}
//		else if(e.getButton()==MouseEvent.BUTTON2){
//			block.add(new Obstacle(mouseX,mouseY,50,50));
//		}
	}
	public void mouseReleased(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
	}
	public void mouseDragged(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
		//if(e.getButton()==MouseEvent.BUTTON1){
			mousep=new Boid(mouseX,mouseY);
			birds.add(mousep);
		//}
//		else if(e.getButton()==MouseEvent.BUTTON2){
//			if(block.size()>0){
//				block.remove(block.size()-1);
//				block.add(new Obstacle(mouseX,mouseY,50,50));
//			}
//		}
	}
	public void mouseMoved(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
	}
	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent e) {
		running=false;
		timer.stop();
	}
	public void windowClosing(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
}
