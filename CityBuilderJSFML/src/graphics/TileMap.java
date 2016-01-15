package graphics;

import java.util.ArrayList;
import java.util.Map;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/*
 * TileMap class.
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
	protected ArrayList<ArrayList<Tile>> tiles;
	
	// Vertex array to draw.
	protected VertexArray vertexArray;
	
	// Constructor.
	public TileMap(Vector2i size, Vector2f sizeOfTile) {
		this.sizeOfTile = sizeOfTile;
	}
	
	// Sets the tiles.
	public void setTiles(ArrayList<ArrayList<Tile>> tiles) {
		this.tiles = tiles;
	}
	
	// Updates the tilemap data (not time dependent).
	public void update() {
		
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		states.transform *= this.getTransform();
		target.draw(this.vertexArray, states);
	}
	
	
}
