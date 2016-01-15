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
	protected Vector2i position;
	protected IntRect hitbox;
	protected BuildingType type;
	
	// Constructor
	public Building(BuildingType type, Vector2i position) {
		this.type = type;
		this.position = position;
	}
	
	// Generates resources.
	public void generateResources(ResourceMap resourcesMap) {
		
	}
}
