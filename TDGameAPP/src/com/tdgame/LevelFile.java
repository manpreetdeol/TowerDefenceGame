package com.tdgame;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * This class has been created for future use for managing different levels of the game keeping in check the design
 * of the application. As of now, we are reading the file. 
 * 
 * @author Team 2
 * @version $revision
 */

public class LevelFile {

	int valueX;
	int valueY;
	
	LevelFile(int x, int y) {
		this.valueX = x;
		this.valueY = y;
	}
	
	FileInputStream file;
	InputStreamReader reader;
	
	Scanner scanner;
	
	// this method is called to read and load the map
	public void readAndLoadMap(String fileName, Screen screen, String typeOfOperation) {
		
		ReadXML xml = new ReadXML(valueX, valueY, screen, fileName);
			
			// array with the size of the map
			screen.map = new int[valueX][valueY];
			
			// call the methods based upon the type of operations
			if(typeOfOperation.equals("createMap")) {
				xml.readXML();
			} else if(typeOfOperation.equals("loadMap")) {
				xml.loadXML();
			}
			
	}
}
