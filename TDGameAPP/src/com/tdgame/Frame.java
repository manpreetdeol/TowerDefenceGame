package com.tdgame;


import java.awt.Dimension;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * This is the main class that is going to start the program and display an applet with a menu bar
 * 
 * @author Team 2
 * @version $revision
 *
 */

public class Frame extends JApplet{
	
//	JLabel instructions;
	public static String title = "Tower Defence Game";
	JMenuBar menubar;
	JPanel mapFrame;
	
	JButton instructions;
	JButton menuCreateMap;
	JButton menuLoadMap;
	JButton menuEditMap;
	JButton menuSaveMap;
	public void init() {
		
		File audioFile = new File("../res/gamestart.wav");
		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(audioFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
		} 
		
		
		// to remove the applet default menubar
		java.awt.Frame[] frames = java.awt.Frame.getFrames();
        for (java.awt.Frame frame : frames) {
            frame.setMenuBar(null);
            frame.pack();
        }
		
		// Make a menu bar and provide the user with options to create, load and save a map
		menubar = new JMenuBar();

		instructions = new JButton("Instructions");
        menuCreateMap = new JButton("Create Map");
        menuLoadMap = new JButton("Load Map");
        menuEditMap = new JButton("Edit Map");
        menuSaveMap = new JButton("Save Map");
        
        instructions.addActionListener(new MenuHandler(this));
        menuCreateMap.addActionListener(new MenuHandler(this));        
        menuLoadMap.addActionListener(new MenuHandler(this));
        menuEditMap.addActionListener(new MenuHandler(this));
        menuSaveMap.addActionListener(new MenuHandler(this));
        
        menubar.add(instructions);
        menubar.add(menuCreateMap);
        menubar.add(menuLoadMap);
        menubar.add(menuEditMap);
        menubar.add(menuSaveMap);
                        
        // to set the size of the menu bar
        menubar.setPreferredSize(new Dimension(1000, 29));
        setJMenuBar(menubar);
        this.setPreferredSize(new Dimension(getHeight(), getWidth())); 

        this.getContentPane().setVisible(true);	
	}
	
}
