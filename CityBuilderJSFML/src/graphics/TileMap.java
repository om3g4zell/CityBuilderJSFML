package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import graphics.Tile.TileType;

/**
 * Draws a tilemap.
 */
public class TileMap extends BasicTransformable implements Drawable {
	// Map the tile type with its color.
	protected Map<Tile.TileType, Color> typeColorMap;
	
	// Dimensions of the tilemap.
	protected Vector2i size;
	
	// Size of one tile.
	protected Vector2f sizeOfTile;
	
	// Tiles.
	protected List<ArrayList<Tile>> tiles;
	
	// Vertex array to draw.
	protected VertexArray vertexArray;
	protected VertexArray borderVertexArray;
	
	// Boolean flag to indicate if tiles data has changed.
	protected boolean hasChanged;
	
	/**
	 * Constructor.
	 * @param size : size (in tiles) of the map
	 * @param sizeOfTile : the size of a tile in floating number
	 */
	public TileMap(Vector2i size, Vector2f sizeOfTile) {
		this.size = size;
		this.sizeOfTile = sizeOfTile;
		
		this.typeColorMap = new HashMap<Tile.TileType, Color>();
		this.vertexArray = new VertexArray(PrimitiveType.QUADS);
		this.borderVertexArray = new VertexArray(PrimitiveType.LINES);
	}
	
	/**
	 *  Adds a (type, color) pair.
	 * @param type : the type
	 * @param color : the color to use for this tile
	 */
	public void addTypeColor(Tile.TileType type, Color color) {
		this.typeColorMap.put(type, color);
		
		this.hasChanged = true;
	}
	
	/**
	 * Sets the type of the tile at the given position.
	 * @param position : position of the tile in tiles coordinates
	 * @param type : type of the tile
	 */
	public void setTile(Vector2i position, TileType type) {
		// Only change if modifications.
		if(this.tiles.get(position.y).get(position.x).tileType != type) {
			this.tiles.get(position.y).get(position.x).tileType = type;
			this.hasChanged = true;
		}
	}
	
	/**
	 * Sets the tiles.
	 * @param tiles2 : sets the tile array of the tile map
	 */
	public void setTiles(List<ArrayList<Tile>> tiles2) {
		// Only change if modifications.
		this.tiles = tiles2;
		
		this.hasChanged = true;
	}
	
	/**
	 * Updates the tilemap data (not time dependent).
	 */
	public void update() {
		// No need to update if nothing changed.
		if(!this.hasChanged)
			return;
		
		// Clear the old vertex array.
		this.vertexArray.clear();
		this.borderVertexArray.clear();
		
		// Generate new vertex array.
		for(int i = 0 ; i < this.size.y ; ++i) {
			for(int j = 0 ; j < this.size.x ; ++j) {
				// Get the color associated with this tile's type.
				Tile tile = this.tiles.get(i).get(j);
				Color color = this.typeColorMap.get(tile.getTileType());
				
				// Create the vertices.
				Vertex leftTop = new Vertex(new Vector2f(j * this.sizeOfTile.x, i * this.sizeOfTile.y), color);
				Vertex rightTop = new Vertex(new Vector2f((j + 1) * this.sizeOfTile.x, i * this.sizeOfTile.y), color);
				Vertex rightBottom = new Vertex(new Vector2f((j + 1) * this.sizeOfTile.x, (i + 1) * this.sizeOfTile.y), color);
				Vertex leftBottom = new Vertex(new Vector2f(j * this.sizeOfTile.x, (i + 1) * this.sizeOfTile.y), color);
				
				this.vertexArray.add(leftTop);
				this.vertexArray.add(rightTop);
				this.vertexArray.add(rightBottom);
				this.vertexArray.add(leftBottom);
			}
		}
		
		// Generates the borders.
		for(int i = 0 ; i < this.size.y ; ++i) {
			Vertex leftTop = new Vertex(new Vector2f(0, i * this.sizeOfTile.y), Color.BLACK);
			Vertex rightTop = new Vertex(new Vector2f(this.size.x * this.sizeOfTile.x, i * this.sizeOfTile.y), Color.BLACK);
			
			Vertex rightBottom = new Vertex(new Vector2f(0, (i + 1) * this.sizeOfTile.y - 1), Color.BLACK);
			Vertex leftBottom = new Vertex(new Vector2f(this.size.x * this.sizeOfTile.x, (i + 1) * this.sizeOfTile.y - 1), Color.BLACK);
			
			this.borderVertexArray.add(leftTop);
			this.borderVertexArray.add(rightTop);
			this.borderVertexArray.add(rightBottom);
			this.borderVertexArray.add(leftBottom);
		}
		
		for(int j = 0 ; j < this.size.x ; ++j) {
			Vertex leftTop = new Vertex(new Vector2f(j * this.sizeOfTile.x + 1, 0), Color.BLACK);
			Vertex leftBottom = new Vertex(new Vector2f(j * this.sizeOfTile.x + 1, this.size.y * this.sizeOfTile.y), Color.BLACK);
			
			Vertex rightTop = new Vertex(new Vector2f((j + 1) * this.sizeOfTile.x, 0), Color.BLACK);
			Vertex rightBottom = new Vertex(new Vector2f((j + 1) * this.sizeOfTile.x, this.size.y * this.sizeOfTile.y), Color.BLACK);
			
			this.borderVertexArray.add(leftTop);
			this.borderVertexArray.add(leftBottom);
			this.borderVertexArray.add(rightTop);
			this.borderVertexArray.add(rightBottom);
		}
		
		// We took care of the changements, we are now up to date.
		this.hasChanged = false;
	}

	/**
	 * Draws the tilemap.
	 * 
	 * @param target : the target to draw on
	 * @param states : the states to use draw
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));

		target.draw(this.vertexArray, newStates);
		target.draw(this.borderVertexArray, newStates);
	}
}
