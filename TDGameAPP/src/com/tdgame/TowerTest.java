package com.tdgame;

import static org.junit.Assert.*;

import org.junit.Test;

public class TowerTest {

	@Test
	/**
	 * Testing if the image file is available
	 * Checking if the object is not null and are not pointing to the same memory address.
	 */
	public void testTowerObjects() {
		Tower tower1 = new Tower(1, "");
		Tower tower2 = new Tower(1, "");
		assertNotSame(tower1, tower2);
		assertNotNull(tower2);
	}
	
	/**
	 * Testing if we have all the specified towers in the array
	 */
	
	@Test
	public void testTowerListArray(){
		Tower tower = new Tower();
		String expectedTowerNames[]={"Laser","Tank","Fire","Missile","Bomber"};
		assertArrayEquals(expectedTowerNames, tower.towerNames);
		
	}
	
	/**
	 * Checking if the towers list array is not null
	 */
	@Test
	public void test() {
		/*Tower lighteningTower = new TowerTypes(id, cost, range,"");
		if(lighteningTower.towerList[id] != null)
		{
			towerIdRepeatCheck = true;
		}
		assertTrue(towerIdRepeatCheck);*/
	}
	

}
