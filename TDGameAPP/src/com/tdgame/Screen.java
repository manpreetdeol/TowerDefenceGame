package com.tdgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.FileNotFoundException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;


/** 
 * This class is responsible for controlling the life cycle of the application, it contains the method to create,
 * load and save a map based upon the dimensions entered by the user. This class is also responsible for controlling 
 * the life cycle of the towers on the map.
 * 
 * IMPORTANT: 1) For external resources we are using relative path. For eg, "../level/Base.xml"
 * 
 * 			  2) The width and height of the applet should be set to the screen size manually by going into 'Run Configurations > 
 * 				 Parameters'. This would be changed into a browser driven applet that would display the applet dynamically.
 * 
 * @author Team 2
 * @version $revision
 * 
 */
public class Screen extends JPanel implements Runnable{

	/* Common variables */
	Frame frame;
	LevelFile levelFile;
	ActionHandler actionHandler;
	MouseHandler mouseHandler;
	Thread thread = new Thread(this);
	boolean running = false;
	int scene;
	
	/* Map grid variables */
	private static Image image;
	public static boolean startGame = true;
	static String typeOfOperation = null;
	public static int[][] map;
	public Image[] terrain = new Image[100];
	private String packagename = "com/tdgame";
	public int handXPos = 0;
	public int handYPos = 0;
	static String newFileName;	
	static int valueOfX;
	static int valueOfY;
	double width;
	double height;	
	String towerImgPath;
	
	/* Tower Variables */
	public Tower[][] towerMap;
	public int towerWidth = 50;
	public int towerHeight = 50;
	static Tower selectedTower;
	static Tower inHandTower;

	public JButton button;
	public Tower towers[]=new Tower[5];
	public String towerNames[]={"Fire","Laser","Bomber","Tank","Missile"};
	public int towerInHand = 0;
	static String towerType="";
	User user;
	static int towerId=0;
	boolean placingTower = false;
	Color selectedTowerColor = null;
	int selectedTowerRange = 0;
	Rectangle repaintMapRectangle;
	Rectangle onMapPropertyRectangle;
	
	
	// Table for user specific details
	Object userRowData[][] = { { "Cash","" }};
	 static Object columnNam[] = { "User", "Data" };
	 JTable userDataTbl = new JTable(userRowData, columnNam);
	 
	JScrollPane userScrollPane = new JScrollPane(userDataTbl);
	
	// Table for displaying Tower store properties
	Object onMapRowData[][] = { { "Type","" },
			{ "Ammunition", "" },
			{ "Range", "" },
			{ "Cost", "" },
			{ "Rate of Fire", "" },
			{ "Refund Rate", "" },
			{ "Add Ammunition Rate", "" }};
	 static Object columnNames[] = { "Properties", "Value" };
	 JTable onMapTowerPropTbl = new JTable(onMapRowData, columnNames);
	 
	JScrollPane onMapScrollPane = new JScrollPane(onMapTowerPropTbl);

	// Table for displaying Tower store properties
	static Object offMapRowData[][] = { { "Type","" },
			{ "Ammunition", "" },
			{ "Range", "" },
			{ "Cost", "" },
			{ "Rate of Fire", "" },
			{ "Refund Rate", "" },
			{ "Add Ammunition Rate", "" }};
	static JTable offMapTowerPropTbl = new JTable(offMapRowData, columnNames);
	static JScrollPane offMapScrollPane = new JScrollPane(offMapTowerPropTbl);
	
	int xPosInGridMap;
	int yPosInGridMap;
	
	
	/* Instruction Variables */
	static String welcomeMessage = "Welcome to Tower Defence Game";
	static String instructions = "In order to play this game, you need a "
			+"\nmap."
			+"\n\nYou can either create your own map by "
			+"\nselecting CREATE MAP from the menu "
			+"\nor you can load an already existing map "
			+"\nby selecting LOAD MAP from the menu. "
			+"\n\nIf you want to create the map, you need "
			+"\nto save the map before you can start "
			+"\nplaying. "
			+ "\n\nOnce the map is saved, you can buy towers"
			+ "\nfrom the tower store and place them on the"
			+ "\nmap.";
	
	// Screen constructor
	public Screen(Frame frame) {
		this.frame = frame;	
		actionHandler = new ActionHandler(this, frame);
		onMapPropertyRectangle=new Rectangle(this.frame.getWidth() - 300 ,0,300,400);
		
		/* set the image */
		image = new ImageIcon("../res/TowerDefense.png").getImage();

	}	

