package com.tdgame;

import java.io.File;  

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

public class SaveXML {  
	
	private Screen screen;
	private String newFileName;

	public SaveXML(Screen screen, String newFileName) {
		this.screen = screen;
		this.newFileName = newFileName;
	}
	
	public void createXML() {
		try {

			DocumentBuilderFactory documentFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = documentFactory
					.newDocumentBuilder();

			// define root elements
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement("Map");
			document.appendChild(rootElement);
			
			for(int k=0; k < this.screen.map[0].length; k++) {
				System.out.println(this.screen.map.length);
				System.out.println(this.screen.map[0].length);
				// define row elements
				Element row = document.createElement("Row");
				rootElement.appendChild(row);
				
				for(int i=0; i < this.screen.map.length; i++) {					
					
					for(int j=k; j < this.screen.map[0].length;) {
						
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
						
						break;
					}
				}
			}			
			
			// creating and writing to xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(
					"level/"+newFileName+".xml"));

			transformer.transform(domSource, streamResult);

			System.out.println("File saved to specified path!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}