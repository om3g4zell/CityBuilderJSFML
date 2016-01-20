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
		this.textures.put(TextureID.PEOPLE_TEXTURE, load("res/people.png"));
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