	/** 
	 *  This method is called when the user wants to create a map.
	 *  
	 *  It would ask the user to enter dimensions of the map and the filename with which he wants to save the map.
	 */
	public void createMap() {

		typeOfOperation = "createMap";
		String mapDimensions = actionHandler.askUserToEnterTheDimensions();

		if(mapDimensions != null) {
			String []splitDimensions = mapDimensions.split(" ");
			valueOfX = Integer.parseInt(splitDimensions[0]);
			valueOfY = Integer.parseInt(splitDimensions[1]);

			map = new int[valueOfX][valueOfY];

			mouseHandler = new MouseHandler(this, valueOfX, valueOfY, true);

			newFileName = actionHandler.saveMapByName();
			instructions = "Please select a start point!!";

			addingMouseListener(typeOfOperation);			

			towerMap = new Tower[valueOfX][valueOfY];

			loadGame();

			towerMap = new Tower[valueOfX][valueOfY];	
			startGame("Base.xml", typeOfOperation, user);
			ImageIcon pic = new ImageIcon("grass.png");
			for(int i=0;i<5;i++){
				towers[i] = new Tower(i,towerNames[i]);
				towers[i].setText(towerNames[i]);
				this.add(towers[i]);
				towers[i].addActionListener(actionHandler);
				towers[i].addMouseListener(actionHandler);
				towers[i].addMouseMotionListener(actionHandler);
			}
			Rectangle onMapPropertyRectangle=new Rectangle(this.frame.getWidth() - 300 ,0,300,400);
			repaint(onMapPropertyRectangle);

			if (thread.getState() == Thread.State.NEW) {
				thread.start();
			}
		}
	}
	

	/** 
	 *  This method is called when the user wants to load an existing map
	 *  
	 *  A list of existing files will be displayed and the user will chose the files to be loaded as per his wish
	 */
	public void loadMap() {

		typeOfOperation = "loadMap";
		newFileName = actionHandler.loadExistingMap();

		if(newFileName != null) {
			ReadXML readXML = new ReadXML();

			// Based upon the file to be loaded, get the number of rows and columns for loading map
			String rows_cols = readXML.getLengthOfExistingMap(newFileName);

			valueOfX = Integer.parseInt(rows_cols.split("_")[1]);
			valueOfY = Integer.parseInt(rows_cols.split("_")[0]);				

			mouseHandler = new MouseHandler(this, valueOfX, valueOfY, true);
			MouseHandler.mapCompleted = true;

			addingMouseListener(typeOfOperation);			

			loadGame();

			towerMap = new Tower[valueOfX][valueOfY];	
			frame.getContentPane().validate();
			startGame(newFileName, typeOfOperation, user);
			ImageIcon pic = new ImageIcon("grass.png");

			for(int i=0;i<5;i++){
				towers[i] = new Tower(i,towerNames[i]);
				towers[i].setText(towerNames[i]);
				this.add(towers[i]);
				towers[i].addActionListener(actionHandler);
				towers[i].addMouseListener(actionHandler);
				towers[i].addMouseMotionListener(actionHandler);
			}

			instructions = "Map loaded!";

			repaint(onMapPropertyRectangle);

			if (thread.getState() == Thread.State.NEW) {
				thread.start();
			}
		}
	}
	
	/**
	 * Adding mouseListerner appropriately in order to avoid multiple mouse listeners added to the same object
	 * 
	 * @param typeOfOperation2 Load map or Create map
	 */
	private void addingMouseListener(String typeOfOperation2) {

		if(typeOfOperation2.equals("loadMap")) {			
			if(this.frame.getMouseListeners().length > 0) {
				this.frame.removeMouseListener(this.frame.getMouseListeners()[0]);
			}
			this.frame.addMouseListener(mouseHandler);
		}
		else {
			if(this.frame.getMouseListeners().length > 0) {
				this.frame.removeMouseListener(this.frame.getMouseListeners()[0]);
			}
			this.frame.addMouseMotionListener(mouseHandler);
			this.frame.addMouseListener(mouseHandler);
		}

	}

