import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.ComponentEvent;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
/* TODO
 * NO snake understands portals
 * add online LAN multiplier
 * make game focusable,
 * make buttons in add snake focusable
 * Twin snake
 * tweak the glitch Snake
 * 
 * add JUMBO Snake
 * 3D?
 * 4D? probably 3d itself is to complicated to play
 * 
 */
public class Main implements ComponentListener, ItemListener, Runnable {
	private JFrame frame, frm_option, frm_hlp;
	private JPanel panel, pnl_option, pnl_hlp;
	private JTextField portText, txt_row, txt_col, txt_nam;
	DefaultListModel<String> list2, list1;
	private JList<String> lst_snks, lst_snkskns;
	private JCheckBox chkbx_fastspawn, chkbx_grid, chkbx_pvp, chkbx_border,
			chkbx_portal, chkbx_multiportal, chkbx_onLANe, chkbx_dirAstnce;
	private JButton btn_StrtGme, btn_Hlp, btn_JnGme, btn_addSnake, btn_snakemade, btn_cnclsnkmk, btn_up, btn_down, btn_right, btn_left;
	private JLabel lbl_pix, lbl_row, lbl_col, lbl_option, lbl_preview, lbl_nam, lbl_snktyps, lbl_snkskins;
	private Font normalf = new Font("arial", Font.PLAIN, 14);
	private Dimension windowSize, screenSize;
	private Toolkit tkit =Toolkit.getDefaultToolkit();
	private Image Gameicon;
	static int frameW, frameH, skin, plyrDirCh=-1, maxrow = 50, maxcol = 75;
	private ArrayList<Integer> snaketypes =new ArrayList<>();
	private ArrayList<Integer> snakeskins =new ArrayList<>();
	private ArrayList<Boolean> guidances =new ArrayList<>();
	private ArrayList<String> snakenames =new ArrayList<>();
	private ArrayList<int[]>keyChList =new ArrayList<>();
	
	private String port;
	private boolean onLANe, optionsSelected, guide=false;

	Main() {
		optionsSelected = false;
		frameW = 500;
		frameH = 400;
	}

