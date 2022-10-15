package orbit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Orbitaling extends JPanel implements ActionListener,ComponentListener,KeyListener,MouseListener,MouseMotionListener,MouseWheelListener,ChangeListener {
	private Timer timer;
    private static Dimension windowSize, screenSize;
    private JButton strt_btn,ms_btn,pth_btn,ctr_btn,exp_btn,imp_btn,rst_btn,hud_btn,trngl_btn,psln_btn;
    private JSlider Am_sldr,Bm_sldr,Cm_sldr,trln_sldr;
    private JFileChooser flCh;
    private JLabel zm_lbl;
    private JTextField zm_txtfld;
    
    protected double data[][][]=new double[3][3][2];//A,B,C  Mass,R, X,Y Vx,Vy
    private ArrayList<Integer> pathX=new ArrayList<Integer>();
    private ArrayList<Integer> pathY=new ArrayList<Integer>();
    private ArrayList<String> imprtdS=new ArrayList<String>();
    
    private static boolean objPath=false,strt=true,centergrav=false,msgrav=false,msgravActive=false,hud=true,debugC=false,debugL=false;
    private int R,Xpos,Ypos;
    protected int frameW,frameH;
    private double M,X,Y,Vx,Vy,Ax,Ay,Fx,Fy,theta1,theta2,thetaC,thetaM;
    private String name;
    private static String errmsg;
    
    private static int mouseX,mouseY,Ox,Oy,trailLen,camX,camY;
    private static double zoom,msfactor;
    
    private final int T=10;//10,100
    private final double CENTRE_MASS=1e5,G=6.67e-5;//-11+3+3=-5(10^3m=1km)(10^3kg)
    
    private Orbitaling A,B,C;
    
    public Orbitaling(){
    	addMouseListener(this);
    	addMouseMotionListener(this);
    	addMouseWheelListener(this);
    	addComponentListener(this);
    	addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        frameW=660;//500+160
        frameH=400;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        camX=0;
    	camY=0;
        Ox=frameW/2+camX;
        Oy=frameH/2+camY;
    	M=20.0;
    	R=10;
    	X=100.0;
    	Y=100.0;
    	msfactor=0.001;
    	trailLen=2000;
    	zoom=1.0;
    	errmsg="[]";
    	timer=new Timer(T,this);
    	timer.start();
    }
	public Orbitaling(short type,double m,int r,double x,double y,double dta[][][]){
		this.M=(int)dta[type][0][0];
		this.R=(int)dta[type][0][1];
		this.X=(int)dta[type][1][0];
		this.Y=(int)dta[type][1][1];
		this.Vx=dta[type][2][0];
		this.Vy=dta[type][2][1];
		this.M+=(this.M==0)?m:0;
		this.R+=(this.R==0)?r:0;
		this.name=(type==0)?"A":(type==1)?"B":"C";
	}
	
	public void assignABC(){
    	A=new Orbitaling((short)0,M,R,X,Y,data);
    	B=new Orbitaling((short)1,M,R,X,Y,data);
    	C=new Orbitaling((short)2,M,R,X,Y,data);
	}
	public void setupOrbitaling(){
		ctr_btn=new JButton("BLackHole At centre");
		ms_btn=new JButton("Mouse");
		pth_btn=new JButton("PATH");
		strt_btn=new JButton("STOP");
		exp_btn=new JButton("EXPORT");
		imp_btn=new JButton("IMPORT");
		rst_btn=new JButton("RESET");
		hud_btn=new JButton("HUD");
		trngl_btn=new JButton(Character.toString((char)916));
		psln_btn=new JButton("+");
		int n=(int)Math.max(Math.max(A.M,B.M),C.M)*2;
		Am_sldr=new JSlider(JSlider.VERTICAL,0,n,(int)A.M);
		Bm_sldr=new JSlider(JSlider.VERTICAL,0,n,(int)B.M);
		Cm_sldr=new JSlider(JSlider.VERTICAL,0,n,(int)C.M);
		trln_sldr=new JSlider(JSlider.VERTICAL,0,10000,2000);
		flCh=new JFileChooser(".\\");
		zm_lbl=new JLabel("ZOOM: %");
		zm_txtfld=new JTextField(""+zoom*100);
		Am_sldr.setMinorTickSpacing(n/100);Am_sldr.setMajorTickSpacing(n/10);
		Bm_sldr.setMinorTickSpacing(n/100);Bm_sldr.setMajorTickSpacing(n/10);
		Cm_sldr.setMinorTickSpacing(n/100);Cm_sldr.setMajorTickSpacing(n/10);
		trln_sldr.setMinorTickSpacing(100);trln_sldr.setMajorTickSpacing(1000);
		Am_sldr.setPaintTicks(true);Am_sldr.setPaintLabels(true);
		Bm_sldr.setPaintTicks(true);Bm_sldr.setPaintLabels(true);
		Cm_sldr.setPaintTicks(true);Cm_sldr.setPaintLabels(true);
		trln_sldr.setPaintTicks(true);trln_sldr.setPaintLabels(true);
		ctr_btn.setBackground(Color.RED);ctr_btn.setForeground(Color.WHITE);
		ms_btn.setBackground(Color.RED);ms_btn.setForeground(Color.WHITE);
		pth_btn.setBackground(Color.RED);pth_btn.setForeground(Color.WHITE);
		strt_btn.setBackground(Color.GREEN);strt_btn.setForeground(Color.WHITE);
		hud_btn.setBackground(Color.GREEN);hud_btn.setForeground(Color.WHITE);
		trngl_btn.setBackground(Color.RED);trngl_btn.setForeground(Color.WHITE);
		psln_btn.setBackground(Color.RED);psln_btn.setForeground(Color.WHITE);
		Am_sldr.setBackground(Color.RED);Am_sldr.setForeground(Color.WHITE);
		Bm_sldr.setBackground(Color.GREEN);Bm_sldr.setForeground(Color.WHITE);
		Cm_sldr.setBackground(Color.BLUE);Cm_sldr.setForeground(Color.WHITE);
		trln_sldr.setBackground(Color.BLACK);trln_sldr.setForeground(Color.WHITE);
		zm_lbl.setBackground(Color.BLACK);zm_lbl.setForeground(Color.WHITE);
		zm_txtfld.setBackground(Color.BLACK);zm_txtfld.setForeground(Color.WHITE);
		ctr_btn.addActionListener(this);ms_btn.addActionListener(this);
		pth_btn.addActionListener(this);strt_btn.addActionListener(this);
		exp_btn.addActionListener(this);imp_btn.addActionListener(this);
		rst_btn.addActionListener(this);hud_btn.addActionListener(this);
		trngl_btn.addActionListener(this);psln_btn.addActionListener(this);
		Am_sldr.addChangeListener(this);Bm_sldr.addChangeListener(this);
		Cm_sldr.addChangeListener(this);trln_sldr.addChangeListener(this);
		zm_txtfld.addActionListener(this);
		ctr_btn.setFocusable(false);ms_btn.setFocusable(false);
		pth_btn.setFocusable(false);strt_btn.setFocusable(false);
		exp_btn.setFocusable(false);imp_btn.setFocusable(false);
		rst_btn.setFocusable(false);hud_btn.setFocusable(false);
		trngl_btn.setFocusable(false);psln_btn.setFocusable(false);
		ctr_btn.setToolTipText("OFF: Applys a Force Pulling objects to the centre");
		ms_btn.setToolTipText("OFF: Applys a Force Pulling objects to your mouse");
		strt_btn.setToolTipText("ON: Pauses/Unpauses the Render");
		pth_btn.setToolTipText("OFF: Displays the paths traversed by objects");
		rst_btn.setToolTipText("RESETS the Render");
		hud_btn.setToolTipText("ON: Heads-Up-Display");
		trngl_btn.setToolTipText("OFF: Joins centers of objects");
		psln_btn.setToolTipText("OFF: Draws a line at the co-ordinates of objects");
		imp_btn.setToolTipText("IMPORTS Object values from a file into the Render");
		exp_btn.setToolTipText("EXPORTS Object values from the Render into a file");
		Am_sldr.setToolTipText("Change the mass of the Red object");
		Bm_sldr.setToolTipText("Change the mass of the Green object");
		Cm_sldr.setToolTipText("Change the mass of the Blue object");
		trln_sldr.setToolTipText("Change trail-path length");
		zm_lbl.setToolTipText("Magnification");
		zm_txtfld.setToolTipText("Hit TAB to register");
		zm_lbl.setOpaque(true);
		bounderOrbitaling();
		add(ctr_btn);add(ms_btn);add(pth_btn);add(strt_btn);add(hud_btn);
		add(exp_btn);add(imp_btn);add(rst_btn);add(trngl_btn);add(psln_btn);
		add(Am_sldr);add(Bm_sldr);add(Cm_sldr);add(trln_sldr);
		add(zm_lbl);add(zm_txtfld);
	}
	public void bounderOrbitaling(){
		imp_btn.setBounds(frameW-210, frameH-35, 140, 35);
		exp_btn.setBounds(frameW-210, frameH-70, 140, 35);
		hud_btn.setBounds(frameW-210, frameH-140, 70, 70);
		trngl_btn.setBounds(frameW-140, frameH-105, 70, 35);
		psln_btn.setBounds(frameW-140, frameH-140, 70, 35);
		zm_lbl.setBounds(frameW-210, frameH-170, 50, 30);
		zm_txtfld.setBounds(frameW-160, frameH-170, 90, 30);
		ctr_btn.setBounds(frameW-210, frameH-240, 70, 70);
		ms_btn.setBounds(frameW-140, frameH-240, 70, 70);
		pth_btn.setBounds(frameW-210, frameH-300, 140, 60);
		strt_btn.setBounds(frameW-210, frameH-360, 140, 60);
		rst_btn.setBounds(frameW-210, frameH-420, 140, 60);
		Am_sldr.setBounds(frameW-210, 0,70, frameH-420);
		Bm_sldr.setBounds(frameW-140, 0, 70, frameH-420);
		Cm_sldr.setBounds(frameW-70, 0, 70, frameH-420);
		trln_sldr.setBounds(frameW-70, frameH-420, 70, 420);
	}
	public void FBD(Orbitaling O1,Orbitaling O2){
		double dx,dy,F1,F2,Fc,Fm,T=1.0;
		dx=Math.abs(this.X-O1.X);
		dy=Math.abs(this.Y-O1.Y);
		this.theta1=Math.atan2(dy,dx);
		this.theta1*=(O1.Y>this.Y)?1:(-1);
		this.theta1=(O1.X>this.X)?theta1:Math.PI-theta1;
		F1=(G*this.M*O1.M)/((dx*dx)+(dy*dy));
		dx=Math.abs(this.X-O2.X);
		dy=Math.abs(this.Y-O2.Y);
		this.theta2=Math.atan2(dy,dx);
		this.theta2*=(O2.Y>this.Y)?1:(-1);
		this.theta2=(O2.X>this.X)?theta2:Math.PI-theta2;
		F2=(G*this.M*O2.M)/((dx*dx)+(dy*dy));
		this.thetaC=Math.atan2(0.0-this.Y, 0.0-this.X);
		Fc=(centergrav)?((G*this.M*CENTRE_MASS)/(Math.sqrt(Math.pow(this.X, 2)+Math.pow(this.Y, 2)))):0.0;//imaginary force pulling to the center of the screen
		this.thetaM=Math.atan2((mouseY-Oy)/zoom-this.Y, (mouseX-Ox)/zoom-this.X);
		Fm=(msgravActive)?((G*this.M*CENTRE_MASS*msfactor)/(Math.sqrt(Math.pow(this.X-(mouseX-Ox)/zoom, 2)+Math.pow(this.Y-(mouseY-Oy)/zoom, 2)))):0.0;//imaginary force pulling to the mouse position
		msfactor+=(msgravActive&&msfactor<10.0)?0.001:0.0;
		this.Fx=F1*Math.cos(this.theta1)+F2*Math.cos(this.theta2)+Fc*Math.cos(this.thetaC)+Fm*Math.cos(this.thetaM);
		this.Fy=F1*Math.sin(this.theta1)+F2*Math.sin(this.theta2)+Fc*Math.sin(this.thetaC)+Fm*Math.sin(this.thetaM);
		this.Ax=this.Fx/this.M;
		this.Ay=this.Fy/this.M;
		this.Vx+=this.Ax*T;
		this.Vy+=this.Ay*T;
		this.X+=this.Vx*T;
		this.Y+=this.Vy*T;
	}
	@Override
	public void paint(Graphics g){
		posPlacer(A,B,C);
		super.paint(g);
		bounderOrbitaling();
		g.setColor(Color.BLACK);
        g.fillRect(0, 0, frameW-210, frameH);
		if(objPath){
			A.pather(g);
			B.pather(g);
			C.pather(g);
		}
		g.setColor(Color.RED);
		g.fillOval(Ox+(int)((A.Xpos-A.R)*zoom), Oy+(int)((A.Ypos-A.R)*zoom), (int)(2*A.R*zoom),(int)(2*A.R*zoom));//red mass
		g.setColor(Color.GREEN);
		g.fillOval(Ox+(int)((B.Xpos-B.R)*zoom), Oy+(int)((B.Ypos-B.R)*zoom), (int)(2*B.R*zoom), (int)(2*B.R*zoom));//green mass
		g.setColor(Color.BLUE);
		g.fillOval(Ox+(int)((C.Xpos-C.R)*zoom), Oy+(int)((C.Ypos-C.R)*zoom), (int)(2*C.R*zoom), (int)(2*C.R*zoom));//blue mass
		g.setColor(Color.WHITE);//velocity of A,B,C
		g.drawLine(Ox+(int)(A.Xpos*zoom), Oy+(int)(A.Ypos*zoom), Ox+(int)((A.Xpos+A.Vx*100)*zoom), Oy+(int)((A.Ypos+A.Vy*100)*zoom) );
		g.drawLine(Ox+(int)(B.Xpos*zoom), Oy+(int)(B.Ypos*zoom), Ox+(int)((B.Xpos+B.Vx*100)*zoom), Oy+(int)((B.Ypos+B.Vy*100)*zoom) );
		g.drawLine(Ox+(int)(C.Xpos*zoom), Oy+(int)(C.Ypos*zoom), Ox+(int)((C.Xpos+C.Vx*100)*zoom), Oy+(int)((C.Ypos+C.Vy*100)*zoom) );
		if(hud)
			HUD(g);
		g.setColor(Color.WHITE);
		g.drawOval(Ox+(int)(A.Xpos*zoom), Oy+(int)(A.Ypos*zoom), 1, 1);//center of red mass
		g.drawOval(Ox+(int)(B.Xpos*zoom), Oy+(int)(B.Ypos*zoom), 1, 1);//center of green mass
		g.drawOval(Ox+(int)(C.Xpos*zoom), Oy+(int)(C.Ypos*zoom), 1, 1);//center of blue mass
		g.fillOval(Ox-2, Oy-2, 4, 4);//center of world
		if(errmsg.length()>2)
			g.drawString(errmsg, frameW/2-errmsg.length()/2, 50);
		errmsg="[]";
		if(zm_txtfld.hasFocus())
			errmsg="[Press TAB to Register Key clicks]";
		g.dispose();
	}
	public void pather(Graphics g){
		g.setColor((this.name.equals("A"))?Color.RED:(this.name.equals("B"))?Color.GREEN:Color.BLUE);
		for(int i=0;i<(int)Math.min(this.pathX.size(),trailLen);i++)
			g.drawOval(Ox+(int)(this.pathX.get(pathX.size()-i-1)*zoom),Oy+(int)(this.pathY.get(pathY.size()-i-1)*zoom),1,1);
	}
	public void HUD(Graphics g){
		g.setColor(Color.RED);
		g.drawOval(20, 20, 50, 50);//red dial
		g.drawOval(45, 45, 1, 1);//center of red dial
		g.drawLine(105, 45, (int)(105+Math.cos(B.theta1)*25), (int)(45+Math.sin(B.theta1)*25));//red angle of green dial
		g.drawLine(45, 105, (int)(45+Math.cos(C.theta1)*25), (int)(105+Math.sin(C.theta1)*25));//red angle of blue dial
		g.drawLine(105, 105, (int)(105+Math.cos(Math.PI+A.thetaC)*25), (int)(105+Math.sin(Math.PI+A.thetaC)*25));//red angle of World dial
		g.drawLine(mouseX, mouseY, (int)(mouseX+Math.cos(Math.PI+A.thetaM)*25), (int)(mouseY+Math.sin(Math.PI+A.thetaM)*25));//red angle of mouse dial
		g.setColor(Color.GREEN);
		g.drawOval(80, 20, 50, 50);//green dial
		g.drawOval(105, 45, 1, 1);//center of green dial
		g.drawLine(45, 45, (int)(45+Math.cos(A.theta1)*25), (int)(45+Math.sin(A.theta1)*25));//green angle of red dial
		g.drawLine(45, 105, (int)(45+Math.cos(C.theta2)*25), (int)(105+Math.sin(C.theta2)*25));//green angle of blue dial
		g.drawLine(105, 105, (int)(105+Math.cos(Math.PI+B.thetaC)*25), (int)(105+Math.sin(Math.PI+B.thetaC)*25));//green angle of World dial
		g.drawLine(mouseX, mouseY, (int)(mouseX+Math.cos(Math.PI+B.thetaM)*25), (int)(mouseY+Math.sin(Math.PI+B.thetaM)*25));//green angle of mouse dial
		g.setColor(Color.BLUE);
		g.drawOval(20, 80, 50, 50);//blue dial
		g.drawOval(45, 105, 1, 1);//center of blue dial
		g.drawLine(45, 45, (int)(45+Math.cos(A.theta2)*25), (int)(45+Math.sin(A.theta2)*25));//blue angle of red dial
		g.drawLine(105, 45, (int)(105+Math.cos(B.theta2)*25), (int)(45+Math.sin(B.theta2)*25));//blue angle of green dial
		g.drawLine(105, 105, (int)(105+Math.cos(Math.PI+C.thetaC)*25), (int)(105+Math.sin(Math.PI+C.thetaC)*25));//blue angle of World dial
		g.drawLine(mouseX, mouseY, (int)(mouseX+Math.cos(Math.PI+C.thetaM)*25), (int)(mouseY+Math.sin(Math.PI+C.thetaM)*25));//blue angle of mouse dial
		g.setColor(Color.WHITE);
		g.drawOval(80, 80, 50, 50);//World dial
		g.drawOval(105, 105, 1, 1);//center of World dial
		g.drawLine(45, 45, (int)(45+Math.cos(A.thetaC)*25), (int)(45+Math.sin(A.thetaC)*25));//world angle of red dial
		g.drawLine(105, 45, (int)(105+Math.cos(B.thetaC)*25), (int)(45+Math.sin(B.thetaC)*25));//world angle of green dial
		g.drawLine(45, 105, (int)(45+Math.cos(C.thetaC)*25), (int)(105+Math.sin(C.thetaC)*25));//world angle of blue dial
		g.drawLine(mouseX, mouseY, (int)(mouseX+Math.cos(Math.atan2(Oy-mouseY, Ox-mouseX))*25), (int)(mouseY+Math.sin(Math.atan2(Oy-mouseY, Ox-mouseX))*25));//world angle of mouse dial
		g.setColor(Color.ORANGE);
		g.drawOval((int)(mouseX-25*(msfactor+1)), (int)(mouseY-25*(msfactor+1)), (int)(50*(msfactor+1)), (int)(50*(msfactor+1)));//mouse dial
		g.drawLine(45, 45, (int)(45+Math.cos(A.thetaM)*25), (int)(45+Math.sin(A.thetaM)*25));//mouse angle of red dial
		g.drawLine(105, 45, (int)(105+Math.cos(B.thetaM)*25), (int)(45+Math.sin(B.thetaM)*25));//mouse angle of green dial
		g.drawLine(45, 105, (int)(45+Math.cos(C.thetaM)*25), (int)(105+Math.sin(C.thetaM)*25));//mouse angle of blue dial
		g.drawLine(105, 105, (int)(105+Math.cos(Math.atan2(mouseY-Oy, mouseX-Ox))*25), (int)(105+Math.sin(Math.atan2(mouseY-Oy, mouseX-Ox))*25));//mouse angle of world dial
		if(debugC){//joins centers
			double dist=Math.sqrt(Math.pow((A.X-B.X)*zoom, 2)+Math.pow((A.Y-B.Y)*zoom, 2));
			g.drawLine(Ox+(int)(A.Xpos*zoom),Oy+(int)(A.Ypos*zoom),(int)(Ox+A.Xpos*zoom+dist*Math.cos(A.theta1)),(int)(Oy+A.Ypos*zoom+dist*Math.sin(A.theta1)));//joins A,B
			dist=Math.sqrt(Math.pow((A.X-C.X)*zoom, 2)+Math.pow((A.Y-C.Y)*zoom, 2));
			g.drawLine(Ox+(int)(A.Xpos*zoom),Oy+(int)(A.Ypos*zoom),(int)(Ox+A.Xpos*zoom+dist*Math.cos(A.theta2)),(int)(Oy+A.Ypos*zoom+dist*Math.sin(A.theta2)));//joins A,C
			dist=Math.sqrt(Math.pow((B.X-C.X)*zoom, 2)+Math.pow((B.Y-C.Y)*zoom, 2));
			g.drawLine(Ox+(int)(B.Xpos*zoom),Oy+(int)(B.Ypos*zoom),(int)(Ox+B.Xpos*zoom+dist*Math.cos(B.theta2)),(int)(Oy+B.Ypos*zoom+dist*Math.sin(B.theta2)));//joins B,C
		}
		if(debugL){// H and V Lines
			g.setColor(Color.RED);
			g.drawLine(0, (Oy+A.Ypos*zoom<frameH)?((Oy+A.Ypos*zoom>0)?(int)(Oy+A.Ypos*zoom):1):(frameH-1), frameW, (Oy+A.Ypos*zoom<frameH)?((Oy+A.Ypos*zoom>0)?(int)(Oy+A.Ypos*zoom):1):(frameH-1));//red H line
			g.drawLine((Ox+A.Xpos*zoom<frameW)?((Ox+A.Xpos*zoom>0)?(int)(Ox+A.Xpos*zoom):1):(frameW-1), 0, (Ox+A.Xpos*zoom<frameW)?((Ox+A.Xpos*zoom>0)?(int)(Ox+A.Xpos*zoom):1):(frameW-1), frameH);//red V line
			g.setColor(Color.GREEN);
			g.drawLine(0, (Oy+B.Ypos*zoom<frameH)?((Oy+B.Ypos*zoom>0)?(int)(Oy+B.Ypos*zoom):1):(frameH-1), frameW, (Oy+B.Ypos*zoom<frameH)?((Oy+B.Ypos*zoom>0)?(int)(Oy+B.Ypos*zoom):1):(frameH-1));//green H line
			g.drawLine((Ox+B.Xpos*zoom<frameW)?((Ox+B.Xpos*zoom>0)?(int)(Ox+B.Xpos*zoom):1):(frameW-1), 0, (Ox+B.Xpos*zoom<frameW)?((Ox+B.Xpos*zoom>0)?(int)(Ox+B.Xpos*zoom):1):(frameW-1), frameH);//green V line
			g.setColor(Color.BLUE);
			g.drawLine(0, (Oy+C.Ypos*zoom<frameH)?((Oy+C.Ypos*zoom>0)?(int)(Oy+C.Ypos*zoom):1):(frameH-1), frameW, (Oy+C.Ypos*zoom<frameH)?((Oy+C.Ypos*zoom>0)?(int)(Oy+C.Ypos*zoom):1):(frameH-1));//blue H line
			g.drawLine((Ox+C.Xpos*zoom<frameW)?((Ox+C.Xpos*zoom>0)?(int)(Ox+C.Xpos*zoom):1):(frameW-1), 0, (Ox+C.Xpos*zoom<frameW)?((Ox+C.Xpos*zoom>0)?(int)(Ox+C.Xpos*zoom):1):(frameW-1), frameH);//blue V line
		}
		g.drawString("ZOOM: "+zoom*100+"%", frameW/2, 20);
	}
	public void calculate(){
		A.FBD(B,C);
		B.FBD(A,C);
		C.FBD(A,B);
		collision();
		minCheck(A,B,C);
	}
	public void minCheck(Orbitaling O1,Orbitaling O2,Orbitaling O3){
		String order="";
		if(Math.min(O1.M,O2.M)>O3.M)
			order+=O3.name+((O2.M<O1.M)?O2.name:O1.name);
		else if(O2.M<O1.M)
			order+=O2.name+((O3.M<O1.M)?O3.name:O1.name);
		else
			order+=O1.name+((O3.M<O2.M)?O3.name:O2.name);
		if(order.substring(0,1).compareTo("A")==0){
			A.pushback(B);
			A.pushback(C);
			if(order.substring(1).compareTo("B")==0){
				B.pushback(A);
				B.pushback(C);
				C.pushback(A);
				C.pushback(B);
			}else{
				C.pushback(A);
				C.pushback(B);
				B.pushback(A);
				B.pushback(C);
			}
		}else if(order.substring(0,1).compareTo("B")==0){
			B.pushback(A);
			B.pushback(C);
			if(order.substring(1).compareTo("A")==0){
				A.pushback(B);
				A.pushback(C);
				C.pushback(A);
				C.pushback(B);
			}else{
				C.pushback(A);
				C.pushback(B);
				A.pushback(B);
				A.pushback(C);
			}
		}else{
			C.pushback(A);
			C.pushback(B);
			B.pushback(A);
			B.pushback(C);
			A.pushback(B);
			A.pushback(C);
		}
	}
	public void pushback(Orbitaling O){
		if((Math.pow(this.X-O.X,2)+Math.pow(this.Y-O.Y,2)<Math.pow(O.R+(this.R/2),2))){
			this.X-=Math.cos(Math.atan2(O.Y-this.Y,O.X-this.X));
			this.Y-=Math.sin(Math.atan2(O.Y-this.Y,O.X-this.X));
		}
	}
	public void collision(){
		double Ue1,Ue2,Va1,Va2,Ve;//totM,fracM;
		if(Math.pow(A.X-B.X,2)+Math.pow(A.Y-B.Y,2)<Math.pow(A.R+B.R,2)){//A,B
			Ue1=A.Vx*Math.cos(A.theta1)+A.Vy*Math.sin(A.theta1);
			Va1=A.Vx*Math.sin(A.theta1)+A.Vy*Math.cos(A.theta1);
			Ue2=B.Vx*Math.cos(B.theta1)+B.Vy*Math.sin(B.theta1);
			Va2=B.Vx*Math.sin(B.theta1)+B.Vy*Math.cos(B.theta1);
			Ve=(A.M*Ue1+B.M*Ue2)/(A.M+B.M);
			Ve/=1000;
//			totM=A.M+B.M;
//			fracM=(A.M-B.M)/totM;			ELASTIC
//			Ve1=(fracM*Ue1)+(2*B.M*Ue2)/totM;
//			Ve2=(fracM*Ue2*(-1.0))+(2*A.M*Ue1)/totM;
			A.Vx=(Ve/A.M)*Math.cos(A.theta1)+Va1*Math.sin(A.theta1);
			A.Vy=(Ve/A.M)*Math.sin(A.theta1)+Va1*Math.cos(A.theta1);
			B.Vx=(Ve/B.M)*Math.cos(B.theta1)+Va2*Math.sin(B.theta1);
			B.Vy=(Ve/B.M)*Math.sin(B.theta1)+Va2*Math.cos(B.theta1);
		}
		if(Math.pow(A.X-C.X,2)+Math.pow(A.Y-C.Y,2)<Math.pow(A.R+C.R,2)){//A,C
			Ue1=A.Vx*Math.cos(A.theta2)+A.Vy*Math.sin(A.theta2);
			Va1=A.Vx*Math.sin(A.theta2)+A.Vy*Math.cos(A.theta2);
			Ue2=C.Vx*Math.cos(C.theta1)+C.Vy*Math.sin(C.theta1);
			Va2=C.Vx*Math.sin(C.theta1)+C.Vy*Math.cos(C.theta1);
			Ve=(A.M*Ue1+C.M*Ue2)/(A.M+C.M);
			Ve/=1000;
//			totM=A.M+C.M;
//			fracM=(A.M-C.M)/totM;			ELASTIC
//			Ve1=(fracM*Ue1)+(2*C.M*Ue2)/totM;
//			Ve2=(fracM*Ue2*(-1.0))+(2*A.M*Ue1)/totM;
			A.Vx=(Ve/A.M)*Math.cos(A.theta2)+Va1*Math.sin(A.theta2);
			A.Vy=(Ve/A.M)*Math.sin(A.theta2)+Va1*Math.cos(A.theta2);
			C.Vx=(Ve/C.M)*Math.cos(C.theta1)+Va2*Math.sin(C.theta1);
			C.Vy=(Ve/C.M)*Math.sin(C.theta1)+Va2*Math.cos(C.theta1);
		}
		if(Math.pow(B.X-C.X,2)+Math.pow(B.Y-C.Y,2)<Math.pow(B.R+C.R,2)){//B,C
			Ue1=B.Vx*Math.cos(B.theta2)+A.Vy*Math.sin(B.theta2);
			Va1=B.Vx*Math.sin(B.theta2)+A.Vy*Math.cos(B.theta2);
			Ue2=C.Vx*Math.cos(C.theta2)+C.Vy*Math.sin(C.theta2);
			Va2=C.Vx*Math.sin(C.theta2)+C.Vy*Math.cos(C.theta2);
			Ve=(B.M*Ue1+C.M*Ue2)/(B.M+C.M);
			Ve/=1000;
//			totM=B.M+C.M;
//			fracM=(B.M-C.M)/totM;			ELASTIC
//			Ve1=(fracM*Ue1)+(2*C.M*Ue2)/totM;
//			Ve2=(fracM*Ue2*(-1.0))+(2*B.M*Ue1)/totM;
			B.Vx=(Ve/B.M)*Math.cos(B.theta2)+Va1*Math.sin(B.theta2);
			B.Vy=(Ve/B.M)*Math.sin(B.theta2)+Va1*Math.cos(B.theta2);
			C.Vx=(Ve/C.M)*Math.cos(C.theta2)+Va2*Math.sin(C.theta2);
			C.Vy=(Ve/C.M)*Math.sin(C.theta2)+Va2*Math.cos(C.theta2);
		}
	}
	public void posPlacer(Orbitaling O1,Orbitaling O2,Orbitaling O3){
		O1.Xpos=(int)Math.round(O1.X);
		O1.Ypos=(int)Math.round(O1.Y);
		O2.Xpos=(int)Math.round(O2.X);
		O2.Ypos=(int)Math.round(O2.Y);
		O3.Xpos=(int)Math.round(O3.X);
		O3.Ypos=(int)Math.round(O3.Y);
		O1.pathX.add(O1.Xpos);
		O2.pathX.add(O2.Xpos);
		O3.pathX.add(O3.Xpos);
		O1.pathY.add(O1.Ypos);
		O2.pathY.add(O2.Ypos);
		O3.pathY.add(O3.Ypos);
		if(O1.pathX.size()>=10000){
			O1.pathX.remove(0);
			O1.pathY.remove(0);
			O2.pathX.remove(0);
			O2.pathY.remove(0);
			O3.pathX.remove(0);
			O3.pathY.remove(0);
		}
			
	}
	public void storerOrbitaling(){
		boolean flcrtd=false;
		String fname="saves/"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy HH,mm,ss.ns")).substring(0,22)+".txt";
		try {
			File f=new File(fname);
			if(f.createNewFile())
				flcrtd=true;
			else
				errmsg="oops file: "+fname+" exists already, try again now";
		}catch(Exception e){
			errmsg="file creation ERROR ";
			e.printStackTrace();
		}
		if(flcrtd){
			try{
				FileWriter writer=new FileWriter(fname);
				writer.write("$START#\n");
				for(int i=0;i<3;i++){
					writer.write("$DATA"+i+"# ");
					for(int j=0;j<6;j++){
						writer.write(Double.toString(this.data[i][j/2][j%2])+" $SPACE# ");
					}
					writer.write("\n");
				}
				writer.write("$X# "+Double.toString(A.X)+" $SPACE# "+Double.toString(B.X)+" $SPACE# "+Double.toString(C.X)+" $SPACE# \n");
				writer.write("$Y# "+Double.toString(A.Y)+" $SPACE# "+Double.toString(B.Y)+" $SPACE# "+Double.toString(C.Y)+" $SPACE# \n");
				writer.write("$VX# "+Double.toString(A.Vx)+" $SPACE# "+Double.toString(B.Vx)+" $SPACE# "+Double.toString(C.Vx)+" $SPACE# \n");
				writer.write("$VY# "+Double.toString(A.Vy)+" $SPACE# "+Double.toString(B.Vy)+" $SPACE# "+Double.toString(C.Vy)+" $SPACE# \n");
				writer.write("$M# "+Double.toString(A.M)+" $SPACE# "+Double.toString(B.M)+" $SPACE# "+Double.toString(C.M)+" $SPACE# \n");
				
				writer.write("$END#");
				writer.close();
			}catch(Exception e){
				errmsg="[file writing ERROR]";
				e.printStackTrace();
			}
		}
	}
	public void readerOrbitaling(){
		String s="";
		if(flCh.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
			File f=flCh.getSelectedFile();
			try{
				Scanner reader=new Scanner(f);
				while(reader.hasNextLine())
					imprtdS.add(reader.nextLine());
				if(imprtdS.get(0).equals("$START#")&&imprtdS.get(imprtdS.size()-1).equals("$END#")){
					for(int i=0;i<3;i++){
						s=imprtdS.get(i+1);
						s=s.substring(s.indexOf("#")+1);
						for(int j=0;j<6;j++){
							this.data[i][j/2][j%2]=Double.parseDouble(s.substring(0,s.indexOf("$")));
							s=s.substring(s.indexOf("#")+1);
						}
					}
					s=imprtdS.get(4);
					s=s.substring(s.indexOf("#")+1);
					A.X=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					B.X=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					C.X=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					s=imprtdS.get(5);
					s=s.substring(s.indexOf("#")+1);
					A.Y=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					B.Y=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					C.Y=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					s=imprtdS.get(6);
					s=s.substring(s.indexOf("#")+1);
					A.Vx=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					B.Vx=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					C.Vx=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					s=imprtdS.get(7);
					s=s.substring(s.indexOf("#")+1);
					A.Vy=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					B.Vy=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					C.Vy=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					s=imprtdS.get(8);
					s=s.substring(s.indexOf("#")+1);
					A.M=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					B.M=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					C.M=Double.parseDouble(s.substring(0,s.indexOf("$")));
					s=s.substring(s.indexOf("#")+1);
					int n=(int)Math.max(Math.max(A.M,B.M),C.M)*2;
					Am_sldr.setMaximum(n);Bm_sldr.setMaximum(n);Cm_sldr.setMaximum(n);
					Am_sldr.setMajorTickSpacing(n/10);Bm_sldr.setMajorTickSpacing(n/10);Cm_sldr.setMajorTickSpacing(n/10);
					Am_sldr.setMinorTickSpacing(n/100);Bm_sldr.setMinorTickSpacing(n/100);Cm_sldr.setMinorTickSpacing(n/100);
					Am_sldr.setValue((int)A.M);Bm_sldr.setValue((int)B.M);Cm_sldr.setValue((int)C.M);
				}
				reader.close();
			}catch(Exception e){
				errmsg="[FILE ERROR]";
				e.printStackTrace();
			}
		}else
			errmsg="...";
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(timer)){
    		calculate();
    		repaint(0, 0, frameW-210, frameH);
		}else if(e.getSource().equals(ctr_btn)){
    		centergrav=!centergrav;
    		ctr_btn.setBackground((centergrav)?Color.GREEN:Color.RED);
    		ctr_btn.setToolTipText(((centergrav)?"ON":"OFF")+": Applys a Force Pulling objects to the centre");
		}else if(e.getSource().equals(pth_btn)){
			objPath=!objPath;
			pth_btn.setBackground((objPath)?Color.GREEN:Color.RED);
			pth_btn.setToolTipText(((objPath)?"ON":"OFF")+": Displays the paths traversed by objects");
		}else if(e.getSource().equals(strt_btn)){
			if(strt)
				timer.stop();
			else
				timer.start();
			strt=!strt;
			strt_btn.setBackground((strt)?Color.GREEN:Color.RED);
			strt_btn.setText((strt)?"START":"STOP");
			strt_btn.setToolTipText(((strt)?"ON":"OFF")+": Pauses/Unpauses the Render");
		}else if(e.getSource().equals(ms_btn)){
			msgrav=!msgrav;
			msgravActive=(msgrav)?msgravActive:false;
			ms_btn.setBackground((msgrav)?Color.GREEN:Color.RED);
			ms_btn.setToolTipText(((msgrav)?"ON":"OFF")+": Applys a Force Pulling objects to your mouse");
		}else if(e.getSource().equals(rst_btn)){
			A=null;
			B=null;
			C=null;
			assignABC();
			A.M=Am_sldr.getValue();
			B.M=Bm_sldr.getValue();
			C.M=Cm_sldr.getValue();
		}else if(e.getSource().equals(exp_btn)){
			storerOrbitaling();
		}else if(e.getSource().equals(imp_btn)){
			readerOrbitaling();
		}else if(e.getSource().equals(hud_btn)){
			hud=!hud;
			hud_btn.setBackground((hud)?Color.GREEN:Color.RED);
			hud_btn.setToolTipText(((msgrav)?"ON":"OFF")+": Heads-Up-Display");
		}else if(e.getSource().equals(trngl_btn)){
			debugC=!debugC;
			trngl_btn.setBackground((debugC)?Color.GREEN:Color.RED);
			trngl_btn.setToolTipText(((debugC)?"ON":"OFF")+": Joins centers of objects");
		}else if(e.getSource().equals(psln_btn)){
			debugL=!debugL;
			psln_btn.setBackground((debugL)?Color.GREEN:Color.RED);
			psln_btn.setToolTipText(((debugL)?"ON":"OFF")+": Draws a line at the co-ordinates of objects");
		}else if(e.getSource().equals(zm_txtfld)){
    		zoom=Double.parseDouble(zm_txtfld.getText())/100.0;
    	}else
    		errmsg="{ActionListener ERROR "+e.getID()+"]";
	}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent e) {
		windowSize = e.getComponent().getBounds().getSize();
        frameW=windowSize.width;
        frameH=windowSize.height;
        Ox=frameW/2+camX;
        Oy=frameH/2+camY;
	}
	public void componentShown(ComponentEvent e) {}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_UP){
			camY+=10;
	        Oy=frameH/2+camY;
		}else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			camY-=10;
	        Oy=frameH/2+camY;
		}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			camX-=10;
			Ox=frameW/2+camX;
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			camX+=10;
			Ox=frameW/2+camX;
		}else
			errmsg="[keyPressed ERROR "+e.getID()+"]";
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void mouseClicked(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
		msgravActive=msgrav;
	}
	public void mouseReleased(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
		msgravActive=false;
		msfactor=0.001;
	}
	public void mouseDragged(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
	}
	public void mouseMoved(MouseEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
	}
	public void stateChanged(ChangeEvent e) {
		if(e.getSource().equals(Am_sldr)){
			A.M=Am_sldr.getValue();
		}else if(e.getSource().equals(Bm_sldr)){
			B.M=Bm_sldr.getValue();
		}else if(e.getSource().equals(Cm_sldr)){
			C.M=Cm_sldr.getValue();
		}else if(e.getSource().equals(trln_sldr)){
			trailLen=trln_sldr.getValue();
		}else
			errmsg="[ChangeListener ERROR]";
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseX=e.getX();
		mouseY=e.getY();
		zoom+=(e.getWheelRotation()<0)?(0.01):(-0.01);
	}
}