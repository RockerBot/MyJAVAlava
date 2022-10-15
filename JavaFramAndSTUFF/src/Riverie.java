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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class Riverie extends JPanel implements ComponentListener,ActionListener,KeyListener{
	private Timer timer;
	private JFrame frame;
	private JTextField posX_txtfld,posY_txtfld,ang_txtfld,vel_txtfld,rv_txtfld;
	private JLabel posX_lbl,posY_lbl,ang_lbl,vel_lbl,rv_lbl;
	private JButton str_btn,rst_btn;
	private Dimension screenSize,windowSize;
	private Image frameicon;
	private ArrayList<Integer> pathX=new ArrayList<Integer>();
    private ArrayList<Integer> pathY=new ArrayList<Integer>();
    
	private int[] wavefronts,boatXs,boatYs;
	private String errmsg;
	private double rV,bX,bY,bV,bA;
	private int frameW,frameH,OX,OY,cam,wavefrontNo=20,X,Y,T=100,timeline,boatSize;
	private boolean simulating;
	
	private Riverie(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameW=650;
        frameH=650;
        timer=new Timer(T,this);
        timer.start();
        errmsg="[]";
        rV=1.0;
        boatXs=new int[5];
        boatYs=new int[5];
        boatSize=5;//5
        timeline=0;//0
        cam=0;//0
        simulating=false;
        wavefronts=new int[wavefrontNo];
	}
	public static void main(String args[]){
		Riverie obj=new Riverie();
		obj.setup();
	}
	private void setup(){
		frame=new JFrame("River PHYSICS");
		frame.setSize(frameW,frameH);
		frame.setBounds(screenSize.width/2-frameW/2, screenSize.height/2-frameH/2, frameW, frameH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameicon=Toolkit.getDefaultToolkit().getImage("RIVERICON.png");
		frame.setIconImage(frameicon);
		frame.addComponentListener(this);
		addKeyListener(this);
		setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setVisible(true);
        setLayout(null);
        setFocusTraversalKeysEnabled(false);
        posX_txtfld=new JTextField();
        posY_txtfld=new JTextField();
        ang_txtfld=new JTextField();
        vel_txtfld=new JTextField();
        rv_txtfld=new JTextField();
        posX_lbl=new JLabel("X");
        posY_lbl=new JLabel("Y");
        vel_lbl=new JLabel("Velocity");
        ang_lbl=new JLabel("Angle");
        rv_lbl=new JLabel("River Velocity");
        str_btn=new JButton("SIMULATE");
        rst_btn=new JButton("RESET");
        str_btn.setBackground(Color.RED);str_btn.setForeground(Color.WHITE);
        posX_lbl.setBackground(Color.BLACK);posX_lbl.setForeground(Color.WHITE);posX_lbl.setOpaque(true);
        posY_lbl.setBackground(Color.BLACK);posY_lbl.setForeground(Color.WHITE);posY_lbl.setOpaque(true);
        vel_lbl.setBackground(Color.BLACK);vel_lbl.setForeground(Color.WHITE);vel_lbl.setOpaque(true);
        ang_lbl.setBackground(Color.BLACK);ang_lbl.setForeground(Color.WHITE);ang_lbl.setOpaque(true);
        rv_lbl.setBackground(Color.BLACK);rv_lbl.setForeground(Color.WHITE);rv_lbl.setOpaque(true);
        posX_txtfld.setText("0");
        posY_txtfld.setText("0");
        vel_txtfld.setText("0");
        ang_txtfld.setText("0");
        rv_txtfld.setText(rV+"");
        posX_txtfld.addActionListener(this);
        posY_txtfld.addActionListener(this);
        vel_txtfld.addActionListener(this);
        ang_txtfld.addActionListener(this);
        rv_txtfld.addActionListener(this);
        str_btn.addActionListener(this);
        rst_btn.addActionListener(this);
        str_btn.setFocusable(false);
        rst_btn.setFocusable(false);
        posX_txtfld.setToolTipText("Boat Horizontal Position");posX_lbl.setToolTipText("Boat Horizontal Position");
        posY_txtfld.setToolTipText("Boat Vertical Position");posY_lbl.setToolTipText("Boat Vertical Position");
        vel_txtfld.setToolTipText("Boat Velocity");vel_lbl.setToolTipText("Boat Velocity");
        ang_txtfld.setToolTipText("Boat Angle");ang_lbl.setToolTipText("Boat Angle");
        rv_txtfld.setToolTipText("River Velocity");rv_lbl.setToolTipText("River Velocity");
        str_btn.setToolTipText("OFF: Does the Simulation");
        rst_btn.setToolTipText("RESETS simulation");
        add(posX_txtfld);add(posX_lbl);
        add(posY_txtfld);add(posY_lbl);
        add(vel_txtfld);add(vel_lbl);
        add(ang_txtfld);add(ang_lbl);
        add(rv_txtfld);add(rv_lbl);
        add(str_btn);add(rst_btn);
		frame.add(this);
		frame.setVisible(true);
		bounder();
	}
	@Override
	public void paint(Graphics g){
		super.paint(g);
		posPlacer();
		g.setColor(Color.BLACK);
		g.fillRect(0, 100, frameW, frameH-220);
		for(int i=0;i<wavefrontNo/2;i++){
			g.setColor(Color.WHITE);
			g.fillRect((frameW/wavefrontNo)*i*2, OY-220, (frameW/wavefrontNo), 20);
			g.setColor(Color.YELLOW);
			g.fillRect((frameW/wavefrontNo)*i*2+frameW/wavefrontNo, OY-220, (frameW/wavefrontNo), 20);
		}
		g.setColor(Color.BLUE);
		g.fillRect(0, OY-200, frameW, 400);//river
		g.setColor(Color.CYAN);
		for(int wavefront:wavefronts)//wavefronts
			g.drawLine(wavefront, OY-200, wavefront, OY+200);
		pather(g);
		g.setColor(Color.ORANGE);
		g.fillPolygon(boatXs, boatYs, 5);//boat
		g.setColor(Color.WHITE);
		g.drawLine(X,Y,X+(int)(bV*Math.cos(bA)*10),Y+(int)(bV*Math.sin(bA)*10));//boat vector
		g.drawLine(X, Y, X+(int)rV*10, Y);//river vector
		g.setColor(Color.RED);
		g.drawLine(X,Y,X+(int)(bV*Math.cos(bA)+rV)*10,Y+(int)(bV*Math.sin(bA)*10));//resolved vector
		g.drawOval(X,Y,1,1);//boat center
		
		g.setColor(Color.WHITE);
		if(errmsg.length()>2)
			g.drawString(errmsg, (int)(frameW/2-errmsg.length()*2.5), 65);//error message
		errmsg="[]";
		if(posX_txtfld.hasFocus()||posY_txtfld.hasFocus()||vel_txtfld.hasFocus()||ang_txtfld.hasFocus()||rv_txtfld.hasFocus())
			errmsg="[Press TAB to Register Key clicks]";
		g.setColor(Color.WHITE);
		g.drawString(timeline+"", OX+400, 60);
		g.dispose();
	}
	private void bounder(){
		for(int i=0;i<wavefrontNo;i++)
			wavefronts[i]=(frameW/wavefrontNo)*i;
		str_btn.setBounds(frameW/2-105,10,100,40);
		rst_btn.setBounds(frameW/2+5,10,100,40);
		posX_lbl.setBounds(frameW/2-270, frameH-100, 100, 30);
        posY_lbl.setBounds(frameW/2-160, frameH-100, 100, 30);
        vel_lbl.setBounds(frameW/2-50, frameH-100, 100, 30);
        ang_lbl.setBounds(frameW/2+60, frameH-100, 100, 30);
        rv_lbl.setBounds(frameW/2+170, frameH-100, 100, 30);
		posX_txtfld.setBounds(frameW/2-270, frameH-70, 100, 30);
        posY_txtfld.setBounds(frameW/2-160, frameH-70, 100, 30);
        vel_txtfld.setBounds(frameW/2-50, frameH-70, 100, 30);
        ang_txtfld.setBounds(frameW/2+60, frameH-70, 100, 30);
        rv_txtfld.setBounds(frameW/2+170, frameH-70, 100, 30);
	}
	private void posPlacer(){
		X=OX+cam+(int)Math.round(bX);
		Y=OY+200+(int)Math.round(bY);
		boatXs[0]=(int)(X-boatSize*Math.sqrt(2)*Math.cos(bA*(-1)-Math.PI/4));
		boatXs[1]=(int)(X-boatSize*Math.sqrt(2)*Math.cos(bA*(-1)-3*Math.PI/4));
		boatXs[2]=(int)(X+boatSize*2*Math.cos(bA*(-1)));
		boatXs[3]=(int)(X+boatSize*Math.sqrt(2)*Math.cos(bA*(-1)-Math.PI/4));
		boatXs[4]=(int)(X+boatSize*Math.sqrt(2)*Math.cos(bA*(-1)-3*Math.PI/4));
		boatYs[0]=(int)(Y+boatSize*Math.sqrt(2)*Math.sin(bA*(-1)-Math.PI/4));
		boatYs[1]=(int)(Y+boatSize*Math.sqrt(2)*Math.sin(bA*(-1)-3*Math.PI/4));
		boatYs[2]=(int)(Y-boatSize*2*Math.sin(bA*(-1)));
		boatYs[3]=(int)(Y-boatSize*Math.sqrt(2)*Math.sin(bA*(-1)-+Math.PI/4));
		boatYs[4]=(int)(Y-boatSize*Math.sqrt(2)*Math.sin(bA*(-1)-3*Math.PI/4));
		pathX.add(X);
		pathY.add(Y);
		if(Y<=OY-200){
			simulating=false;
			str_btn.setBackground(Color.RED);
		}
	}
	private void pather(Graphics g){
		g.setColor(Color.RED);
		for(int i=0;i<pathX.size();i++)
			g.drawOval(pathX.get(i), pathY.get(i), 1, 1);
	}
	private void calculate(){
		double T=1.0;
		for(int i=0;i<wavefrontNo;i++){
			wavefronts[i]+=rV*T;
			wavefronts[i]=(wavefronts[i]>=frameW)?1:wavefronts[i];
			wavefronts[i]=(wavefronts[i]<=0)?frameW-1:wavefronts[i];
		}
		if(simulating){
			velocityResolver(T);
			timeline++;
		}
	}
	private void velocityResolver(double T){
		bX+=(bV*Math.cos(bA)+rV)*T;
		bY+=bV*Math.sin(bA)*T;
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
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(timer)){
    		calculate();
    		repaint();
		}else if(e.getSource().equals(posX_txtfld)&&!simulating){
			bX=Double.parseDouble(posX_txtfld.getText());
		}else if(e.getSource().equals(posY_txtfld)&&!simulating){
			bY=Double.parseDouble(posY_txtfld.getText());
		}else if(e.getSource().equals(vel_txtfld)&&!simulating){
			bV=Double.parseDouble(vel_txtfld.getText());
		}else if(e.getSource().equals(ang_txtfld)&&!simulating){
			bA=Math.toRadians(Double.parseDouble(ang_txtfld.getText()))*(-1);
		}else if(e.getSource().equals(rv_txtfld)&&!simulating){
			rV=Double.parseDouble(rv_txtfld.getText());
		}else if(e.getSource().equals(str_btn)){
			simulating=!simulating;
			str_btn.setBackground((simulating)?Color.GREEN:Color.RED);
			str_btn.setToolTipText(((simulating)?"ON":"OFF")+": Does the Simulation");
		}else if(e.getSource().equals(rst_btn)){
			simulating=false;
			str_btn.setBackground(Color.RED);
			timeline=0;
			bX=0;
			bY=0;
		}else
			errmsg="[ActionListener ERROR "+e.getID()+"]";
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			cam-=10;
		}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			cam+=10;
		}else
			errmsg="[keyPressed ERROR "+e.getID()+"]";
	}
	@Override
	public void keyReleased(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
