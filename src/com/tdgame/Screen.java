package com.tdgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tdgame.MouseHandler;

/**
 * 
 * @author Team 2
 *
 * Screen class is responsible for controlling the life cycle of the application, it contains the method to create,
 * load and save a map based upon the dimensions entered by the user.
 * 
 * IMPORTANT: 1) The images are being read from the bin folder where the .class files are located. So its important to
 * 				 copy/paste the image file from the 'res' folder to that location
 * 
 * 			  2) The width and height of the applet should be set to the screen size manually by going into 'Run Configurations > 
 * 				 Parameters'. This would be changed into a browser driven applet that would display the applet dynamically.
 */
public class Screen extends JPanel implements Runnable{

	Frame frame;
	LevelFile levelFile;
	ActionHandler actionHandler;
	
	Thread thread = new Thread(this);
	private int fps = 0;
	boolean running = false;
	int scene;
	int valueOfX;
	int valueOfY;
	
	double width;
	double height;
	
	public int[][] map;
	public Image[] terrain = new Image[100];
	public Image[] path = new Image[100];
	private String packagename = "com/tdgame";
	
	public int hand = 0;
	public int handXPos = 0;
	public int handYPos = 0;
	private int boxNumberX;
	private int boxNumberY;
	private Graphics g;
	private boolean firstRun = true;
	private String newFileName;
	
	
	public Screen(Frame frame) {
		this.frame = frame;		
		actionHandler = new ActionHandler(this, frame);		
	}	

	// this method is called when the user wants to create a map (only grid is being displayed as of now)
	public void createMap() {
		
		String mapDimensions = actionHandler.askUserToEnterTheDimensions();
		
		String []splitDimensions = mapDimensions.split(" ");
		valueOfX = Integer.parseInt(splitDimensions[0]);
		valueOfY = Integer.parseInt(splitDimensions[1]);
		
		MouseHandler mouseHandler = new MouseHandler(this, valueOfX, valueOfY, true);
		this.frame.addMouseMotionListener(mouseHandler);
		this.frame.addMouseListener(mouseHandler);
				
		newFileName = actionHandler.saveMapByName();
													
		loadGame();
		startGame("Base.xml", "createMap");
		
		thread.start();
					
	}
	
	// this method is called when the user wants to load an existing map
	public void loadMap() {
		
		newFileName = actionHandler.loadExistingMap();
		
		ReadXML readXML = new ReadXML();
		
		// Based upon the file to be loaded, get the number of rows and columns for loading map
		String rows_cols = readXML.getLengthOfExistingMap(newFileName);
				
		valueOfX = Integer.parseInt(rows_cols.split("_")[1]);
		valueOfY = Integer.parseInt(rows_cols.split("_")[0]);
		
		this.frame.addMouseListener(new MouseHandler(this, valueOfX, valueOfY, true));
					
		loadGame();
		startGame(newFileName, "loadMap");
		
		thread.start();
	}
	
	// Save the new file with the user specific name
	protected void saveMap() throws FileNotFoundException{
				
		SaveXML saveXML = new SaveXML(this, newFileName);
		
		saveXML.createXML();
		
//		PrintWriter pw = new PrintWriter("C:\\Users\\NAPSTER\\TowerDefence\\TDGame\\level\\" + newFileName);
//	
//		for(int k=0; k < map.length; k++) {
//			for(int i=0; i < map.length; i++) {
//				for(int j=k; j < map[0].length;) {
//					pw.print(map[i][j] + " ");
//					break;
//				}
//			}
//			pw.print("\r\n");
//		}
//		pw.close();
				
	}
	

	// Called by repaint() method
	@Override
	public void paintComponent(Graphics g) {	
		
			this.g = g;
			g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
				
			// Background
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.frame.getWidth() - 300, this.frame.getHeight() - 200);
			
