package world;

import java.util.*;

import org.jsfml.system.Vector2i;

/*
 * ResourceMap class.
 * Common interface for all maps.
 */
public class ResourcesMap {
	
	protected ArrayList<ArrayList<ResourcesStack>> resources;
	
	// Constructs the map.
	public ResourcesMap(Vector2i size) {
		// Instanciate resources
		this.resources = new  ArrayList<ArrayList<ResourcesStack>>();
		
		// Create a large enough two-dimensional array.
		for(int i = 0 ; i < size.y ; ++i) {
			ArrayList<ResourcesStack> row = new ArrayList<ResourcesStack>();
			
			for(int j = 0 ; j < size.x ; ++j) {
				row.add(new ResourcesStack());
			}
			
			this.resources.add(row);
		}
	}
	
	// Returns the resources on the tile.
	public ResourcesStack getResources(Vector2i index) {
		return this.resources.get(index.y).get(index.x);
	}
	
	// Sets the resources on the tile.
	public void setResources(Vector2i index, ResourcesStack resources) {
		// Get the row and modify the row.
		ArrayList<ResourcesStack> row = this.resources.get(index.y);
		row.set(index.x, resources);
		
		// Set the row back in the two-dimensional array.
		this.resources.set(index.y, row);
	}
}
