package graphics;

import java.util.ArrayList;
import java.util.HashMap;
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
	protected VertexArray borderVertexArray;
	
	// Constructor.
	public TileMap(Vector2i size, Vector2f sizeOfTile) {
		this.size = size;
		this.sizeOfTile = sizeOfTile;
		
		this.typeColorMap = new HashMap<Tile.TileType, Color>();
		this.vertexArray = new VertexArray(PrimitiveType.QUADS);
		this.borderVertexArray = new VertexArray(PrimitiveType.LINES);
	}
	
	// Adds type <-> color.
	public void addTypeColor(Tile.TileType type, Color color) {
		this.typeColorMap.put(type, color);
	}
	
	// Sets only one tile type.
	public void setTile(Vector2i position, TileType type) {
		this.tiles.get(position.y).get(position.x).tileType = type;
	}
	
	// Sets the tiles.
	public void setTiles(ArrayList<ArrayList<Tile>> tiles) {
		this.tiles = tiles;
	}
	
	// Updates the tilemap data (not time dependent).
	public void update() {
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
			Vertex leftTop = new Vertex(new Vector2f(j * this.sizeOfTile.x, 0), Color.BLACK);
			Vertex leftBottom = new Vertex(new Vector2f(j * this.sizeOfTile.x, this.size.y * this.sizeOfTile.y), Color.BLACK);
			
			Vertex rightTop = new Vertex(new Vector2f((j + 1) * this.sizeOfTile.x - 1, 0), Color.BLACK);
			Vertex rightBottom = new Vertex(new Vector2f((j + 1) * this.sizeOfTile.x - 1, this.size.y * this.sizeOfTile.y), Color.BLACK);
			
			this.borderVertexArray.add(leftTop);
			this.borderVertexArray.add(leftBottom);
			this.borderVertexArray.add(rightTop);
			this.borderVertexArray.add(rightBottom);
		}
	}

	// Draws the tilemap.
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));

		target.draw(this.vertexArray, newStates);
		target.draw(this.borderVertexArray, newStates);
	}
}
