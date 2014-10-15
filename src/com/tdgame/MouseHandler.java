package com.tdgame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;


public class MouseHandler implements MouseListener, MouseMotionListener{

	private Screen screen;
	private Screen.MouseHeld mouseHeld;
	int count=0;
	int x;
	int y;
	int previousX;
	int previousY;
	boolean createMap;
	boolean startPointDone;
	Collection<String> arrayList_to_hold_occupied_blocks = new ArrayList<String>();
	ArrayList<String> path_completion_detection = new ArrayList<String>();
	ArrayList<String> nextBlock = new ArrayList<String>();
	private boolean mapCompleted = false;
	
	int userSelectionX;
	int userSelectionY;
	int boxNumberX;
	int boxNumberY;
	private String arrayIndex;
	
	public MouseHandler(Screen screen, int x, int y, boolean createMap) {
		this.screen = screen;
		this.x = x;
		this.y = y;
		this.createMap = createMap;
		this.mouseHeld = this.screen.new MouseHeld(); 
	}
	
	public MouseHandler(Screen screen) {
		this.screen = screen;
		this.mouseHeld = this.screen.new MouseHeld(); 
	}

	public void mouseDragged(MouseEvent e) {
		System.out.println("Mouse key released");
		
		if(createMap && !mapCompleted) {
			createOwnMap(e);
		}
	}

	public void mouseMoved(MouseEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse key released");
				
		if(createMap && !mapCompleted) {
			createOwnMap(e);
		}
				
		
	}

	private void createOwnMap(MouseEvent e) {
		
		userSelectionX = e.getXOnScreen();
		userSelectionY = e.getYOnScreen();
		
		System.out.println("userSelectionX "+ userSelectionX + " userSelectionY " + userSelectionY);
//		System.exit(0);
		// get the index number based upon the x - y coordinates of the mouse click
		boxNumberX = (userSelectionX / 50) - 1;
		boxNumberY = (userSelectionY / 50) - 1;
		
		int boxNumberX_plus_1;
		int boxNumberX_minus_1;
		int boxNumberY_plus_1;
		int boxNumberY_minus_1;
				
		
		// hold the string version of array index where mouse was released
		arrayIndex = boxNumberX+""+ boxNumberY;
		
		// this condition is to limit the mouse released detection to the map
		if ((userSelectionX <= (x * 50) + 50 && userSelectionX > 50) && (userSelectionY <= (y * 50) + 50 && userSelectionY > 50)) {				
			System.out.println("arrayIndex" + arrayIndex);
			// count 0 means Starting point, count 1 means ending point
			// this check is to make sure that the starting & ending point are placed on the borders of the map
			if(count == 0 || count == 1) {
				if((boxNumberX == 0 || boxNumberY == 0) || 
						(boxNumberX == x - 1 || boxNumberY == y - 1)) {
					
					// Store the surrounding blocks of finish point in an array to see if the path has been finished
					if(count == 1) {
						
						boxNumberX_plus_1 = boxNumberX + 1;
						boxNumberX_minus_1 = boxNumberX - 1;
						boxNumberY_plus_1 = boxNumberY + 1;
						boxNumberY_minus_1 = boxNumberY - 1;
						
						path_completion_detection.add(boxNumberX_plus_1 +""+boxNumberY);
						path_completion_detection.add(boxNumberX_minus_1 +""+boxNumberY);
						path_completion_detection.add(boxNumberX +""+boxNumberY_plus_1);
						path_completion_detection.add(boxNumberX +""+ boxNumberY_minus_1);
						
						startPointDone = true;
						
					}
					// to decide where exactly the next block should come in
					else if(count == 0) {
						
						boxNumberX_plus_1 = boxNumberX + 1;
						boxNumberX_minus_1 = boxNumberX - 1;
						boxNumberY_plus_1 = boxNumberY + 1;
						boxNumberY_minus_1 = boxNumberY - 1;
						
						nextBlock.add(boxNumberX_plus_1 +""+boxNumberY);
						nextBlock.add(boxNumberX_minus_1 +""+boxNumberY);
						nextBlock.add(boxNumberX +""+boxNumberY_plus_1);
						nextBlock.add(boxNumberX +""+ boxNumberY_minus_1);
					}
					
					count++;
					arrayList_to_hold_occupied_blocks.add(arrayIndex);					
					mouseHeld.mouseDown(e, count, boxNumberX, boxNumberY);
				}
			}
			// when starting & ending points are done, go to else
			else {
				
				System.out.println("arrayIndex" + arrayIndex);
				if(nextBlock.contains(arrayIndex) && !arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
					count++;
					arrayList_to_hold_occupied_blocks.add(arrayIndex);	
					mouseHeld.mouseDown(e, count, boxNumberX, boxNumberY);
					
					boxNumberX_plus_1 = boxNumberX + 1;
					boxNumberX_minus_1 = boxNumberX - 1;
					boxNumberY_plus_1 = boxNumberY + 1;
					boxNumberY_minus_1 = boxNumberY - 1;
					
					nextBlock.clear();
					nextBlock.add(boxNumberX_plus_1 +""+boxNumberY);
					nextBlock.add(boxNumberX_minus_1 +""+boxNumberY);
					nextBlock.add(boxNumberX +""+boxNumberY_plus_1);
					nextBlock.add(boxNumberX +""+ boxNumberY_minus_1);
				}
				
								
				// check if path is completed
				if(path_completion_detection.contains(arrayIndex) && arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
					System.out.println("Your path is completed");
					String userReply = mouseHeld.pathCompleted(screen);
					
					if(userReply.equalsIgnoreCase("YES")) {
						mapCompleted  = true;
					}
					
				}
			}
			
		}
		
	}
	
	public void saveMapByMenu() {
		
		if(path_completion_detection.contains(arrayIndex) && arrayList_to_hold_occupied_blocks.contains(arrayIndex)) {
			System.out.println("Your path is completed");
			String userReply = mouseHeld.pathCompleted(screen);
			
			if(userReply.equalsIgnoreCase("YES")) {
				mapCompleted  = true;
			}
			
		}
		else {
			mouseHeld.incompleteMap();
		}
		
	}
	

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
	
	}
	

}
