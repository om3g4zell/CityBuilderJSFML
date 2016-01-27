package graphics;

import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import world.Zone;
import world.ZoneMap;

public class ZoneMapLayer implements Drawable {
	
	// Map the tile type with its color.
	protected Map<Zone.ZoneClass, Color> typeColorMap;
	
	// Tiles.
	protected ZoneMap zones;
	
	protected Vector2i sizeOfTile;
	
	// Vertex array to draw.
	protected VertexArray vertexArray;
	
	// size of the zone map
	protected Vector2i size;
	
	// Boolean flag to indicate if tiles data has changed.
	protected boolean hasChanged;
		
	public ZoneMapLayer(ZoneMap zones) {
		
		this.zones = zones;
		this.hasChanged = false;
		this.size = zones.getSize();
		this.sizeOfTile = new Vector2i(16 ,16);
		
		this.typeColorMap = new HashMap<Zone.ZoneClass, Color>();
		this.vertexArray = new VertexArray(PrimitiveType.QUADS);
	}
	
	/**
	 * Sets the type of the tile at the given position.
	 * @param position : position of the tile in tiles coordinates
	 * @param type : type of the tile
	 */
	public void setTile(Vector2i position, Zone.ZoneClass type) {
		// Only change if modifications.
		if(this.zones.getZoneMap().get(position.y).get(position.x).getType() != type) {
			this.zones.getZoneMap().get(position.y).get(position.x).setType(type);
			this.hasChanged = true;
		}
	}
	
	public void update() {
		if(!this.hasChanged)
			return;
		
		// Clear the old vertex array.
		this.vertexArray.clear();
		
		// Generate new vertex array.
		for(int i = 0 ; i < this.size.y ; ++i) {
			for(int j = 0 ; j < this.size.x ; ++j) {
				// Get the color associated with this tile's type.
				Zone zone = this.zones.getZoneMap().get(i).get(j);
				Color color = this.typeColorMap.get(zone.getType());
				
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
	}
	
	public void addTypeColor(Zone.ZoneClass type, Color color) {
		this.typeColorMap.put(type, color);
		
		this.hasChanged = true;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {

		target.draw(this.vertexArray, states);
			
	}
}
