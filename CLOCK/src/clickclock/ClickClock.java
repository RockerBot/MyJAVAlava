package clickclock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ClickClock extends JPanel implements ComponentListener,ActionListener{
	private Timer timer;
	private JFrame frame;
	private Dimension screenSize,windowSize;
	private Image frameicon;
	private String errmsg;
	private double hTheta,mTheta,sTheta,hlen,mlen,slen;
	private int frameW,frameH,OX,OY,radius,hours,minutes,seconds,T=100;
	
	public ClickClock(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameW=450;
        frameH=250;
        timer=new Timer(T,this);
        timer.start();
        errmsg="[]";
        hlen=0.33;
        mlen=0.66;
        slen=1.0;
	}
	public static void main(String args[]){
		ClickClock obj=new ClickClock();
		obj.setup();
	}
	public void setup(){
		frame=new JFrame("TICK-TOCK THE CLICK-CLOCK");
		frame.setSize(frameW,frameH);
		frame.setBounds(screenSize.width/2-frameW/2, screenSize.height/2-frameH/2, frameW, frameH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameicon=Toolkit.getDefaultToolkit().getImage("CLOCKICON.png");
		frame.setIconImage(frameicon);
		frame.addComponentListener(this);
		setBackground(Color.DARK_GRAY);
        setFocusable(true);
        setVisible(true);
        setLayout(null);
        setFocusTraversalKeysEnabled(false);
		frame.add(this);
		//String s=LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy HH,mm,ss.ns"));
		frame.setVisible(true);
	}
	@Override
	public void paint(Graphics g){
		radius=3*Math.min(frameW, frameH)/8;
		g.setColor(Color.BLACK);
        g.fillRect(0, 0, frameW, frameH);
        g.setColor(Color.WHITE);
        if(errmsg.length()>2)
        	g.drawString(errmsg, OX-errmsg.length(), 20);
        errmsg="[]";
		g.setColor(Color.BLUE);
		g.fillArc(OX-(int)((radius)*slen), OY-(int)((radius)*slen), (int)(radius*2*slen), (int)(radius*2*slen), 90, (90+(int)Math.toDegrees(sTheta))*(-1));
		g.setColor(Color.BLACK);
		g.fillArc(OX-(int)((radius)*mlen), OY-(int)((radius)*mlen), (int)(radius*2*mlen), (int)(radius*2*mlen),0,360);
		g.setColor(Color.MAGENTA);
		g.fillArc(OX-(int)((radius)*mlen), OY-(int)((radius)*mlen), (int)(radius*2*mlen), (int)(radius*2*mlen), 90, (90+(int)Math.toDegrees(mTheta))*(-1));
		g.setColor(Color.BLACK);
		g.fillArc(OX-(int)((radius)*hlen), OY-(int)((radius)*hlen), (int)(radius*2*hlen), (int)(radius*2*hlen),0,360);
		g.setColor(Color.ORANGE);
		g.fillArc(OX-(int)((radius)*hlen), OY-(int)((radius)*hlen), (int)(radius*2*hlen), (int)(radius*2*hlen), 90, (90+(int)Math.toDegrees(hTheta))*(-1));
		g.setColor(Color.WHITE);
		g.drawOval(OX-radius, OY-radius, 2*radius,2*radius);
		for(int i=1;i<=12;i++){
			g.setColor(Color.WHITE);
			g.drawLine(OX+(int)(radius*0.8*Math.cos(Math.PI*i/6)), OY+(int)(radius*0.8*Math.sin(Math.PI*i/6)), OX+(int)(radius*Math.cos(Math.PI*i/6)), OY+(int)(radius*Math.sin(Math.PI*i/6)));
			for(int j=1;j<5;j++){
				g.setColor(Color.WHITE);
				g.drawLine(OX+(int)(radius*0.9*Math.cos(Math.PI*i/6+Math.PI*j/30)), OY+(int)(radius*0.9*Math.sin(Math.PI*i/6+Math.PI*j/30)), OX+(int)(radius*Math.cos(Math.PI*i/6+Math.PI*j/30)), OY+(int)(radius*Math.sin(Math.PI*i/6+Math.PI*j/30)));
				g.setColor(Color.BLACK);
				g.drawLine(OX+(int)(radius*hlen*Math.cos(Math.PI*i/6+Math.PI*j/30)), OY+(int)(radius*hlen*Math.sin(Math.PI*i/6+Math.PI*j/30)), OX+(int)(radius*mlen*Math.cos(Math.PI*i/6+Math.PI*j/30)), OY+(int)(radius*mlen*Math.sin(Math.PI*i/6+Math.PI*j/30)));
			}
			g.drawLine(OX, OY, OX+(int)(radius*mlen*Math.cos(Math.PI*i/6)), OY+(int)(radius*mlen*Math.sin(Math.PI*i/6)));
		}
	}
	public void calculate(){
		hours=Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH")));
		hours+=(hours>12)?(-12):0;
		minutes=Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("mm")));
		seconds=Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss")));
		hTheta=(hours/12.0)*2*Math.PI-Math.PI/2;
		mTheta=(minutes/60.0)*2*Math.PI-Math.PI/2;
		sTheta=(seconds/60.0)*2*Math.PI-Math.PI/2;
	}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent e) {
		windowSize = e.getComponent().getBounds().getSize();
        frameW=windowSize.width;
        frameH=windowSize.height;
        OX=frameW/2;
        OY=frameH/2;
	}
	public void componentShown(ComponentEvent arg0) {}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(timer)){
    		calculate();
    		repaint();
		}else
			errmsg="{ActionListener ERROR "+e.getID()+"]";
		
	}
}
