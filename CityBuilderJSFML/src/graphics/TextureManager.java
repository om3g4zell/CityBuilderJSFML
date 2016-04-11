package graphics;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Texture;

public class TextureManager {
	
	public static enum TextureID {
		PEOPLE_TEXTURE,
		MONEY_TEXTURE,
		TILE_CURSOR_TEXTURE,
		GROSSERY_STORE_TEXTURE,
		BUILDINGS_TEXTURE,
		CHECKBOX_TEXTURE,
		PAUSE,
	}
	
	protected Map<TextureID, Texture> textures;
	
	public TextureManager() {
		this.textures = new HashMap<TextureID, Texture>();
		this.textures.put(TextureID.PEOPLE_TEXTURE, load("res/people.png"));
		this.textures.put(TextureID.MONEY_TEXTURE, load("res/billets.32.png"));
		this.textures.put(TextureID.TILE_CURSOR_TEXTURE, load("res/hoverframe.png"));
		this.textures.put(TextureID.GROSSERY_STORE_TEXTURE, load("res/grocery_store.64x32.png"));
		this.textures.put(TextureID.BUILDINGS_TEXTURE, load("res/buildings.png"));
		this.textures.put(TextureID.CHECKBOX_TEXTURE, load("res/checkbox.png"));
		this.textures.put(TextureID.PAUSE, load("res/pause.png"));
	}
	/**
	 * Load the Texture
	 * @param path : file path of the texture
	 * @return texture : return the texture
	 */
	public Texture load(String path) {
		//Create a Texture instance
		Texture jsfmlLogoTexture = new Texture();

		try {
		    //Try to load the texture from file "jsfml.png"
		    jsfmlLogoTexture.loadFromFile(Paths.get(path));
		   
		} catch(IOException ex) {
		    //Ouch! something went wrong
		    ex.printStackTrace();
		}
		return jsfmlLogoTexture;
	}
	/**
	 *
	 * @param id
	 * @return the texture associated with the id
	 */
	public Texture get(TextureID id) {
		return this.textures.get(id);
	}
}
