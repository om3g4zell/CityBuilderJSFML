package world;

import java.util.ArrayList;

import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

/*
 * Represent a building
 */
public class Building {
	
	// Building Type
	public static enum BuildingType {
		HOUSE,
		GROCERY_STORE,
		ROAD,
		GENERATOR
	}
	
	// Attributs
	protected ArrayList<Need> needs;
	protected int range;
	protected IntRect hitbox;
	protected BuildingType type;
	
	// Constructor
	public Building(BuildingType type, Vector2i position) {
		this.type = type;
		
		switch(this.type) {
			case GENERATOR:this.range = 18;
				this.hitbox = new IntRect(position.x,position.y,1,1);
				break;
			case GROCERY_STORE:this.range = 28;
			this.hitbox = new IntRect(position.x,position.y,8,4);
				break;
			case HOUSE:this.range = 99;
			this.hitbox = new IntRect(position.x,position.y,2,2);
				break;
			case ROAD:this.range = 1;
			this.hitbox = new IntRect(position.x,position.y,1,1);
				break;
		}
	}
	
	// Generates resources.
	public void generateResources(ResourceMap resourcesMap) {
		
	}
}
