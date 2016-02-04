package world;

import java.util.*;

import org.jsfml.system.Vector2i;

/**
 * Common interface for all maps.
 */
public class ResourcesMap {
	
	protected List<List<ResourcesStack>> resources;
	protected Vector2i size;
	
	/**
	 * Constructs the map.
	 * @param size : the size of the resources map in tiles
	 */
	public ResourcesMap(Vector2i size) {
		this.size = size;

		// Instantiates the resources
		this.resources = new  ArrayList<List<ResourcesStack>>();
		
		// Create a large enough two-dimensional array.
		for(int i = 0 ; i < size.y ; ++i) {
			ArrayList<ResourcesStack> row = new ArrayList<ResourcesStack>();
			
			for(int j = 0 ; j < size.x ; ++j) {
				row.add(new ResourcesStack());
			}
			
			this.resources.add(row);
		}
	}
	
	/**
	 * Resets all the resources to 0.
	 */
	public void reset() {
		for(int i = 0 ; i < this.size.y ; ++i) {
			for(int j = 0 ; j < this.size.x ; ++j) {
				getResources(new Vector2i(j, i)).reset();
			}
		}
	}
	
	/**
	 * Returns the resources on the tile.
	 * @param x : the x position in tile coordinates
	 * @param y : the y position in tile coordinates
	 * @return the resources stack at the given position
	 */
	public ResourcesStack getResources(int x, int y) {
		return this.resources.get(y).get(x);
	}
	
	/**
	 * Returns the resources on the tile.
	 * @param position : the position in tile coordinates
	 * @return the resources stack at the given position
	 */
	public ResourcesStack getResources(Vector2i position) {
		return getResources(position.x, position.y);
	}
	
	/**
	 * Sets the resources on the tile.
	 * @param position  : the position in tile coordinates
	 * @param resources : the new resources stack
	 */
	public void setResources(Vector2i position, ResourcesStack resources) {
		// Get the row and modify the row.
		List<ResourcesStack> row = this.resources.get(position.y);
		row.set(position.x, resources);
		
		// Set the row back in the two-dimensional array.
		this.resources.set(position.y, row);
	}
	
	/**
	 * Returns the size of the resources map.
	 * 
	 * @return the size
	 */
	public Vector2i getSize() {
		return this.size;
	}
	
	/**
	 * clone ResourcesMap
	 * @return ResourcesMap : clone of the ResourcesMap
	 */
	public ResourcesMap cloneResourcesMap() {
		ResourcesMap rMap = new ResourcesMap(new Vector2i(this.size.x,this.size.y));
		
		for(int i = 0 ; i < this.size.y ; ++i) {
			for(int j = 0 ; j < this.size.x ; ++j) {
				rMap.setResources(new Vector2i(j,i), getResources(j,i).cloneResourcesStack());
			}
		}
		return rMap;
		
	}
}