	public void componentHidden(ComponentEvent e) {
		if (!onLANe && optionsSelected)// TODO
			System.out.println("Hidden");// Threadilizing.gamestart=false;//TODO
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {
		if (!onLANe && optionsSelected)// TODO
			System.out.println("UnHidden");// Threadilizing.gamestart=true;//TODO
	}

	public void componentResized(ComponentEvent e) {
		windowSize = e.getComponent().getBounds().getSize();
		frameW = windowSize.width;
		frameH = windowSize.height;
		if(!GameBoard.gamestart)setUpPosOfComponetz();
		else{
			GameBoard.pix=Math.min((frameW-300-GameBoard.gap.x)/GameBoard.colno,(frameH-50-GameBoard.gap.y)/(GameBoard.rowno+4));
			GameBoard.width.setPoint(GameBoard.colno * GameBoard.pix + GameBoard.gap.x, GameBoard.rowno * GameBoard.pix + GameBoard.gap.y);
		}
	}

	public static void main(String[] args) {
		Main obj = new Main();
		obj.setup();
	}

	public void setup() {
		frame = new JFrame("SNAKE");
		panel = new JPanel();
		lbl_option = new JLabel("OPTIONS");
		lbl_row = new JLabel("Rows: ");
		lbl_col = new JLabel("Columns: ");
		//lbl_pix= new JLabel("Pixel Size: ");
		//txt_pix = new JTextField("25");
		txt_row = new JTextField("23");
		txt_col = new JTextField("34");
		portText = new JTextField("Enter port");
		btn_StrtGme = new JButton("START");
		btn_JnGme = new JButton("JOIN");
		btn_addSnake =new JButton("Add Snake");
		btn_Hlp = new JButton("HELP");
		
		frame.setSize(frameW, frameH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Gameicon = tkit.getImage("ICON.png");
		frame.setIconImage(Gameicon);
		panel.setBackground(Color.DARK_GRAY);
		screenSize = tkit.getScreenSize();
		frame.add(panel);
		frame.addComponentListener(this);
		panel.setLayout(null);
		
		chkbx_fastspawn = new JCheckBox("Quick Respawn", false);
		chkbx_grid = new JCheckBox("Grid Lines", false);
		chkbx_pvp = new JCheckBox("PVP", false);
		chkbx_border = new JCheckBox("BORDER", true);
		chkbx_portal = new JCheckBox("Black Holes", false);
		chkbx_multiportal = new JCheckBox("Multiple Black Holes", false);
		chkbx_onLANe = new JCheckBox("LAN", false);
		
		lbl_option.setForeground(Color.WHITE);
		//lbl_pix.setForeground(Color.WHITE);
		lbl_col.setForeground(Color.WHITE);
		lbl_row.setForeground(Color.WHITE);
		
		chkbx_fastspawn.setToolTipText("Click to allow snake to respawn immediatly after death");
		chkbx_grid.setToolTipText("Click to show GRID lines");
		chkbx_pvp.setToolTipText("Click to enable PvP between snakes");
		chkbx_border.setToolTipText("Click to disable border collisions");
		chkbx_portal.setToolTipText("Click to add teleportaion portals");
		chkbx_multiportal.setToolTipText("Click to add effects to portals");
		chkbx_onLANe.setToolTipText("Click to connect to the internet");
		txt_col.setToolTipText("Number of Columns (4-"+maxcol+")");
		txt_row.setToolTipText("Number of Rows (4-"+maxrow+")");
		btn_addSnake.setToolTipText("");///////////////////////////
		
		btn_StrtGme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.remove(panel);
				optionsSelected = true;
				frame.repaint();
				startDemStuffz();
			}
		});
		btn_JnGme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				optionsSelected = true;
				port = portText.getText();
			}
		});
		btn_addSnake.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewSetup();
				btn_addSnake.setVisible(false);
			}
		});
		btn_Hlp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				helpSetup();
				btn_Hlp.setVisible(false);
			}
		});
		panel.add(lbl_option);
		//panel.add(lbl_pix);
		panel.add(lbl_col);
		panel.add(lbl_row);
		//panel.add(txt_pix);
		panel.add(txt_col);
		panel.add(txt_row);
		panel.add(portText);
		panel.add(btn_StrtGme);
		panel.add(btn_JnGme);
		panel.add(btn_Hlp);
		panel.add(btn_addSnake);

		this.assignDemChkbxs(chkbx_fastspawn);
		this.assignDemChkbxs(chkbx_grid);
		this.assignDemChkbxs(chkbx_pvp);
		this.assignDemChkbxs(chkbx_border);
		this.assignDemChkbxs(chkbx_portal);
		this.assignDemChkbxs(chkbx_multiportal);
		this.assignDemChkbxs(chkbx_onLANe);
		
		chkbx_fastspawn.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_fastspawn.setToolTipText(((e.getStateChange()%2==0)?"Click to allow snake to respawn immediatly after death":"Allows snake to respawn immediatly after death"));
			}
		});
		chkbx_grid.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_grid.setToolTipText(((e.getStateChange()%2==0)?"Click to show GRID lines":"Shows GRID lines"));
			}
		});
		chkbx_pvp.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_pvp.setToolTipText(((e.getStateChange()%2==0)?"Click to enable PvP between snakes":"PvP Enabled"));
			}
		});
		chkbx_border.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_border.setToolTipText(((e.getStateChange()%2==0)?"Border Disabled":"Click to disable border collisions"));
			}
		});
		chkbx_portal.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_portal.setToolTipText(((e.getStateChange()%2==0)?"Click to add teleportaion portals":"Teleportaion Portal: ENABLED"));
				if (!chkbx_portal.isSelected()) chkbx_multiportal.setSelected(false);
				chkbx_multiportal.setVisible(chkbx_portal.isSelected());
			}
		});
		chkbx_multiportal.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_multiportal.setToolTipText(((e.getStateChange()%2==0)?"Click to add effects to portals":"Portals have special effects"));
				chkbx_multiportal.setVisible(chkbx_portal.isSelected());
			}
		});
		chkbx_onLANe.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				chkbx_multiportal.setToolTipText(((e.getStateChange()%2==0)?"Click to play Online":"Click to play Offline"));
				if (chkbx_onLANe.isSelected())portText.setVisible(true);
				else {
					portText.setVisible(false);
					portText.setText("Enter port");
				}
				onLANe = chkbx_onLANe.isSelected();
			}
		});

		setUpPosOfComponetz();

		portText.setVisible(false);
		chkbx_multiportal.setVisible(false);
		chkbx_onLANe.setVisible(false);//delete this line when implemented TODO
		frame.setVisible(true);
	}
	
	public void helpSetup(){
		frm_hlp = new JFrame("HELP");
		pnl_hlp = new JPanel(){
			private static final long serialVersionUID = 12L;
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				int ofst=3;
				g.setFont(normalf);
				g.drawString("            There are 5 Snakes:", 10, 15);
				g.drawString("Player Snake:  You and your friends", 10, 30);
				g.drawString("Computer Snake:  Basic AI", 10, 45);
				g.drawString("GHOST Snake:  hovers around the apple, eats other snakes", 10, 60);
				g.drawString("Splitter Snake:  When reaching length 6, it splits", 10, 75);
				g.drawString("Glitch Snake:  Motives Unclear, GLITCHES", 10, 90);
				g.drawString("            There are Portals:", 10, 105);
				g.drawString("Normal: go in oneside, come out the other", 10, 120);
				g.drawString("Reverse: go in one direction, come out in the opposite direction", 10, 135);
				g.drawString("Clockwise: rotates you clockwise when leaving a portal", 10, 150);
				g.drawString("Anticlockwise: rotates you anticlockwise when leaving a portal", 10, 165);
				g.drawString("When the border  is diabled, the wals wrap around", 10, 180);
				g.drawString("PRESS SPACE: when a snake dies, it can come back", 10, 195);
				g.drawString("PRESS SLASH: for a hard reset/pause", 10, 210);
				g.setColor(Color.RED);
				g.drawString("Enter Snake name, Hit TAB, now u can click on the buttons", 10, 225);
				g.drawString(" and press the coresponding key for the direction", 10, 240);
				g.drawString("When game starts, Alt+Tab out and back in, then it works", 10, 255);
			}
		};
		JButton btn_back =new JButton("BACK");
		pnl_hlp.setFocusable(true);
		pnl_hlp.setFocusTraversalKeysEnabled(false);
		frm_hlp.setSize(frameW, frameH);
		frm_hlp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pnl_hlp.setBackground(Color.BLACK);
		frm_hlp.add(pnl_hlp);
		pnl_hlp.setLayout(null);
		btn_back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btn_Hlp.setVisible(true);
				frm_hlp.dispose();
			}
		});
		
		btn_back.setFocusable(false);		
		pnl_hlp.add(btn_back);
		helpSetUpPosOfComponetz(btn_back);
		
		frm_hlp.setVisible(true);
		frm_hlp.setResizable(false);
		pnl_hlp.repaint();
	}
	
	public void helpSetUpPosOfComponetz(JButton btn_back){
		btn_back.setBounds(200, 320, 100, 25);
	}
	
	public void previewSetup(){
		skin=0;
		int[] keyCh={0,0,0,0};
		frm_option = new JFrame("Snake Creator");
		pnl_option = new JPanel() {
			private static final long serialVersionUID = 11L;
			@Override
			protected void paintComponent(Graphics g){
				super.paintComponent(g);
				g.setColor(Color.WHITE);
				int ofst=3;
				g.drawLine(230-ofst, 210-ofst, 310+ofst, 210-ofst);
				g.drawLine(230-ofst, 285+ofst, 310+ofst, 285+ofst);
				g.drawLine(190-ofst, 235-ofst, 230-ofst, 235-ofst);
				g.drawLine(310+ofst, 235-ofst, 350+ofst, 235-ofst);
				g.drawLine(190-ofst, 260+ofst, 230-ofst, 260+ofst);
				g.drawLine(310+ofst, 260+ofst, 350+ofst, 260+ofst);
				g.drawLine(230-ofst, 210-ofst, 230-ofst, 235-ofst);
				g.drawLine(310+ofst, 210-ofst, 310+ofst, 235-ofst);
				g.drawLine(230-ofst, 260+ofst, 230-ofst, 285+ofst);
				g.drawLine(310+ofst, 260+ofst, 310+ofst, 285+ofst);
				g.drawLine(190-ofst, 235-ofst, 190-ofst, 260+ofst);
				g.drawLine(350+ofst, 235-ofst, 350+ofst, 260+ofst);
				
				g.drawRect(25, 50, 175, 125);
				g.drawRect(249,49,101,list1.size()*18+1);
				g.drawRect(359,49,101,list2.size()*18+1);
				if(lst_snkskns.getSelectedIndex()+1==9 && skin==5)drawSnakesPreview(g, 6);
				drawSnakesPreview(g, skin);
				if(snaketypes.size()>20){
					g.setColor(Color.RED);
					g.setFont(new Font("arial", Font.BOLD, 30));
					g.drawString("Too Many snakes", 10, 30);
					g.drawString("May cause Lag", 10, frameH-100);
				}
			}
			private void drawSnakesPreview(Graphics g, int skin){
				String imgname[] = { "PLACEHOLDERS", "snake1", "snake2", "csnake", "gsnake","lsnake" ,"rsnake" ,"splitsnake" ,"glitchsnake"};
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeTailR"+skin+"_0.png"), 50, 125, 25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeBodyUL"+skin+"_0.png"), 75, 125,25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeBodyUD"+skin+"_0.png"), 75, 100,25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeBodyDR"+skin+"_0.png"), 75, 75, 25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeBodyLR"+skin+"_0.png"), 100,75, 25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeBodyDL"+skin+"_0.png"), 125,75, 25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeBodyUR"+skin+"_0.png"), 125,100,25, 25, this);
				g.drawImage(tkit.getImage("res/"+imgname[skin]+"/SnakeHeadRIGHT"+skin+"_0.png"),150,100,25,25, this);
			}
		};
		lbl_preview =new JLabel("PREVIEW");
		lbl_snkskins=new JLabel("Skins");
		lbl_snktyps=new JLabel("Snake");
		lbl_nam=new JLabel("Snake's Name: ");
		txt_nam=new JTextField("Bob");
		btn_snakemade =new JButton("APPLY");
		btn_cnclsnkmk =new JButton("CANCEL");
		btn_up= new JButton("UP");
		btn_down= new JButton("DOWN");
		btn_right= new JButton("RIGHT");
		btn_left= new JButton("LEFT");
		chkbx_dirAstnce= new JCheckBox("Direction Assitance");
		list2=new DefaultListModel<>();
		list2.addElement("Player Snake");
		list2.addElement("Player2 Snake");
		list2.addElement("Computer Snake");
		list2.addElement("Ghost Snake");
		list2.addElement("Left Twin Snake");
		list2.addElement("Right Twin Snake");
		list2.addElement("Splitter Snake");
		list2.addElement("Glitch Snake");
		list2.addElement("Default");
		lst_snkskns =new JList<>(list2);
		list1=new DefaultListModel<>();
		list1.addElement("Player Snake");
		list1.addElement("Computer Snake");
		list1.addElement("Ghost Snake");
		//list1.addElement("Twin Snakes");
		list1.addElement("Splitter Snake");
		list1.addElement("Glitch Snake");
		lst_snks =new JList<>(list1);
		pnl_option.setFocusable(true);
		pnl_option.setFocusTraversalKeysEnabled(false);
		frm_option.setSize(frameW, frameH);
		frm_option.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pnl_option.setBackground(Color.BLACK);
		frm_option.add(pnl_option);
		pnl_option.setLayout(null);
		
		chkbx_dirAstnce.setOpaque(false);
		chkbx_dirAstnce.setForeground(Color.WHITE);
		lbl_preview.setForeground(Color.WHITE);
		lbl_snktyps.setForeground(Color.WHITE);
		lbl_snkskins.setForeground(Color.WHITE);
		lbl_nam.setForeground(Color.WHITE);
		lst_snks.setBackground(Color.GRAY);
		lst_snkskns.setBackground(Color.GRAY);
		lst_snks.setForeground(Color.WHITE);
		lst_snkskns.setForeground(Color.WHITE);
		lst_snkskns.setSelectedIndex(8);
		lst_snks.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(lst_snkskns.getSelectedIndex()+1==9){
					skin=lst_snks.getSelectedIndex()+1;
					skin+=(skin>1)?1:0;
					skin+=(skin>4)?2:0;//skin+=(skin>5)?1:0;
				}
				pnl_option.repaint();
			}
		});
		lst_snkskns.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()){
					skin=lst_snkskns.getSelectedIndex()+1;
					if(lst_snkskns.getSelectedIndex()+1==9){
						skin=lst_snks.getSelectedIndex()+1;
						skin+=(skin>1)?1:0;
						skin+=(skin>4)?2:0;//skin+=(skin>5)?1:0;
					}
					pnl_option.repaint();
				}
			}
		});
		chkbx_dirAstnce.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				guide=chkbx_dirAstnce.isSelected();
			}
		});
		btn_snakemade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int typ=lst_snks.getSelectedIndex()+1;
				typ+=(typ>3)?1:0;
				snaketypes.add(typ);
				snakenames.add(txt_nam.getText());
				snakeskins.add(skin);
				keyChList.add(keyCh);
				guidances.add(guide);
				btn_addSnake.setVisible(true);
				frm_option.dispose();
			}
		});
		btn_cnclsnkmk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btn_addSnake.setVisible(true);
				frm_option.dispose();
			}
		});
		btn_up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plyrDirCh=Snake.FACING_UP;
			}
		});
		btn_down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plyrDirCh=Snake.FACING_DOWN;
			}
		});
		btn_left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plyrDirCh=Snake.FACING_LEFT;
			}
		});
		btn_right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				plyrDirCh=Snake.FACING_RIGHT;
			}
		});
		pnl_option.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(plyrDirCh>=0)keyCh[plyrDirCh-1]=e.getKeyCode();
				if(plyrDirCh==Snake.FACING_UP)
					btn_up.setText(KeyEvent.getKeyText(e.getKeyCode()));
				else if(plyrDirCh==Snake.FACING_DOWN)
					btn_down.setText(KeyEvent.getKeyText(e.getKeyCode()));
				else if(plyrDirCh==Snake.FACING_RIGHT)
					btn_right.setText(KeyEvent.getKeyText(e.getKeyCode()));
				else if(plyrDirCh==Snake.FACING_LEFT)
					btn_left.setText(KeyEvent.getKeyText(e.getKeyCode()));
			}
		});
		btn_up.setFocusable(false);
		btn_down.setFocusable(false);
		btn_left.setFocusable(false);
		btn_right.setFocusable(false);
		btn_snakemade.setFocusable(false);
		btn_cnclsnkmk.setFocusable(false);
		chkbx_dirAstnce.setFocusable(false);
		
		pnl_option.add(chkbx_dirAstnce);
		pnl_option.add(btn_up);
		pnl_option.add(btn_down);
		pnl_option.add(btn_right);
		pnl_option.add(btn_left);
		pnl_option.add(btn_snakemade);
		pnl_option.add(btn_cnclsnkmk);
		pnl_option.add(lbl_preview);
		pnl_option.add(lbl_snkskins);
		pnl_option.add(lbl_snktyps);
		pnl_option.add(lbl_nam);
		pnl_option.add(txt_nam);
		pnl_option.add(lst_snks);
		pnl_option.add(lst_snkskns);
		previewSetUpPosOfComponetz();
		
		frm_option.setVisible(true);
		frm_option.setResizable(false);
		pnl_option.repaint();
	}

	protected void previewSetUpPosOfComponetz(){
		lbl_preview.setBounds(85,25,60,25);
		lbl_snktyps.setBounds(270,25,60,25);
		lbl_snkskins.setBounds(390,25,60,25);
		lst_snks.setBounds(250,50,100,list1.size()*18);
		lst_snkskns.setBounds(360,50,100,list2.size()*18);
		lbl_nam.setBounds(31,175,100,25);
		txt_nam.setBounds(119,175,75,25);
		btn_up.setBounds(230, 210, 80, 25);
		btn_left.setBounds(190, 235, 80, 25);
		btn_right.setBounds(270, 235, 80, 25);
		btn_down.setBounds(230, 260, 80, 25);
		chkbx_dirAstnce.setBounds(25, 230, 200, 25);
		btn_snakemade.setBounds(310, 320, 100, 25);
		btn_cnclsnkmk.setBounds(200, 320, 100, 25);
	}

	private void assignDemChkbxs(JCheckBox chkbx) {
		panel.add(chkbx);
		chkbx.setForeground(Color.WHITE);
		chkbx.setOpaque(false);
	}

	private void setUpPosOfComponetz() {
		lbl_option.setBounds(frameW / 2 - 30, 20, 60, 25);
		chkbx_fastspawn.setBounds(frameW / 4 - 75, 50, 150, 20);
		chkbx_pvp.setBounds((frameW * 3) / 4 - 75, 50, 150, 20);
		chkbx_grid.setBounds(frameW / 4 - 75, 90, 150, 20);
		chkbx_border.setBounds((frameW * 3) / 4 - 75, 90, 150, 20);
		chkbx_portal.setBounds(frameW / 4 - 75, 130, 150, 20);
		chkbx_multiportal.setBounds((frameW * 3) / 4 - 75, 130, 150, 20);
		lbl_row.setBounds(frameW / 4 - 75, 170, 60, 20);
		lbl_col.setBounds((frameW * 3) / 4 - 75, 170, 60, 20);
		//lbl_pix.setBounds(frameW/2-90, 170, 80, 20);
		//txt_pix.setBounds(frameW/2-30, 170, 25, 20);
		txt_row.setBounds(frameW / 4 - 40, 170, 25, 20);
		txt_col.setBounds((frameW * 3) / 4 - 20, 170, 25, 20);
		//.setBounds(frameW / 4 - 75, 210, 150, 20);
		chkbx_onLANe.setBounds((frameW * 3) / 4 - 75, 210, 150, 20);
		//btn_BstCh_inc.setBounds(frameW / 4 - 55, 240, 40, 20);
		portText.setBounds((frameW * 3) / 4 - 75, 235, 150, 20);
		//lbl_guidance.setBounds(frameW / 4 - 55, 260, 40, 20);
		//btn_BstCh_dec.setBounds(frameW / 4 - 55, 280, 40, 20);
		btn_StrtGme.setBounds(frameW / 2 -140, 260, 80, 25);
		btn_JnGme.setBounds(frameW / 2 + 60, 260, 80, 25);
		btn_addSnake.setBounds(frameW / 2 - 50, 260, 100, 25);
		btn_Hlp.setBounds(frameW / 2 - 40, 290, 80, 25);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {}

	@Override
	public void run() {
		// System.out.println("thread is runnig"+k++ + Thread.currentThread());
		System.out.println(Thread.currentThread().getName());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {System.out.println(e);}
		// System.out.println("thread is $ $ runnig"+k--+Thread.currentThread());
		System.out.println(Thread.currentThread());
	}

	private void startDemStuffz() {
		GameBoard gameboard = new GameBoard();
		for(int i=0;i<snaketypes.size();i++)
			gameboard.addSnake(snaketypes.get(i), snakeskins.get(i),snakenames.get(i),keyChList.get(i),guidances.get(i));
		GameBoard.fastspawn = chkbx_fastspawn.isSelected();
		GameBoard.grid = chkbx_grid.isSelected();
		GameBoard.border = chkbx_border.isSelected();
		GameBoard.portal = chkbx_portal.isSelected();
		GameBoard.multiportal = chkbx_multiportal.isSelected();
		GameBoard.pvp = chkbx_pvp.isSelected();
		GameBoard.colno=Integer.parseInt(txt_col.getText());
		GameBoard.colno=(GameBoard.colno<=snaketypes.size()*3)?snaketypes.size()*3+1:Math.min(GameBoard.colno,maxrow);
		GameBoard.rowno=Integer.parseInt(txt_row.getText());
		GameBoard.rowno=(GameBoard.rowno<=snaketypes.size()*3)?snaketypes.size()*3+1:Math.min(GameBoard.rowno,maxrow);
		GameBoard.pix=25;//Integer.parseInt(txt_pix.getText());
		GameBoard.width = new Point(GameBoard.colno * GameBoard.pix + GameBoard.gap.x, GameBoard.rowno * GameBoard.pix + GameBoard.gap.y);
		GameBoard.enemy.randomizeInt(GameBoard.colno, GameBoard.rowno);
		GameBoard.gamestart = true;
		frame.setBounds(screenSize.width / 2 - (GameBoard.width.x + 30) / 2, screenSize.height/2-(GameBoard.width.y+50)/2, GameBoard.width.x+300, 50+GameBoard.width.y+GameBoard.pix*4);// 905=875+30,650+50=700
		frame.setBackground(Color.DARK_GRAY);
		frame.add(gameboard);
	}


}