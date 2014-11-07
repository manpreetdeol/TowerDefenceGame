package com.tdgame;

import static org.junit.Assert.*;

import javax.swing.JTextField;

import org.junit.Test;

public class ReadXMLTest {

	/**
	 * Checking if ReadXML object is not null
	 * @throws Exception
	 */
	@Test
	public void testReadXML() throws Exception {
		int valueX = 0;
		int valueY = 0;
		Screen screen = new Screen(new Frame());
		String fileName = "";
		
		ReadXML result = new ReadXML(valueX, valueY, screen, fileName);
		assertNotNull(result);
	}
	
	/**
	 * Checking if the length of the map is not 0
	 * @throws Exception
	 */
	@Test
	public void testGetLengthOfExistingMap() throws Exception {
			ReadXML fixture = new ReadXML(18, 18, new Screen(new Frame()), "");
			String newFileName = "testcase.xml";

			String result = fixture.getLengthOfExistingMap(newFileName);
			assertNotEquals("0_0", result);
		}
}
