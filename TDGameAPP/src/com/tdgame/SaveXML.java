package com.tdgame;

import java.io.File;  
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerException;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult;  

import org.w3c.dom.Attr;  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  

/**
 * This class contains the code to create an XML file and load it into the 2D array for creating a new map
 * 
 * @author Team 2
 * @version $revision
 *
 */
public class SaveXML {  
	
	private Screen screen;
	private String newFileName;

	public SaveXML(Screen screen, String newFileName) {
		this.screen = screen;
		this.newFileName = newFileName;
	}
	
	/**
	 * To create XML file from map array 
	 */
	public void createXML() {
		try {
			int count = 0;
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("Map");
			document.appendChild(rootElement);
			
			for(int k=0; k < this.screen.map[0].length; k++) {
				// define row elements
				Element row = document.createElement("Row");
				rootElement.appendChild(row);
				
				for(int i=0; i < this.screen.map.length; i++) {					
					
					for(int j=k; j < this.screen.map[0].length;) {
						
						count++;
						// tiles
						Element tile = document.createElement("Tile");
						row.appendChild(tile);
						
						// add attributes to tile											
						Attr attribute = document.createAttribute("x");
						attribute.setValue(j+"");
						tile.setAttributeNode(attribute);
						
						attribute = document.createAttribute("y");
						attribute.setValue(i+"");
						tile.setAttributeNode(attribute);
						
						attribute = document.createAttribute("value");
						attribute.setValue(this.screen.map[i][j]+"");
						tile.setAttributeNode(attribute);
												
						attribute = document.createAttribute("position");
						if(MouseHandler.hashMap_of_pathIndex_with_position.containsValue(i+"_"+j)) {

							for (Entry<Integer, String> entry : MouseHandler.hashMap_of_pathIndex_with_position.entrySet()) {
					            if (entry.getValue().equals(i+"_"+j)) {
					                attribute.setValue(entry.getKey()+"");
					                break;
					            }
					        }
						}
						else {
							attribute.setValue(0+"");
						}
						
						tile.setAttributeNode(attribute);
						
						break;
					}
				}
			}			
			
			if(newFileName.contains(".xml")) {
				newFileName = newFileName.split("\\.")[0];
			}
			
			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(
					"../level/"+newFileName+".xml"));

			transformer.transform(domSource, streamResult);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}