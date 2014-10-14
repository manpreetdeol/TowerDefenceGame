package com.tdgame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author Team 2
 *
 * ActionHandler class handles asks the user to enter the dimensions and returns these dimensions back to the Screen class
 * where these dimensions are used to load the map 
 */

public class ActionHandler extends JDialog{

	Screen screen;
	Frame frame;
	List<String> listOfFiles = new ArrayList<String>();
	
	public ActionHandler(Screen screen, Frame frame) {
		this.screen = screen;
		this.frame = frame;
	}	

	// this method is called when user wants to create its own map
	// it prompts the user to enter the dimensions and filename to save the map (not saving yet)
	public String askUserToEnterTheDimensions() {
		
		  boolean wrongInput = true;
		  int result;
		  JTextField xField = new JTextField(5);
	      JTextField yField = new JTextField(5);

	      JPanel panel = new JPanel();
	      panel.add(new JLabel("Max value: x=18 and y=10\n"));
	      panel.add(new JLabel("x:"));
	      panel.add(xField);
	      panel.add(Box.createHorizontalStrut(15)); // a spacer
	      panel.add(new JLabel("y:"));
	      panel.add(yField);

	      while(wrongInput) {
	    	  
	    	  // clear text fields
	    	  
	    	  xField.setText("");
	    	  yField.setText("");
	    	  
	    	  result = JOptionPane.showConfirmDialog(null, panel, 
		               "Please Enter Integer Values for X and Y", JOptionPane.OK_CANCEL_OPTION);
		      
		      if (result == JOptionPane.OK_OPTION) {
		    	  
		    	  if(!(xField.getText().isEmpty() || yField.getText().isEmpty() || (xField.getText().isEmpty() && xField.getText().isEmpty()))) {
		    		  
		    		  if(Integer.parseInt(xField.getText()) <= 18 && Integer.parseInt(yField.getText()) <= 10) {
		    					    		  
			    		  try {							
								  
								return xField.getText() +" "+yField.getText();
							
							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
		    		  }
		    	  }
		    	  
		        
		      }
	      }      
	      return null;
	}

	public String saveMapByName() {
		
		String newFileName="";
		JPanel panel = new JPanel();
		
		JTextField Field = new JTextField(10);
	    panel.add(new JLabel("Save Map"));
	    panel.add(new JLabel("with Filename: "));
	    panel.add(Field);
	    
		int result = JOptionPane.showConfirmDialog(null, panel, 
	               "Save the Map with file name as: ", JOptionPane.OK_CANCEL_OPTION);
		
		 if (result == JOptionPane.OK_OPTION) {	    	  
	    	  newFileName = Field.getText();
		 }
		
		return newFileName;
	}	
	
	// If path is complete, ask if the user wants to save it
	public int pathCompleted() {
		
		int input = JOptionPane.showOptionDialog(null, "Would you like to save this map?" , "Map Completed..!!" , 0, 3, null, null, null);
		System.out.println("input "+ input);
		
		return input;
	}
	
	// Load existing maps and ask user as which map to load
	public String loadExistingMap() {
		
		File[] tempList= getFileList();		
		    
	    File input = (File) JOptionPane.showInputDialog(null,"Which map would you like to play?","Load Map",
	    		JOptionPane.QUESTION_MESSAGE,null,tempList,tempList[0]);
	    
	    System.out.println("user choice "+ input.toString());
	    
	    return (input.toString()).split("\\\\")[1];
	}

	// give the list of the map files
	private File[] getFileList() {

		File folder = new File("level/");
		File[] tempList = folder.listFiles();
		System.out.println(folder.exists());

	    for (int i = 0; i < tempList.length; i++) {
	      if (tempList[i].isFile()) {
	    	  System.out.println("File " + tempList[i].getName());
	    	  listOfFiles.add(tempList[i].getName());
	      } else if (tempList[i].isDirectory()) {
	    	  System.out.println("Directory " + tempList[i].getName());
	    	  listOfFiles.add(tempList[i].getName());
	      }
	    }
		return tempList;
		
	}
	
	public void pathIncomplete() {
		JOptionPane.showMessageDialog(frame, "Please complete the path in order to save the map!");;
	}
	
}
