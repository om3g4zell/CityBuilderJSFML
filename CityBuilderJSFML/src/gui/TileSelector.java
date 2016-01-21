package gui;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

import graphics.TextureManager;
import graphics.TextureManager.TextureID;

/**
 * Cursor to select a tile.
 */
public class TileSelector implements Drawable {
	// Attributes.
	RenderWindow window;
	Sprite cursorSprite;
	Vector2i selectedTile, tilemapSize;
	Vector2f tileSize;
	
	/**
	 * Constructor.
	 * @param window : used to recover exact position of the mouse
	 * @param textures : used to display the cursor
	 */
	public TileSelector(RenderWindow window, TextureManager textures, Vector2i tilemapSize, Vector2f tileSize) {
		this.window = window;
		this.cursorSprite = new Sprite();
		this.cursorSprite.setTexture(textures.get(TextureID.TILE_CURSOR_TEXTURE));
		this.tilemapSize = tilemapSize;
		this.tileSize = tileSize;
	}
	
	/**
	 * Returns the selected tile.
	 * @return the selected tile's coordinates.
	 */
	public Vector2i getSelectedTile() {
		return this.selectedTile;
	}
	
	public void update() {
	    // Get the selected tile.
	    Vector2i rawMousePosition = Mouse.getPosition(this.window);
	    Vector2f mousePosition = window.mapPixelToCoords(rawMousePosition);

	    Vector2i tileIndex = new Vector2i((int)(mousePosition.x / this.tileSize.x), (int)(mousePosition.y / this.tileSize.y));

	    // Make sure the selector is still on the tile map.
	    if(tileIndex.x < 0)
	        tileIndex = new Vector2i(0, tileIndex.y);
	    else if(tileIndex.x >= this.tilemapSize.x)
	        tileIndex = new Vector2i((int)this.tilemapSize.x - 1, tileIndex.y);;

	    if(tileIndex.y < 0)
	        tileIndex = new Vector2i(tileIndex.x, 0);
	    else if(tileIndex.y >= this.tilemapSize.y)
	        tileIndex = new Vector2i(tileIndex.x, (int)this.tilemapSize.y - 1);

	    this.selectedTile = tileIndex;

	    // Move the selector sprite.
	    cursorSprite.setPosition(this.selectedTile.x * this.tileSize.x, this.selectedTile.y * this.tileSize.y);
	}
	
	/**
	 * Draws the cursor.
	 * 
	 * @param target : the target to draw on
	 * @param states : the states to use draw
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		target.draw(this.cursorSprite, states);
	}
}