			// Instructions
			g.setColor(Color.BLACK);
			g.drawString("Welcome to Tower Defence Game", this.frame.getWidth() - 280 , 50);
						
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
					System.out.println((int) height + (col * (int) height));
					// draw image
					if(map[row][col] == 0) {
						g.drawImage(terrain[map[row][col]], ((int) width + (row * (int) width)), ((int) height + (col * (int) height)) - 50, (int) width, (int) height, null);
						System.out.println((int) width + (row * (int) width) +"    "+ (int) height + (col * (int) height));
						System.out.println("width " + width + " Height "+ height);
					}		
					// if the map reads 1 for some array value, it means that is the starting point
					else if(map[row][col] == 1) {
						g.setColor(Color.BLUE);
						g.fillRect(50 + (row * 50), (col * 50), (int) width, (int) height);
					}
					// if the map reads 2 for some array value, it means that is the ending point
					else if(map[row][col] == 2) {
						g.setColor(Color.RED);
						g.fillRect(50 + (row * 50), (col * 50), (int) width, (int) height);
					}
					// if the map reads 3 for some array value, it means that is the path
					else if(map[row][col] == 3) {
						g.setColor(Color.GRAY);
						g.fillRect(50 + (row * 50), (col * 50), (int) width, (int) height);
				
					}
				}
			}	
			
			// Health & Money panel
			g.drawRect(50, this.frame.getHeight() - 200 + 50, 150, 50);
			g.drawRect(50, this.frame.getHeight() - 200 + 50 + 50, 150, 50);
			
			JLabel label = new JLabel("Instructions");
			label.setBounds(250, this.frame.getHeight() - 200 , (int) width, (int) height);
			
			frame.add(label);
			
			// list of towers panel		
			for(int i=0;i < valueOfX - 4; i++) {
				for(int j=0; j < 2 ; j++) {
					g.drawRect(250 + (i * 50), this.frame.getHeight() - 200 + 50 + (j * 50), (int) width, (int) height);
				}
			}

		
		g.drawString(fps+"", 10, 10);
				
		
	}
	
	// a separate thread is started for the map grid
	@Override
	public void run() {	
		
		System.out.println("Success");
		
		long lastFrame = System.currentTimeMillis();
		
		int frames = 0;
		running = true;
		scene=0;	
		
		// the map grid would be refreshed every 2 ms so that we don't get the flickering effect
		while(running) {
			
			frames++;
			
			if(System.currentTimeMillis()-1000 >= lastFrame){
				fps=frames;
				frames=0;
				lastFrame=System.currentTimeMillis();
			}
			
			// to draw stuff all the time on the screen : goes around 2 millions frames per second. Which is of no use.
			repaint();	
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.exit(0);
		
	}
	
	// Load Game for the first time 
	public void loadGame() {
		
//		user = new User(this);
		levelFile = new LevelFile(valueOfX, valueOfY);
		 
		ClassLoader cl = this.getClass().getClassLoader();
				
		// To load the image in the Image array
		 for(int y=0; y < 10; y++) {
			 for(int x=0; x < 10; x++) {
				 System.out.println(cl.getResource(packagename  + "/grass.png"));
				 terrain[x + (y * 10)] = new ImageIcon(cl.getResource(packagename  + "/grass.png")).getImage();
				 terrain[x + (y * 10)] = createImage(new FilteredImageSource( terrain[x + (y * 10)].getSource(), new CropImageFilter(x*50, y*50, 50, 50)));
			 }
		 }
	
		running = true;
	}
	
	// will get the position of the blocks of the grid depending upon the place where the user clicks
	public void placeTower(int x, int y) {
		int xPos = (x - (int) width) / (int) width;
		int yPos = (y - (int) height) / (int) height;
		
		// if the user clicked out of the map
		if(xPos > valueOfX || yPos > valueOfY) {
			
		}
	}
	
	public void startGame(String fileName, String typeOfOperation) {
		
		levelFile.readAndLoadMap(fileName, this, typeOfOperation);		
		this.scene = 1;	// game level 1
	}

	// Class to handle mouse events
	public class MouseHeld {

		boolean mouseDown;
		
		public void mouseMoved(MouseEvent e) {
			handXPos = e.getXOnScreen();
			handYPos = e.getYOnScreen();
		}
		
		public void updateMouse(MouseEvent e) {
			if(scene ==1 ) {
				if(mouseDown && hand == 0) {
					// write something that can make the map based upon user clicks
				}
			}
		}
		
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
			}
			// 2 means that its the ending point - paint it red
			else if(count == 2) {
				if(map[boxNumberX][boxNumberY] == 0) {
					map[boxNumberX][boxNumberY] = 2;
				}
				else if(map[boxNumberX][boxNumberY] == 2) {
					map[boxNumberX][boxNumberY] = 0;
				}
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
						
//			g.drawImage(path[map[boxNumberX][boxNumberY]], (int) width + (boxNumberX * (int) width), (int) height + (boxNumberY * (int) height), (int) width, (int) height, null);
			
		}
		
		public String pathCompleted(Screen screen) {
			
			int userReply = actionHandler.pathCompleted();
			
			if(userReply == JOptionPane.YES_OPTION) {
				
				try {
					screen.saveMap();
					return "YES";
				} catch (Exception e) {}
			}
			else {
				return "NO";
			}
			return null;
		}
		
		public void incompleteMap() {
			actionHandler.pathIncomplete();			
		}
							
	}
	
}

