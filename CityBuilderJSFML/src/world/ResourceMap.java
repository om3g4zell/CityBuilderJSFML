package world;

import java.util.*;

import org.jsfml.system.Vector2i;

/*
 * ResourceMap class.
 * Common interface for all maps.
 */
public class ResourceMap {
	
	protected ArrayList<ArrayList<ResourceStack>> resources;
	
	// Constructs the map.
	public ResourceMap(Vector2i size) {
		// Create a large enough two-dimensional array.
		for(int i = 0 ; i < size.y ; ++i) {
			ArrayList<ResourceStack> row = new ArrayList<ResourceStack>();
			
			for(int j = 0 ; j < size.x ; ++j) {
				row.add(new ResourceStack());
			}
			
			this.resources.add(row);
		}
	}
	
	// Returns the resources on the tile.
	public ResourceStack getResources(Vector2i index) {
		return this.resources.get(index.y).get(index.x);
	}
	
	// Sets the resources on the tile.
	public void setResources(Vector2i index, ResourceStack resources) {
		// Get the row and modify the row.
		ArrayList<ResourceStack> row = this.resources.get(index.y);
		row.set(index.x, resources);
		
		// Set the row back in the two-dimensional array.
		this.resources.set(index.y, row);
	}
}
