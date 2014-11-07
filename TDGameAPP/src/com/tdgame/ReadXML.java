package com.tdgame;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class contains the code to parse an XML file and load it into the 2D array
 * 
 * @author Team 2
 * @version $revision
 *
 */

public class ReadXML {

	Screen screen;
	int valueX, valueY;
	String fileName="";
	
	public ReadXML(){}
		
	public ReadXML(int valueX, int valueY, Screen screen, String fileName) {
		this.valueX = valueX;
		this.valueY = valueY;
		this.screen = screen;
		this.fileName = fileName;
	}
	
	// this method gets the length of existing map in order to load it from the Base.XML file
		public String getLengthOfExistingMap(String newFileName) {
			
			int numberOfCols = 0;
			int numberOfRows = 0;
			
			try {
				File xmlFile = new File("../level/" + newFileName); //specifying the path to read the files 
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory
						.newInstance(); 
				DocumentBuilder documentBuilder = documentFactory
						.newDocumentBuilder();
				Document doc = documentBuilder.parse(xmlFile);

				doc.getDocumentElement().normalize();
				NodeList nodeList = doc.getElementsByTagName("Row");
				
				//reading the file for getting the number of Cols and Rows
				for (int row = 0; row < nodeList.getLength(); row++) {
					Node node = nodeList.item(row);

					NodeList subList = node.getChildNodes();
					
					numberOfCols = subList.getLength();
					break;
				}
				
				numberOfRows = nodeList.getLength(); 
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return numberOfRows+ "_" +numberOfCols;
			
		}
		
	// this methods reads the Base.XML file when the user wants to create a new map
	public void readXML() {
		Node tile;
		int x, y, value;

		try {

			File xmlFile = new File("../level/" + fileName); //reading Base XML
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize(); 
			NodeList nodeList = doc.getElementsByTagName("Row");

			//read the file for getting the x and y coordinates of the base xml file
			for (int row = 0; row < valueY; row++) {
				Node node = nodeList.item(row);

				NodeList subList = node.getChildNodes();

				for (int cols = 0; cols < valueX * 2; cols++) {

					tile = subList.item(cols);

					if (tile.getNodeType() == Node.ELEMENT_NODE) {
					
						Element col = (Element) tile;
						
						x = Integer.parseInt(col.getAttribute("x"));
						y = Integer.parseInt(col.getAttribute("y"));
						value = Integer.parseInt(col.getAttribute("value"));
												
						screen.map[y][row] = value;
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// this method loads an existing XML file into the 2D array
	public void loadXML() {
		Node tile;
		int x, y, value, position;
		MouseHandler.hashMap_of_pathIndex_with_position = new TreeMap<Integer, String>(); 
		
		try {

			File xmlFile = new File("../level/" + fileName);
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("Row");

			for (int row = 0; row < nodeList.getLength(); row++) {
				Node node = nodeList.item(row);

				NodeList subList = node.getChildNodes();

				for (int cols = 0; cols < subList.getLength(); cols++) {

					tile = subList.item(cols);

					if (tile.getNodeType() == Node.ELEMENT_NODE) {
					
						Element col = (Element) tile;
						
						x = Integer.parseInt(col.getAttribute("x"));
						y = Integer.parseInt(col.getAttribute("y"));
						value = Integer.parseInt(col.getAttribute("value"));
						position = Integer.parseInt(col.getAttribute("position"));
						
						if(MouseHandler.largestPositionValue_inMap < position) {
							MouseHandler.largestPositionValue_inMap = position;
						}
						
						
												
						screen.map[y][row] = value;
						
						//setting 1 for the start point
						if(value == 1) { 
							MouseHandler.startPoint_while_loadingMap = y+"_"+row;
						}
						//setting 2 for the end point
						else if(value == 2) {
							MouseHandler.endPoint_while_loadingMap = y+"_"+row;
						}
						//setting 3 for the path
						else if(value == 3) {							
							MouseHandler.hashMap_of_pathIndex_with_position.put(position, y+"_"+row);
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