	/**
	 * This method will take care of saving the file in XML format
	 * 
	 * @throws FileNotFoundException
	 */
	protected void saveMap() throws FileNotFoundException{

		SaveXML saveXML = new SaveXML(this, newFileName);

		saveXML.createXML();				
	}

	/**
	 * This methods is used to print instructions on the screen
	 * 
	 * @param g Graphics object
	 * @param text Instructions to be displayed on the screen
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	private void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}
	private void clearRect(Graphics g) {
		g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
	}
	

	/**
	 * This method is responsible for displaying everything on the screen including the grid, tower store, 
	 * tower characteristics etc.
	 * 
	 * @param g Graphics object
	 */
	@Override
	public void paintComponent(Graphics g)
	{	
		super.paintComponent(g);
		try 
		{
			
			g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

			// Background
			g.setColor(Color.WHITE);

			g.fillRect(0, 0, this.frame.getWidth() - 300, this.frame.getHeight() - 200);		

			// Instructions
			g.setColor(Color.RED);
			g.setFont(new Font("TimesRoman", Font.BOLD, 17));
			g.drawString(welcomeMessage, this.frame.getWidth() - 300 , 50);

			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 14));
			drawString(g,instructions, this.frame.getWidth() - 300 , 70);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.BOLD, 14));
			drawString(g,"Tower Store", (int)this.width * 5 + 80, (frame.getHeight() -(frame.getHeight() -(int)this.height * 10) + 100));
			
						
			// Main screen image
			if(startGame) {
				g.drawImage(image, 50, 0, null);
			}
