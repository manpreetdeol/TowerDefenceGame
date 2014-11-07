package com.tdgame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class asks the user to enter different inputs in the form of warning, message, questions etc. dialog boxes 
 * and takes appropriate action based upon those inputs
 * 
 * @author Team 2
 * version $revision
 * 
 */
public class ActionHandler extends JDialog implements ActionListener, MouseMotionListener, MouseListener{

	/* Common variables */
	Screen screen;
	Frame frame;
	MouseEvent me;
	MouseHandler mouseHandler;
	private Screen.MouseHeld  mouseButtonHandler;	
	List<String> listOfFiles = new ArrayList<String>();
	
	/* Instruction variables */
	String instructionDialog = "In order to play this game, you need a map.\n\nYou can either create your "
			+ "own map by selecting CREATE MAP from the menu \nor you can load an already existing map by selecting"
			+ " LOAD MAP from the menu. \n\nIf you want to create a map, you need to save the map before you can \nstart playing. "
			+ "\n\nOnce the map is saved, you can buy towers from the tower store and place them"
			+ "\non the map.";
			
	
	public ActionHandler() {}	

	public ActionHandler(Screen screen, Frame frame) {
		this.screen = screen;
		this.frame = frame;
		this.mouseButtonHandler = this.screen.new MouseHeld();
		
	}	

	/**
	 * This method is called when user wants to create its own map. It prompts the user to enter the dimensions and filename to save the map
	 * 
	 * @return xField_yField dimensions of the map
	 */
	public String askUserToEnterTheDimensions() {

		boolean wrongInput = true;
		int result;
		JTextField xField = new JTextField(5);
		JTextField yField = new JTextField(5);

		JPanel panel = new JPanel();
		panel.add(new JLabel("Max value: x=18 and y=10\n"));
		panel.add(new JLabel("x:"));
		panel.add(xField); //adding text field to panel
		panel.add(Box.createHorizontalStrut(15)); // a spacer
		panel.add(new JLabel("y:"));
		panel.add(yField);

		while(wrongInput) {

			// clear text fields
			xField.setText("");
			yField.setText("");

			// Dialog box to enter map dimensions
			result = JOptionPane.showConfirmDialog(null, panel, 
					"Please Enter Integer Values for X and Y", JOptionPane.OK_CANCEL_OPTION);
			
			if (result == JOptionPane.OK_OPTION) {

				if(!(xField.getText().isEmpty() || yField.getText().isEmpty() || (xField.getText().isEmpty() && xField.getText().isEmpty()))) {


					try {	
						//parsing the input and validating the values as per rules
						if(Integer.parseInt(xField.getText()) <= 18 && Integer.parseInt(yField.getText()) <= 10) {
							return xField.getText() +" "+yField.getText();

						} 
						else {
							//incase of invalid input, invalidInput() would be called
							invalidInput();
							screen.instructions = "Invalid input. Please try again";
						}
					}catch (NumberFormatException e) {
						invalidInput();
						screen.instructions = "Invalid input. Please try again";
					}

				}
				else {
					invalidInput();
					screen.instructions = "Invalid input. Please try again";
				}


			}
			else {
				//setting the flag to false to ask the user again for the input incase of incorrect input in the first attempt
				wrongInput = false;
			}

		}      
		return null;
	}

	/**
	 * To notify the user of an invalid input
	 */
	private void invalidInput() {
		JOptionPane.showConfirmDialog(null, "Please enter a valid input!", "Invalid Input..!", 
				JOptionPane.OK_CANCEL_OPTION, 2, null);		
	}

	/**
	 * To display the game instructions
	 */
	public void menuInstructions() {
		JOptionPane.showMessageDialog(null, instructionDialog);
	}

	/**
	 * Ask the user to enter a valid filename
	 * 
	 * @return newFileName Name of the file to be saved
	 */
	public String saveMapByName() {

		String newFileName="";
		JPanel panel = new JPanel();

		JTextField Field = new JTextField(10);
		panel.add(new JLabel("Save Map"));
		panel.add(new JLabel("with Filename: "));
		panel.add(Field);

		int result=0;
		boolean fileExists = true;
		
		//flag inside a loop to check if the file already exisits
		while(fileExists) {
			result = JOptionPane.showConfirmDialog(null, panel, 
					"Save the Map with file name as: ", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION) {

				newFileName = Field.getText();

				if(newFileName.length() > 0) {		    		  

					File[] fileList= getFileList();

					for(int i=0; i < fileList.length; i++) {

						if(fileList[i].getName().toString().equalsIgnoreCase(newFileName+ ".xml")) {
							JOptionPane.showConfirmDialog(null, "Please choose another name.", "File already exists!", JOptionPane.OK_CANCEL_OPTION, 2, null);
							fileExists = true; //setting the flag to true as file already exists
							break;
						}
						else {
							fileExists = false; //when false it will save the file with the name user provided.
						}
					}
				}
				else {
					JOptionPane.showConfirmDialog(null, "Please choose a valid name.", "Invalid filename!", JOptionPane.OK_CANCEL_OPTION, 2, null);
				}

			}
			else {
				JOptionPane.showConfirmDialog(null, "Unless you assign a name, you can't create a map.", "Invalid filename!", JOptionPane.OK_CANCEL_OPTION, 2, null);
			}
		}		

		return newFileName; //return the filename for creating the file.
	}	
	
