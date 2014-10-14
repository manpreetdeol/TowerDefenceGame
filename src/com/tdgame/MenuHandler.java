package com.tdgame;


import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * 
 * @author Team 2
 *
 * MenuHandler class handles the Menu events and takes appropriate action based upon the user choice
 */

public class MenuHandler implements MenuListener{
	
//	Screen screen;
	Frame frame;
	
	MenuHandler() {	}
	
	MenuHandler(Frame frame) {
		this.frame = frame;
	}
	

	// this method catches the menu events and handles them accordingly
	@Override
	public void menuSelected(MenuEvent e) {
		
		Screen screen = new Screen(frame);
		this.frame.add(screen);
		
//		MouseHandler mouseHandler = new MouseHandler(screen);
		
		JMenu myMenu = (JMenu) e.getSource();
	    
		String selectedOption = myMenu.getText();
		
		if(selectedOption.equalsIgnoreCase("Create Map")) {
			screen.createMap();			
		} else if(selectedOption.equalsIgnoreCase("Load Map")) {
			
		} else if(selectedOption.equalsIgnoreCase("Save Map")) {
			
		}
		
	}

	@Override
	public void menuDeselected(MenuEvent e) {
		System.out.println();
	}

	@Override
	public void menuCanceled(MenuEvent e) {
		
	}

}
