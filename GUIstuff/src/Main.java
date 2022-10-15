import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ComponentEvent;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class Main implements ActionListener,ComponentListener,ItemListener,Runnable{
    private JFrame frame;
    private JPanel panel;
    private JTextField portText;
    private JCheckBox chkbx_dualp,chkbx_fastspawn,chkbx_grid,chkbx_csnake,chkbx_gsnake,chkbx_pvp,chkbx_border,chkbx_portal,chkbx_multiportal,chkbx_onLANe;
    private JButton btn_StrtGme, btn_JnGme, btn_BstCh_inc, btn_BstCh_dec;
    private JLabel lbl_guidance, lbl_option;
    private Dimension windowSize, screenSize;
    private Image Gameicon;
    private int frameW, frameH;
    private String port;
    private boolean dualp,fastspawn,grid,border,gsnake,csnake,portal,multiportal,onLANe,pvp,optionsSelected;
    
    Main(){
    	optionsSelected=false;
    	frameW=500;
    	frameH=400;
    }
    public void componentHidden(ComponentEvent e){
        if(!onLANe && optionsSelected)
            ;//gameplay.gamestart=false;
    }
    public void componentMoved(ComponentEvent e){}
    public void componentShown(ComponentEvent e){
        if(!onLANe && optionsSelected)
            ;//gameplay.gamestart=start;
    }
    public void componentResized(ComponentEvent e) {
        windowSize = e.getComponent().getBounds().getSize();
        frameW=windowSize.width;
        frameH=windowSize.height;
        setUpPosOfComponetz();
    }
    public static void main(String[] args){
        Main obj=new Main();
        obj.setup();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    	
    	if(e.getSource().equals(btn_BstCh_inc)){
    		if(lbl_guidance.getText().equals("N/A")){
    			lbl_guidance.setText("P1");
    		}else if(lbl_guidance.getText().equals("P1")){
    			if(dualp)
    				lbl_guidance.setText("P2");
    			else if(csnake)
    				lbl_guidance.setText("C");
    			else if(gsnake)
    				lbl_guidance.setText("G");
    			else
    				lbl_guidance.setText("N/A");
    		}else if(lbl_guidance.getText().equals("P2")){
    			if(csnake){
    				lbl_guidance.setText("C");
    			}else if(gsnake){
    				lbl_guidance.setText("G");
    			}else{
    				lbl_guidance.setText("N/A");
    			}
    		}else if(lbl_guidance.getText().equals("C")){
    			if(gsnake){
    				lbl_guidance.setText("G");
    			}else{
    				lbl_guidance.setText("N/A");
    			}
    		}else if(lbl_guidance.getText().equals("G")){
    			lbl_guidance.setText("N/A");
    		}
    	}else if(e.getSource().equals(btn_BstCh_dec)){
    		if(lbl_guidance.getText().equals("P2")){
    			lbl_guidance.setText("P1");
    		}else if(lbl_guidance.getText().equals("N/A")){
    			if(gsnake)
    				lbl_guidance.setText("G");
    			else if(csnake)
    				lbl_guidance.setText("C");
    			else if(dualp)
    				lbl_guidance.setText("P2");
    			else
    				lbl_guidance.setText("P1");
    		}else if(lbl_guidance.getText().equals("G")){
    			if(csnake){
    				lbl_guidance.setText("C");
    			}else if(dualp){
    				lbl_guidance.setText("P2");
    			}else{
    				lbl_guidance.setText("P1");
    			}
    		}else if(lbl_guidance.getText().equals("C")){
    			if(dualp){
    				lbl_guidance.setText("P2");
    			}else{
    				lbl_guidance.setText("P1");
    			}
    		}else if(lbl_guidance.getText().equals("P1")){
    			lbl_guidance.setText("N/A");
    		}
    	}else if(e.getSource().equals(btn_StrtGme)){
    		frame.remove(panel);
    		optionsSelected=true;
    		frame.repaint();
    		startDemStuffz();
    	}else if(e.getSource().equals(btn_JnGme)){
    		optionsSelected=true;
    		port=portText.getText();
    	}
    }
    public void setup(){
        frame=new JFrame();
        panel=new JPanel();
        frame.setSize(frameW,frameH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Gameicon=Toolkit.getDefaultToolkit().getImage("ICON.png");
        frame.setIconImage(Gameicon);
        panel.setBackground(Color.DARK_GRAY);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.add(panel);
        frame.addComponentListener(this);
        panel.setLayout(null);
        
        lbl_option=			new JLabel("OPTIONS");
        lbl_guidance=		new JLabel("N/A");
        portText=			new JTextField("Enter port");
        btn_BstCh_inc=		new JButton("/\\");
        btn_BstCh_dec=		new JButton("\\/");
        btn_StrtGme=		new JButton("START");
        btn_JnGme=			new JButton("JOIN");
        chkbx_dualp=		new JCheckBox("Two Player Game"		,false);
        chkbx_fastspawn=	new JCheckBox("Quick Respawn"		,false);
        chkbx_grid=			new JCheckBox("Grid Lines"			,true);
        chkbx_csnake=		new JCheckBox("COMPUTER Snake"		,false);
    	chkbx_gsnake=		new JCheckBox("GHOST Snake"			,false);
    	chkbx_pvp=			new JCheckBox("PVP"					,false);
        chkbx_border=		new JCheckBox("BORDER"				,true);
        chkbx_portal=		new JCheckBox("Black Holes"			,false);
        chkbx_multiportal=	new JCheckBox("Multiple Black Holes",false);
    	chkbx_onLANe=		new JCheckBox("LAN"					,false);
        lbl_option.setForeground(Color.WHITE);
        lbl_guidance.setForeground(Color.WHITE);
        btn_BstCh_inc.addActionListener(this);
        btn_BstCh_dec.addActionListener(this);
        btn_StrtGme.addActionListener(this);
        btn_JnGme.addActionListener(this);
        panel.add(lbl_option);
        panel.add(lbl_guidance);
        panel.add(portText);
        panel.add(btn_BstCh_inc);
        panel.add(btn_BstCh_dec);
        panel.add(btn_StrtGme);
        panel.add(btn_JnGme);
        
        this.assignDemChkbxs(chkbx_dualp);
        this.assignDemChkbxs(chkbx_fastspawn);
        this.assignDemChkbxs(chkbx_grid);
        this.assignDemChkbxs(chkbx_csnake);
        this.assignDemChkbxs(chkbx_gsnake);
        this.assignDemChkbxs(chkbx_pvp);
        this.assignDemChkbxs(chkbx_border);
        this.assignDemChkbxs(chkbx_portal);
        this.assignDemChkbxs(chkbx_multiportal);
        this.assignDemChkbxs(chkbx_onLANe);
        setUpPosOfComponetz();
        
        portText.setVisible(false);
        chkbx_multiportal.setVisible(false);
        chkbx_onLANe.setVisible(false);
        chkbx_pvp.setVisible(false);        
        frame.setVisible(true);
    }
    public void assignDemChkbxs(JCheckBox chkbx){
    	panel.add(chkbx);
    	chkbx.addItemListener(this);
    	chkbx.setForeground(Color.WHITE);
    	chkbx.setOpaque(false);
    }
	public void setUpPosOfComponetz(){
								lbl_option.setBounds(frameW/2-30, 20, 60, 25);
        chkbx_fastspawn.setBounds(	frameW/4-75, 50, 150, 20);	chkbx_dualp.setBounds(		(frameW*3)/4-75, 50, 150, 20);
        chkbx_grid.setBounds(		frameW/4-75, 90, 150, 20);	chkbx_border.setBounds(		(frameW*3)/4-75, 90, 150, 20);
        chkbx_csnake.setBounds(		frameW/4-75, 130, 150, 20);	chkbx_gsnake.setBounds(		(frameW*3)/4-75, 130, 150, 20);
        chkbx_portal.setBounds(		frameW/4-75, 170, 150, 20);	chkbx_multiportal.setBounds((frameW*3)/4-75, 170, 150, 20);
        chkbx_pvp.setBounds(		frameW/4-75, 210, 150, 20);	chkbx_onLANe.setBounds(		(frameW*3)/4-75, 210, 150, 20);
        btn_BstCh_inc.setBounds(	frameW/4-55, 240, 40, 20);	portText.setBounds(			(frameW*3)/4-75, 235, 150, 20);
        lbl_guidance.setBounds(		frameW/4-55, 260, 40, 20);	
        btn_BstCh_dec.setBounds(	frameW/4-55, 280, 40, 20);
		btn_StrtGme.setBounds(		frameW/2-85, 260, 80, 25);  btn_JnGme.setBounds(		frameW/2+5, 260, 80, 25);
	}
    @Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("fdgfh");
		if(chkbx_portal.isSelected()){
			chkbx_multiportal.setVisible(true);
		}else{
			chkbx_multiportal.setVisible(false);
			chkbx_multiportal.setSelected(false);
		}
		if(chkbx_dualp.isSelected()||chkbx_csnake.isSelected()||chkbx_gsnake.isSelected()){
			chkbx_pvp.setVisible(true);
		}else{
			chkbx_pvp.setVisible(false);
			chkbx_pvp.setSelected(false);
		}
		if(chkbx_dualp.isSelected()){
			chkbx_onLANe.setVisible(true);
		}else{
			chkbx_onLANe.setVisible(false);
			chkbx_onLANe.setSelected(false);
		}
		if(chkbx_onLANe.isSelected()){
			portText.setVisible(true);
		}else{
			portText.setVisible(false);
			portText.setText("Enter port");
		}
		dualp=		chkbx_dualp.isSelected();
		fastspawn=	chkbx_fastspawn.isSelected();
		grid=		chkbx_grid.isSelected();
		border=		chkbx_border.isSelected();
		gsnake=		chkbx_gsnake.isSelected();
		csnake=		chkbx_csnake.isSelected();
		portal=		chkbx_portal.isSelected();
		multiportal=chkbx_multiportal.isSelected();
		onLANe=		chkbx_onLANe.isSelected();
		pvp=		chkbx_pvp.isSelected();
		System.out.println(fastspawn+","+dualp);
		System.out.println(grid+","+border);
		System.out.println(csnake+","+gsnake);
		System.out.println(portal+","+multiportal);
		System.out.println(pvp+","+onLANe);
	}
    @Override
	public void run() {
		System.out.println("thread is runnig"+k++ + Thread.currentThread());
		System.out.println(Thread.currentThread().getName());
		try{
			Thread.sleep(500);
		}catch(InterruptedException e){
			System.out.println(e);
		}
		System.out.println("thread is $ $ runnig"+k--+Thread.currentThread());
		System.out.println(Thread.currentThread());
	}
    public void startDemStuffz(){
		System.out.println("startdemsuffz");//TO-DO
		
	}
}