	/**
	 * Ask user for his/her choice to load the map
	 * 
	 * @return input Name of the file to be loaded
	 */
	public String loadExistingMap() {

		File[] tempList= getFileList();	//getting the list of the existing maps	

		File input = (File) JOptionPane.showInputDialog(null,"Which map would you like to play?","Load Map",
				JOptionPane.QUESTION_MESSAGE,null,tempList,tempList[0]);

		if (input != null) {

			return (input.toString()).split("\\\\")[2]; 
		}    

		return null;		   
	}

	// If path is complete, ask if the user wants to save it
	public int pathCompleted() {

		int input = JOptionPane.showOptionDialog(null, "Would you like to save this map?" , "Map Completed..!!" , 0, 3, null, null, null);

		return input;
	}
	
	// give the list of the map files
	public File[] getFileList() {

		File folder = new File("../level/"); //path where maps are stored
		File[] tempList = folder.listFiles();

		//loop to print all the available files
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				listOfFiles.add(tempList[i].getName());
			} else if (tempList[i].isDirectory()) {
				listOfFiles.add(tempList[i].getName());
			}
		}
		return tempList;		
	}

	public void pathIncomplete() {
		JOptionPane.showMessageDialog(frame, "Please complete the path in order to save the map!"); //Show dialog incase user try to save incomplete map
	}

	// Method for editing the map once loaded
	public void editMap() {
		JOptionPane.showMessageDialog(frame, "For editing the map, you can right click on the last tile"
				+ " of the path \n and keep on back tracking till the point you want. Once there, you can"
				+ "\nstart creating the path by using left click of your mouse and proceed \ntowards the end"
				+ " point. "
				+ "\n\nYou can't change the start or end point. In order to do that, you would have to "
				+ "\ncreate a new map instead."
				+ "\n\nPlease go ahead and start editing the map.");		
	}

	// method to handle actions based upon events generated by button
	@Override
	public void actionPerformed(ActionEvent e) {
//		screen.selectedTower=(Tower) e.getSource();
//
//		screen.towerType=screen.selectedTower.getText();
		
		//this.screen.placingTower = true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	// method to handle actions based upon events generated by mouse press	
	//mousePressed action is triggered to set values of tower properties 
	@Override
	public void mousePressed(MouseEvent e) { 
		Tower temp = (Tower) e.getSource();
		screen.towerType=temp.getText();
		screen.offMapTowerPropTbl.setValueAt(temp.type, 0, 1);
		screen.offMapTowerPropTbl.setValueAt(temp.ammunition, 1, 1);
		screen.offMapTowerPropTbl.setValueAt(temp.range, 2, 1);
		screen.offMapTowerPropTbl.setValueAt(temp.cost, 3, 1);
		screen.offMapTowerPropTbl.setValueAt(temp.rateOfFire, 4, 1);
		screen.offMapTowerPropTbl.setValueAt(temp.refundRate, 5, 1);
		screen.offMapTowerPropTbl.setValueAt(temp.costToAddAmmunition, 6, 1);
		
		screen.selectedTowerColor = temp.getBackground();
		screen.selectedTowerRange = temp.range;
		screen.towerImgPath=temp.imgPath;
		this.mouseButtonHandler.placecTower = true;
		screen.inHandTower=temp;
	}

	// method to handle actions based upon events generated by mouse release
	//mouseReleased action for placing tower on the map
	@Override
	public void mouseReleased(MouseEvent e) {
		int x = (int)(e.getXOnScreen())/50;
		int y = (int)(e.getYOnScreen())/50 ;
		screen.towerInHand = 1;
		screen.placeTower(x, y,screen.towerType);
		this.screen.placingTower = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		this.screen.placingTower = true;
		this.screen.handXPos = e.getXOnScreen();
		this.screen.handYPos = e.getYOnScreen();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
