package com.tdgame;


import java.awt.Font;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.omg.CORBA.FREE_MEM;

/**
 * 
 * @author Team 2
 * 
 * This is the main class that is going to start the program and display an applet with a menu bar
 *
 */

public class Frame extends JApplet{
	
//	JLabel instructions;
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
        
       // this.setLayout(null);       
        
        // to display a label on the first applet screen
//        instructions = new JLabel();
//        instructions.setText("Please choose an option from Menu bar.");
////        instructions.setBounds(10, 10, 1000, 20);        
//        this.add(instructions);
		
		setVisible(true);		
		
	}
	
	

}
