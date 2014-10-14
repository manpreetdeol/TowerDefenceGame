package com.tdgame;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Team 2
 *
 * This class contains the code to parse an XML file and load it into the 2D array
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

	// this methods reads the Base.XML file when the user wants to create a new map
	public void readXML() {
		Node tile;
		int x, y, value;

		try {

			File xmlFile = new File("level/" + fileName);
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("Row");

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			for (int row = 0; row < valueY; row++) {
				Node node = nodeList.item(row);

				NodeList subList = node.getChildNodes();
				System.out.println("\nElement type :" + node.getNodeName());

				for (int cols = 0; cols < valueX * 2; cols++) {

					tile = subList.item(cols);

					if (tile.getNodeType() == Node.ELEMENT_NODE) {
					
						Element col = (Element) tile;
						
						x = Integer.parseInt(col.getAttribute("x"));
						y = Integer.parseInt(col.getAttribute("y"));
						value = Integer.parseInt(col.getAttribute("value"));
						
						System.out.println("x : " + col.getAttribute("x"));
						System.out.println("y : " + col.getAttribute("y"));
						System.out.println("value : " + col.getAttribute("value"));
						
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
		int x, y, value;

		try {

			File xmlFile = new File("level/" + fileName);
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("Row");

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			for (int row = 0; row < nodeList.getLength(); row++) {
				Node node = nodeList.item(row);

				NodeList subList = node.getChildNodes();
				System.out.println("\nElement type :" + node.getNodeName());

				for (int cols = 0; cols < subList.getLength(); cols++) {

					tile = subList.item(cols);

					if (tile.getNodeType() == Node.ELEMENT_NODE) {
					
						Element col = (Element) tile;
						
						x = Integer.parseInt(col.getAttribute("x"));
						y = Integer.parseInt(col.getAttribute("y"));
						value = Integer.parseInt(col.getAttribute("value"));
						
						System.out.println("x : " + col.getAttribute("x"));
						System.out.println("y : " + col.getAttribute("y"));
						System.out.println("value : " + col.getAttribute("value"));
						
						screen.map[y][row] = value;

//						
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// this method gets the length of existing map in order to load it from the Base.XML file
	public String getLengthOfExistingMap(String newFileName) {
		
		int numberOfCols = 0;
		int numberOfRows = 0;
		
		try {
			File xmlFile = new File("level/" + newFileName);
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("Row");

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			
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
}
