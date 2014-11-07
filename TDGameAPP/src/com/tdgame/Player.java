package com.tdgame;

/**
 * 
 * This class is calling methods of User class for setting user properties.
 * It will also be used in the next builds.
 * 
 * @author Team2
 * @version $revision 
 *
 */
public class Player{

	public int health;
	public int money;
	
	
	public Player(User user) {
		this.money = user.startingCash; 
		this.health = user.startingHealth;
	}

}
