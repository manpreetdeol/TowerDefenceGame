package com.tdgame;


import javax.swing.JApplet;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * 
 * @author Team 2
 * 
 * This is the main class that is going to start the program and display an applet with a menu bar
 *
 */

public class Frame extends JApplet{
	
	public static String title = "Tower Defence Game";
			
	// In Applet, init() method acts as the main method that would run when we execute the program
	@Override
	public void init() {

		// Make a menu bar and provide the user with options to create, load and save a map
		JMenuBar menubar = new JMenuBar();

        JMenu menuCreateMap = new JMenu("Create Map");
        JMenu menuLoadMap = new JMenu("Load Map");
        JMenu menuSaveMap = new JMenu("Save Map");
        
        menuCreateMap.addMenuListener(new MenuHandler(this));        
        menuLoadMap.addMenuListener(new MenuHandler(this));
        menuSaveMap.addMenuListener(new MenuHandler(this));
        
        menubar.add(menuCreateMap);
        menubar.add(menuLoadMap);
        menubar.add(menuSaveMap);
                        
        setJMenuBar(menubar);
		
		setVisible(true);
		
		
	}
	
	

}
