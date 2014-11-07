package com.tdgame;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ActionHandlerTest {

	int listCount;
	File[] list;
	ActionHandler ac;
	
	@Before
	public void setContext(){
		ac = new ActionHandler();
		list = ac.getFileList();
		listCount = 0;
	}
	
	/**
	 * Checking if the program is loading all the available maps in level folder
	 */
	@Test
	public void testmapListCheck() {
		ActionHandler ac = new ActionHandler();
		File[] list = ac.getFileList();
		listCount = list.length;
		System.out.println(listCount);
		assertEquals(9, listCount);
	}

	@Test
	public void testmapListCheckNotTrue() {
		listCount = list.length;
		assertNotEquals(10, listCount);
	}
}
