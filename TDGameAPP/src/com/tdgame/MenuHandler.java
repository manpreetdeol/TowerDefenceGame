package com.tdgame;


import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * MenuHandler class handles the Menu events and takes appropriate action based upon the user choice
 * 
 * @author Team 2
 * @version $revision
 */

public class MenuHandler implements ActionListener{
	
	Screen screen;
	Frame frame;
	JButton myButton; 
	
	MenuHandler() {}
	
	MenuHandler(Frame frame) { 

		this.frame = frame;		
		screen = new Screen(frame); 
		this.frame.getContentPane().add(screen); //adding screen to the frame
		this.frame.getContentPane().validate(); //used to validate the screen that calls the paintComponent for JPanel validation.
	}
	

	// this method catches the menu events and handles them accordingly
	@Override
	public void actionPerformed(ActionEvent e) {

		ActionHandler actionHandler = new ActionHandler();
		MouseHandler mouseHandler = new MouseHandler(screen);
		
		myButton = (JButton) e.getSource(); 
	    
		String selectedOption = myButton.getText();
		
		//checking the option clicked by the user and performing actions on its basis
		if(selectedOption.equalsIgnoreCase("Create Map")) {
			Screen.startGame = false;
			MouseHandler.count=0;
			screen.createMap();
		} else if(selectedOption.equalsIgnoreCase("Load Map")) {
			Screen.startGame = false;
			screen.loadMap();
		} else if(selectedOption.equalsIgnoreCase("Save Map")) {
			mouseHandler.saveMapByMenu();
		} else if(selectedOption.equalsIgnoreCase("Instructions")) {
			actionHandler.menuInstructions();
		} else if(selectedOption.equalsIgnoreCase("Edit Map")) {
			actionHandler.editMap();
			MouseHandler.saveEditedMap = true;
			mouseHandler.mapReadyForEditing();
		}
		
//		screen.repaint();
		
		myButton.setSelected(false);
		
	}

}
