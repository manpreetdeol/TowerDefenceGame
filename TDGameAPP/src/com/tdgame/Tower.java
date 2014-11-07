package com.tdgame;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This class is responsible for maintaining the tower characteristics like: “Type”, “Ammunition”, “Range”,
 *  “Cost”, Rate of Fire”, “Refund Rate” and “Ammunition Cost”. Each tower has different images and other 
 *  properties like x-y position to determine its position on the map.  
 * 
 * @author Team 2
 * 
 */
public class Tower extends JButton {
	
	public String imageFile = "";
	public String imgPath;

	public Image image;
	//public static Tower[] towerList = new Tower[200];
	//public static Tower lighteningTower = new TowerTypes(0, 10, 2,"").getImageFile("tower2"); //10 is tower cost, default range is 2
	public int id;
	public int cost;
	public int range;
	public int ammunition;
	public String type;
	public int refundRate;
	public int costToAddAmmunition;
	public int rateOfFire;
	public int xPosInTowerMap;
	public int yPosInTowerMap;
	public int actualAmmunition;
	

	
	
	public Color towerColors[]={Color.ORANGE,Color.CYAN,Color.MAGENTA,Color.LIGHT_GRAY,Color.BLACK};
	public String towerNames[]={"Laser","Tank","Fire","Missile","Bomber"};
	
		
	public Tower()
	{
		super();
	}
	public Tower(ImageIcon icon)
	{
		super(icon);
	}
	
	public Tower(int id, String towerType){
		
		switch(towerType){
		case "Fire":
			setTowerProperties(id,100,10000,1,towerType,10,"../res/towers/fire.png");
			setIcon(new ImageIcon(this.imgPath));
			break;
			
		case "Laser":
			setTowerProperties(id,200,1000,1,towerType,5,"../res/towers/laser.png");
			setIcon(new ImageIcon(this.imgPath));
			break;
		case "Bomber":
			setTowerProperties(id,500,100,2,towerType,3,"../res/towers/bomber.png");
			setIcon(new ImageIcon(this.imgPath));
			break;
		case "Tank":
			setTowerProperties(id,800,10,3,towerType,2,"../res/towers/tank.png");
			setIcon(new ImageIcon(this.imgPath));
			break;
		case "Missile":
			setTowerProperties(id,1000,3,3,towerType,1,"../res/towers/missile.png");
			setIcon(new ImageIcon(this.imgPath));
			break;
		default:break;
		}	
	
	}
	
	/**
	 * This method sets the properties of the tower
	 * 
	 * @param id unique identifier for towers
	 * @param cost to buy tower
	 * @param ammunition quantity of bullets available
	 * @param range power range of tower
	 * @param type type of tower
	 * @param rateOfFire rate at which tower fires 
	 * @param path tower image path
	 */
	public void setTowerProperties(int id, int cost,int ammunition,int range,String type,int rateOfFire,String path){
		this.id = id;
		this.ammunition=ammunition;
		this.actualAmmunition=ammunition;
		this.cost = cost;
		this.range = range;
		this.type=type;
		this.refundRate=(int)(cost*25)/100;
		this.rateOfFire=rateOfFire;
		this.costToAddAmmunition=(int)(cost*50)/100;
		this.imgPath=path;
		
		//towerList[id] = this;

	}
	
	public Tower getImageFile(String str){
		this.imageFile = str;
		this.image = new ImageIcon("res/towers/" + this.imageFile + ".png").getImage();
		return this;
	}
}
