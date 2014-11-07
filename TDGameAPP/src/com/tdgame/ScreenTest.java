package com.tdgame;

import static org.junit.Assert.*;

import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;

public class ScreenTest {

	
	Screen screen;
	ReadXML readXML;
	String rows_cols;
	String filename;
	int result;
	int valueOfX;
	int valueOfY;
	
	/**
	 * Setting up context to be used by all the test methods
	 */
	
	@Before
	public void setContext()
	{
		screen = new Screen(null);
		readXML = new ReadXML();
		filename = "testcase.xml";
		rows_cols = readXML.getLengthOfExistingMap(filename);
		
		valueOfX = Integer.parseInt(rows_cols.split("_")[1]);
		valueOfY = Integer.parseInt(rows_cols.split("_")[0]);
		
		LevelFile level_file = new LevelFile(valueOfX, valueOfY);
		level_file.readAndLoadMap(filename, screen, "loadMap");
		result = 0;
		
	}
	
	/**
	 * Testing the start point on the basis of its value.
	 * 
	 * If the value of start point is found in the screen map, two-dimentional array,
	 * then increment the result counter.
	 * 
	 * In the end we are checking that if we have the value of 1 just once then
	 * we have one start point that makes the test case true.
	 */
	
	@Test
	public void testStartPoint() 
	{
		for(int i=0;i<screen.map.length;i++){
			for(int j=0;j<screen.map[0].length;j++)
			{
				if(screen.map[i][j] == 1)
				{
					result++;
				}
			}
		}
		assertEquals(1, result);
	}
	
	/**
	 * Testing the stop point on the basis of its value.
	 * 
	 * If the value of stop point is found in the screen map, two-dimentional array,
	 * then increment the result counter.
	 * 
	 * In the end we are checking that if we have the value of 2 just once then
	 * we have one stop point that makes the test case true.
	 */
	
	@Test
	public void testStopPoint() 
	{
		for(int i=0;i<screen.map.length;i++){
			for(int j=0;j<screen.map[0].length;j++)
			{
				if(screen.map[i][j] == 2)
				{
					result++;
				}
			}
		}
		
		assertEquals(1, result);
	}
	
	/**
	 * Testing the path on the basis of its value i.e. 3
	 * 
	 * If the value of path is found in the screen map, two-dimentional array,
	 * then set the value of result to true.
	 */
	
	@Test
	public void testPath() 
	{
		boolean result = false;
		for(int i=0;i<screen.map.length;i++){
			for(int j=0;j<screen.map[0].length;j++)
			{
				if(screen.map[i][j] == 3)
				{
					result = true;
				}
			}
		}
		
		assertTrue(result);
	}
	
	/**
	 * Testing if the tower is placed on the right location
	 */
	public void testPlacingTower()
	{
		Tower[][] towermap = new Tower[10][10];
		towermap[0][0] = new Tower(1, "Laser");
		assertTrue(screen.placeTower(0, 0, "Laser"));
		
	}
}