//			else {
//				g.drawImage(imageWhite, 50, 0, null);
//			}
			

			// Grid			
			g.setColor(Color.GRAY);			
			
			// calculate the dimensions of the tile dynamically by getting the width and height of the screen/frame
			double width1 = this.frame.getWidth()*10000 / (valueOfX * 50);
			double width2 = width1 / 10000;
			double width3 = this.frame.getWidth() / width2;
			width = width3 / valueOfX;

			double height1 = this.frame.getHeight()*10000 / (valueOfY * 50);
			double height2 = height1 / 10000;
			double height3 = this.frame.getHeight() / height2;
			height = height3 / valueOfY;

			// create map based upon the dimensions given by the user			
			for(int row=0; row < map.length; row++) {
				for(int col=0; col < map[0].length; col++) {
					// draw image
					if(map[row][col] == 0) {
						g.drawImage(terrain[map[row][col]], ((int) width + (row * (int) width)), ((int) height + (col * (int) height)) - (int)height , (int) width, (int) height, null);
					}		
					// if the map reads 1 for some array value, it means that is the starting point
					else if(map[row][col] == 1) {
						g.setColor(Color.BLUE);
						//g.drawImage(terrain[map[row][col]], ((int) width + (row * (int) width)), ((int) height + (col * (int) height)) - 50, (int) width, (int) height, null);
						g.fillRect((int) width + (row * (int) width), (col * (int) height), (int) width, (int) height);
					}
					// if the map reads 2 for some array value, it means that is the ending point
					else if(map[row][col] == 2) {
						g.setColor(Color.RED);
						//g.drawImage(terrain[map[row][col]], ((int) width + (row * (int) width)), ((int) height + (col * (int) height)) - 50, (int) width, (int) height, null);
						g.fillRect((int) width + (row * (int) width), (col * (int) height), (int) width, (int) height);
					}
					// if the map reads 3 for some array value, it means that is the path
					else if(map[row][col] == 3) {
						g.setColor(Color.GRAY);
						//g.drawImage(terrain[map[row][col]], ((int) width + (row * (int) width)), ((int) height + (col * (int) height)) - 50, (int) width, (int) height, null);
						g.fillRect((int) width + (row * (int) width), (col * (int) height), (int) width, (int) height);

					}
				}
			}	


			if(MouseHandler.mapCompleted) {
				//Window to display properties of towers on the map 			
				JButton sellTower=new JButton("Sell Tower");
				sellTower.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						user.player.money+=selectedTower.refundRate;
						System.out.println(user.player.money+"\n"+selectedTower.xPosInTowerMap+"\n"+selectedTower.yPosInTowerMap);
						towerMap[selectedTower.xPosInTowerMap][selectedTower.yPosInTowerMap]=null;
						Point location=selectedTower.getLocation();
						onMapTowerPropTbl.setValueAt("", 0, 1);
						onMapTowerPropTbl.setValueAt("", 1, 1);
						onMapTowerPropTbl.setValueAt("", 2, 1);
						onMapTowerPropTbl.setValueAt("", 3, 1);
						onMapTowerPropTbl.setValueAt("", 4, 1);
						onMapTowerPropTbl.setValueAt("", 5, 1);
						onMapTowerPropTbl.setValueAt("", 6, 1);
						
						xPosInGridMap=location.x;
						yPosInGridMap=location.y;
						
						frame.remove(selectedTower);
						//clearRect(g);
						frame.getContentPane().validate();
						//Tower.towerList[selectedTower.id]=null;
					}
				});
				frame.add(sellTower);
				sellTower.setBounds(this.frame.getWidth() - 6*(int)width , 6*(int)height, 2*(int)width, (int)(height/2));
				//sellTower.setBounds(this.frame.getWidth() - 300 , 300, 100, 25);
				
				JButton addAmmunition=new JButton("Add Ammunition");
				addAmmunition.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(user.player.money>=selectedTower.costToAddAmmunition){
							user.player.money-=selectedTower.costToAddAmmunition;
							selectedTower.ammunition+=selectedTower.actualAmmunition;
							onMapTowerPropTbl.setValueAt(selectedTower.ammunition, 1, 1);
						//	Screen.updateOnMapTowerProperty(selectedTower);
						}
						else {
							Object[] options = { "OK" };
							JOptionPane.showOptionDialog(null, "Not Enough Money To Buy Ammunition!!", "Warning",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
							null, options, options[0]);
						}
					}
				});
				frame.add(addAmmunition);
				addAmmunition.setBounds(this.frame.getWidth() - 4*(int)width , 6*(int)height, 3*(int)width, (int)(height/2));
				
				
				frame.add(onMapScrollPane);
				drawString(g,"Active Tower Properties", this.frame.getWidth() - 6*(int)width ,2*(int)height);
				onMapScrollPane.setBounds(this.frame.getWidth() - 6*(int)width , 3*(int)height, 5*(int)width, 3*(int)height);
				
				frame.add(userScrollPane);
				userScrollPane.setBounds(this.frame.getWidth() - 6*(int)width , 7*(int)height, 5*(int)width, (int)height);
				userDataTbl.setValueAt(user.player.money, 0, 1);
				
				//Window to display properties of available towers
				frame.add(offMapScrollPane);
				offMapScrollPane.setBounds(this.frame.getWidth() - 6*(int)width , 10*(int)height, 5*(int)width, 3*(int)height);

			}
			

			// List of available towers	
			for(int i=0;i < 5; i++){
				for(int j=0; j < 1 ; j++) {

					towers[i].setBounds((int)this.width * 5 + (i * 50), (frame.getHeight() -(frame.getHeight() -(int)this.height * 10)) + (int)this.height * (j+1), (int) width, (int) height);
				}
			}

			//Create towers on the grid
			for(int x=0; x<valueOfX; x++)
			{
				for(int y=0; y<valueOfY; y++)
				{
					if(towerMap[x][y] != null)
					{
						final Tower towerOnMapBtn=towerMap[x][y];
						
						this.add(towerOnMapBtn);

						towerOnMapBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								selectedTower=towerOnMapBtn;
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.type, 0, 1);
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.ammunition, 1, 1);
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.range, 2, 1);
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.cost, 3, 1);
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.rateOfFire, 4, 1);
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.refundRate, 5, 1);
								onMapTowerPropTbl.setValueAt(towerOnMapBtn.costToAddAmmunition, 6, 1);
								//Point location = selectedTower.getLocation();
								//xPosInTowerMap=(int)(location.x/(int)(width));
								//yPosInTowerMap=(int)(location.y/(int)(height));
								//System.out.println(xPosInTowerMap+"\n"+yPosInTowerMap);
								
								
								// JOptionPane.showConfirmDialog(null, this, 
								//           "Please Enter Integer Values for X and Y", JOptionPane.OK_CANCEL_OPTION);
							}
						});
						towerOnMapBtn.setBounds(((int)width+x*(int)width), ((int)height+y*(int)height)-(int)this.height, (int)width, (int)height);
					}
				}
			}
			
			
			
			if(this.placingTower){// && Tower.towerList[towerInHand - 1] != null){
				//g.drawRect((int)this.handXPos - (int)(this.width) - (int)(this.width/2), (int)this.handYPos - ((int)this.towerWidth) - ((int)this.height/2), (int)this.width, (int)this.height);
				int ovalWidth = (int)(this.selectedTowerRange*this.width*2);
				int ovalHeight = (int)(this.selectedTowerRange*this.height*2);
				g.setColor(this.selectedTowerColor);
				//g.fillRect((int)this.handXPos - (int)(this.width/2), (int)this.handYPos - ((int)this.towerWidth) - ((int)this.height/2), (int)this.width, (int)this.height);
				g.drawImage(new ImageIcon(this.towerImgPath).getImage(), (int)this.handXPos - (int)(this.width/2), 
						(int)this.handYPos - ((int)this.towerWidth) - ((int)this.height/2), 
						(int)this.width, (int)this.height,null);
				
				//g.drawOval((int)this.handXPos - ((int)(2*this.towerWidth)), (int)this.handYPos - ((int)(3*this.towerWidth)), );
				g.drawOval((int)this.handXPos - ((int)(ovalWidth/2)), ((int)this.handYPos - ((int)((ovalHeight/2))) - (int)this.height), ovalWidth, ovalHeight);
				g.setColor(new Color(64, 64, 64, 64));
				g.fillOval((int)this.handXPos - ((int)(ovalWidth/2)), ((int)this.handYPos - ((int)((ovalHeight/2))) - (int)this.height) , ovalWidth, ovalHeight);
			}

		} catch (Exception e) {

		}
	}

	/**
	 * Called for creating a thread that would execute separately from the main thread 
	 */
	@Override
	public void run() {	

		long lastFrame = System.currentTimeMillis();

		int frames = 0;
		running = true;
		scene=0;	

		// the map grid would be refreshed every 2 ms so that we don't get the flickering effect
		while(running) {

			frames++;

			if(System.currentTimeMillis()-1000 >= lastFrame){
				frames=0;
				lastFrame=System.currentTimeMillis();
			}

			repaintMapRectangle=new Rectangle((int)width,0,frame.getWidth()-350,getHeight());
			//Rectangle repaintRectangle=new Rectangle(50,0,valueOfX*50,getHeight());
			// to draw stuff all the time on the screen : goes around 2 millions frames per second. Which is of no use.
			repaint(repaintMapRectangle);	

			try {
				Thread.sleep(2);
			} catch (Exception e) {}
		}				
	}

	/**
	 * This method will create a new User object and load the grass image into the array named terrain[]
	 */ 
	public void loadGame() {

		user = new User(this);
		levelFile = new LevelFile(valueOfX, valueOfY);

		ClassLoader cl = this.getClass().getClassLoader();

		// To load the image in the Image array
		for(int y=0; y < 10; y++) {
			for(int x=0; x < 10; x++) {
	
				terrain[x + (y * 10)] = new ImageIcon(cl.getResource(packagename  + "/grass.png")).getImage();
				terrain[x + (y * 10)] = createImage(new FilteredImageSource( terrain[x + (y * 10)].getSource(), new CropImageFilter(x*(int)towerWidth, y*(int)towerHeight, (int)towerWidth, (int)towerHeight)));
			}
		}

		running = true;
	}

	/**
	 *  This method is responsible for loading the XML file to the 2D array
	 *  
	 * @param fileName Name of the file to be loaded
	 * @param typeOfOperation Load map or Create map
	 * @param user User object
	 */

	public void startGame(String fileName, String typeOfOperation, User user) {
		user.createPlayer();
		levelFile.readAndLoadMap(fileName, this, typeOfOperation);		
		this.scene = 1;	// game level 1
	}

	/**
	 * This method is called when user drags a tower and leaves the mouse button to place tower on the map
	 * 
	 * @param xPos x-position of the tile
	 * @param yPos y-position of the tile
	 * @param new_towerText type of tower
	 * @return boolean returns True, if the tower was successfully placed, else, returns False
	 */
	public boolean placeTower(int xPos, int yPos,String new_towerText){
		
		if(xPos > 0 && xPos <= valueOfX && yPos >0 && yPos <= valueOfY){

			xPos -= 1;
			yPos -= 1;
			if(map[xPos][yPos] == 0){
				
				if(towerMap[xPos][yPos] == null){
					if(inHandTower.cost > user.player.money){
						
						//Display popup												
						Object[] options = { "OK" };
						JOptionPane.showOptionDialog(null, "Not Enough Money", "Warning",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
						
						return false;
					}else{
						user.player.money -= inHandTower.cost;
						towerMap[xPos][yPos] = new Tower(towerId,new_towerText);
						towerMap[xPos][yPos].xPosInTowerMap=xPos;
						towerMap[xPos][yPos].yPosInTowerMap=yPos;
						towerId++;
						return true;
					}
				}else{
					Object[] options = { "OK" };
					JOptionPane.showOptionDialog(null, "Tower Already Present At This Position!!", "Warning",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[0]);
					return false;
				}
			}else{
				Object[] options = { "OK" };
				JOptionPane.showOptionDialog(null, "Tower Cannot Be Placed On The Path!!", "Warning",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
				return false;
			}
		}else{
			if(placingTower){
				Object[] options = { "OK" };
				JOptionPane.showOptionDialog(null, "Tower Cannot Be Placed Outside The Map Boundary!!", "Warning",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
			}
			return false;
		}
	}

	/**
	 * This inner class is responsible handling some of the actions triggered by a mouse click, mainly changing the values
	 * of the 2D array
	 * 
	 * @author Team 2
	 *
	 */
	// Class to handle mouse events
	public class MouseHeld {

		boolean mouseDown;
		boolean createPath;
		boolean placecTower;

		// This method gets the coordinates of the mouse pointer on mouse movement
		public void mouseMoved(MouseEvent e) {
			handXPos = e.getXOnScreen();
			handYPos = e.getYOnScreen();
		}

		/**
		 * This method is responsible for setting the value of the array based upon the start, end and path points
		 * 
		 * @param e MouseEvent
		 * @param count To differentiate between start, end and path point
		 * @param boxNumberX Array index for X
		 * @param boxNumberY Array index for Y
		 */
		public void mouseDown(MouseEvent e, int count, int boxNumberX, int boxNumberY) {
			mouseDown = true;

			// 1 means that its the starting point - paint it blue
			if(count == 1) {
				if(map[boxNumberX][boxNumberY] == 0) {
					map[boxNumberX][boxNumberY] = 1;
				}
				else if(map[boxNumberX][boxNumberY] == 1) {
					map[boxNumberX][boxNumberY] = 0;
				}
				instructions = "Please select an end point!!"; 
			}
			// 2 means that its the ending point - paint it red
			else if(count == 2) {
				if(map[boxNumberX][boxNumberY] == 0) {
					map[boxNumberX][boxNumberY] = 2;
				}
				else if(map[boxNumberX][boxNumberY] == 2) {
					map[boxNumberX][boxNumberY] = 0;
				}
				instructions = "Select a path from start to end point!!";
			}
			// 3 means that this will be the path - paint it gray
			else {
				if(map[boxNumberX][boxNumberY] == 0) {
					map[boxNumberX][boxNumberY] = 3;
				}
				else if(map[boxNumberX][boxNumberY] == 3) {
					map[boxNumberX][boxNumberY] = 0;
				}
			}
			repaint(onMapPropertyRectangle);
		}

		// This method is handles the saving of file on map completion
		public String pathCompleted(Screen screen) {

			int userReply = actionHandler.pathCompleted();

			if(userReply == JOptionPane.YES_OPTION) {

				try {
					saveMap();
					instructions = "Map Saved..!!";
					typeOfOperation = "saveMap";
					return "YES";
				} catch (Exception e) {
		
					System.exit(0);
				}
			}
			else {
				return "NO";
			}
			return null;
		}

		// This method makes sure if the map is incomplete before saving it
		public void incompleteMap() {
			actionHandler.pathIncomplete();			
		}
		

	}
	/*public static void updateOnMapTowerProperty(Tower tower){
		
		onMapTowerPropTbl.setValueAt(tower.ammunition, 1, 1);
		onMapTowerPropTbl.setValueAt(tower.range, 2, 1);
		onMapTowerPropTbl.setValueAt(tower.cost, 3, 1);
		onMapTowerPropTbl.setValueAt(tower.rateOfFire, 4, 1);
		onMapTowerPropTbl.setValueAt(tower.refundRate, 5, 1);
		onMapTowerPropTbl.setValueAt(tower.costToAddAmmunition, 6, 1);
	}*/

}

