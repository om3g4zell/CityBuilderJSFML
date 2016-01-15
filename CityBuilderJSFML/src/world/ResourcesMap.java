package world;

import java.util.*;

import org.jsfml.system.Vector2i;

/*
 * ResourceMap class.
 * Common interface for all maps.
 */
public class ResourcesMap {
	
	protected ArrayList<ArrayList<ResourcesStack>> resources;
	protected Vector2i size;
	
	// Constructs the map.
	public ResourcesMap(Vector2i size) {
		this.size = size;

		// Instantiates the resources
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
	
	// Resets all the resources to 0.
	public void reset() {
		for(int i = 0 ; i < this.size.y ; ++i) {
			for(int j = 0 ; j < this.size.x ; ++j) {
				getResources(new Vector2i(j, i)).reset();
			}
		}
	}
	
	// Returns the resources on the tile.
	public ResourcesStack getResources(Vector2i position) {
		return this.resources.get(position.y).get(position.x);
	}
	
	// Sets the resources on the tile.
	public void setResources(Vector2i position, ResourcesStack resources) {
		// Get the row and modify the row.
		ArrayList<ResourcesStack> row = this.resources.get(position.y);
		row.set(position.x, resources);
		
		// Set the row back in the two-dimensional array.
		this.resources.set(position.y, row);
	}
}
