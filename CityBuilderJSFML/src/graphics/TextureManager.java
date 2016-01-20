package graphics;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Texture;

public class TextureManager {
	
	public static enum TextureID {
		PEOPLE_TEXTURE
	}
	
	protected Map<TextureID, Texture> textures;
	
	public TextureManager() {
		this.textures = new HashMap<TextureID, Texture>();
	}
	/**
	 * Load the Texture
	 * @param id : ID of the Texture
	 * @param path : file path of the texture
	 */
	public void load(TextureID id, String path) {
		//Create a Texture instance
		Texture jsfmlLogoTexture = new Texture();

		try {
		    //Try to load the texture from file "jsfml.png"
		    jsfmlLogoTexture.loadFromFile(Paths.get(path));
		   
		} catch(IOException ex) {
		    //Ouch! something went wrong
		    ex.printStackTrace();
		}
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
