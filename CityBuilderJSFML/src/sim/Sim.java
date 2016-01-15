package sim;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.VideoMode;

import graphics.Tile.TileType;
import graphics.Tile;
import graphics.TileMap;

/*
 * Simulation class.
 * Contains init, update and render.
 */
public class Sim {
	// Constants.
	protected static final Vector2i TILEMAP_SIZE = new Vector2i(12, 8);
	protected static final Vector2f TILE_SIZE = new Vector2f(32.f, 32.f);
	
	// Attributes.
	protected RenderWindow window;
	protected TileMap tilemap;
	protected ArrayList<ArrayList<Tile>> tiles;
	
	// Constructor
	public Sim(int width, int height, String title) {
		this.window = new RenderWindow(new VideoMode(width, height), title);
	}
	
	// Initialization
	public void init() {
		// Init tiles array.
		this.tiles = new ArrayList<ArrayList<Tile>>();
		
		for(int i = 0 ; i < TILEMAP_SIZE.y ; ++i) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			
			for(int j = 0 ; j < TILEMAP_SIZE.x ; ++j) {
				row.add(new Tile(TileType.TERRAIN_GRASS, new Vector2i(j, i)));
			}
			
			this.tiles.add(row);
		}
		
		// Init tilemap.
		// Only support TERRAIN_GRASS for now.
		this.tilemap = new TileMap(TILEMAP_SIZE, TILE_SIZE);
		this.tilemap.addTypeColor(TileType.TERRAIN_GRASS, new Color(0, 70, 0));
		this.tilemap.setTiles(this.tiles);
	}
	
	// Updates all the simulation.
	public void update(Time dt) {
		// Update the tilemap.
		tilemap.update();
	}
	
	// Renders all the simulation.
	public void render() {
		this.window.clear(Color.WHITE);
		/////////////

		this.window.draw(tilemap);
		
		/////////////
		this.window.display();
		
	}
	
	// Returns the window.
	public RenderWindow getWindow() {
		return this.window;
	}
}
