import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main implements ActionListener,ComponentListener,KeyListener{
	private JFrame frame;
	private JPanel panel;
	private JButton birdies_btn;
	private Dimension windowSize, screenSize;
    private Image frameicon;
    private int frameW, frameH;
    
    public Main(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frameW=screenSize.width/2;
		frameH=screenSize.height/2;
	}
    public static void main(String args[]){
    	Main obj=new Main();
		obj.setup();
    }
    public void setup(){
		frame=new JFrame("MAIN FRAME");
        panel=new JPanel();
        frame.setSize(frameW,frameH);
        frame.setBounds(screenSize.width/2-frameW/2, screenSize.height/2-frameH/2, frameW, frameH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameicon=Toolkit.getDefaultToolkit().getImage("ICON.png");
        frame.setIconImage(frameicon);
        frame.addComponentListener(this);
        panel.setBackground(Color.DARK_GRAY);
        panel.setFocusable(true);
        panel.setVisible(true);
        panel.setLayout(null);
        panel.addKeyListener(this);
        panel.setFocusTraversalKeysEnabled(false);
        birdies_btn=new JButton("BIRDIES");
        birdies_btn.addActionListener(this);
        panel.add(birdies_btn);
        frame.add(panel);
        bounder();
        frame.setVisible(true);
    }
    public void bounder(){
    	birdies_btn.setBounds(frameW/2, frameH/2, 80, 40);
    }
    public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentResized(ComponentEvent e) {
		windowSize = e.getComponent().getBounds().getSize();
        frameW=windowSize.width;
        frameH=windowSize.height;
        bounder();
	}
	public void componentShown(ComponentEvent arg0) {}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(birdies_btn)){
			Birdies ob=new Birdies();
			ob.setup();
		}//else if(e.getSource().equals(imprtbtn)){
			//;
		//}
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			;//TODO
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
}
