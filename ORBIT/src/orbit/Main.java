package orbit;

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
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Main implements ActionListener,ComponentListener,KeyListener{
	private JFrame frame;
	private JPanel panel;
	private JLabel mlbl,rlbl,xlbl,ylbl,Vxlbl,Vylbl,Albl,Blbl,Clbl;
	private JButton strtbtn,imprtbtn,dfltbtn;
	private JTextField txtfld[][]=new JTextField[3][6];
	private JFileChooser flCh;
	private Dimension windowSize, screenSize;
    private Image frameicon;
    private ArrayList<String> imprtdS=new ArrayList<String>();
    private int frameW, frameH;
    
	public Main(){
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameW=450;
        frameH=250;
	}
	public void setup(){
		frame=new JFrame("ORBIT");
        panel=new JPanel();
        frame.setSize(frameW,frameH);
        frame.setBounds(screenSize.width/2-frameW/2, screenSize.height/2-frameH/2, frameW, frameH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameicon=Toolkit.getDefaultToolkit().getImage("BlackHole.png");
        frame.setIconImage(frameicon);
        panel.setBackground(Color.DARK_GRAY);
        panel.setFocusable(true);
        panel.setVisible(true);
        frame.addComponentListener(this);
        panel.setLayout(null);
        frame.add(panel);
        panel.addKeyListener(this);
        panel.setFocusTraversalKeysEnabled(false);
        
        mlbl=new JLabel("Mass");xlbl=new JLabel("X");Vxlbl=new JLabel("Vx");
        rlbl=new JLabel("Radius");ylbl=new JLabel("Y");Vylbl=new JLabel("Vy");
        Albl=new JLabel("A");Blbl=new JLabel("B");Clbl=new JLabel("C");
		strtbtn=new JButton("START");imprtbtn=new JButton("IMPORT");dfltbtn=new JButton("DEFAULT");
		flCh=new JFileChooser(".\\");
		for(int i=0;i<3;i++){
			for(int j=0;j<6;j++)
				txtfld[i][j]=new JTextField();
		}
		mlbl.setForeground(Color.WHITE);xlbl.setForeground(Color.WHITE);Vxlbl.setForeground(Color.WHITE);
		rlbl.setForeground(Color.WHITE);ylbl.setForeground(Color.WHITE);Vylbl.setForeground(Color.WHITE);
		Albl.setForeground(Color.WHITE);Blbl.setForeground(Color.WHITE);Clbl.setForeground(Color.WHITE);
		bounder();
        for(int i=0;i<3;i++){
			for(int j=0;j<6;j++)
				txtfld[i][j].setBounds(100+(50*j),45+(25*i),40,25);
		}
        strtbtn.addActionListener(this);
        imprtbtn.addActionListener(this);
        dfltbtn.addActionListener(this);
        panel.add(mlbl);panel.add(xlbl);panel.add(Vxlbl);
        panel.add(rlbl);panel.add(ylbl);panel.add(Vylbl);
        panel.add(Albl);panel.add(Blbl);panel.add(Clbl);
        panel.add(strtbtn);panel.add(imprtbtn);panel.add(dfltbtn);
        for(int i=0;i<3;i++){
			for(int j=0;j<6;j++)
				panel.add(txtfld[i][j]);
		}
		
		frame.setVisible(true);
	}
	public void bounder(){
		mlbl.setBounds(100,20,40,25);xlbl.setBounds(200,20,40,25);Vxlbl.setBounds(300,20,40,25);
		rlbl.setBounds(150,20,40,25);ylbl.setBounds(250,20,40,25);Vylbl.setBounds(350,20,40,25);
		Albl.setBounds(50,45,20,25);Blbl.setBounds(50,70,20,25);Clbl.setBounds(50,95,20,25);
        strtbtn.setBounds(frameW/2-120, 120, 80, 40);imprtbtn.setBounds(frameW/2-40, 120, 80, 40);dfltbtn.setBounds(frameW/2+40, 120, 100, 40);
	}
	public static void main(String args[]){
		Main obj=new Main();
		obj.setup();
	}
	public void reader(){
		String s="";
		if(flCh.showOpenDialog(panel)==JFileChooser.APPROVE_OPTION){
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
							txtfld[i][j].setText(s.substring(0,s.indexOf("$")));
							s=s.substring(s.indexOf("#")+1);
						}
					}
				}
				reader.close();
			}catch(Exception e){
				System.out.println("[FILE ERROR]");
				e.printStackTrace();
			}
		}
	}
	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentResized(ComponentEvent e) {
		windowSize = e.getComponent().getBounds().getSize();
        frameW=windowSize.width;
        frameH=windowSize.height;
        bounder();
	}
	public void componentShown(ComponentEvent e) {}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(strtbtn)){
			openOrbtlng();
		}else if(e.getSource().equals(dfltbtn)){
			txtfld[0][0].setText("400000.0");	txtfld[0][1].setText("50");		txtfld[0][2].setText("0");		txtfld[0][3].setText("0");		txtfld[0][4].setText("0");		txtfld[0][5].setText("0");
			txtfld[1][0].setText("50000.0");	txtfld[1][1].setText("20");		txtfld[1][2].setText("700");	txtfld[1][3].setText("0");		txtfld[1][4].setText("0");		txtfld[1][5].setText("0.6");
			txtfld[2][0].setText("1.0");		txtfld[2][1].setText("4");		txtfld[2][2].setText("750");	txtfld[2][3].setText("0");		txtfld[2][4].setText("0");		txtfld[2][5].setText("0.86");
//			<--+--+--+--+-M -+--+--+--+-->		<--+--+--+--+-R -+--+--+--+-->	<--+--+--+--+-X -+--+--+--+-->	<--+--+--+--+-Y -+--+--+--+-->	<--+--+--+--+-Vx-+--+--+--+-->	<--+--+--+--+-Vy-+--+--+--+-->
		}else if(e.getSource().equals(imprtbtn)){
			reader();
		}
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			openOrbtlng();
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	public void openOrbtlng(){
		Orbitaling ob=new Orbitaling();
		String temp="";
        for(int i=0;i<3;i++){
			for(int j=0;j<6;j++){
				temp=txtfld[i][j].getText();
				ob.data[i][j/2][j%2]=Double.parseDouble(((temp.length()>0)?temp:"0.0"));
			}
		}
        ob.assignABC();
        frame.remove(panel);
		frame.setBounds(screenSize.width/2-(ob.frameW)/2,screenSize.height/2-(ob.frameH)/2,ob.frameW,ob.frameH);
        frame.setBackground(Color.DARK_GRAY);
        ob.setupOrbitaling();
        frame.add(ob);
        frame.repaint(0, 0, ob.frameW-210, ob.frameH);
	}
}