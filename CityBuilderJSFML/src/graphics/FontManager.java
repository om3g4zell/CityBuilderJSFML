package graphics;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Font;

public class FontManager {
	
	public static enum FontID {
		VCR_MONO,
		CAVIAR_DREAM
	}
	
	protected Map<FontID, Font> fonts;
	
	/**
	 * Instanciate the font manger and load fonts
	 */
	public FontManager() {
		this.fonts = new HashMap<FontID, Font>();
		this.fonts.put(FontID.VCR_MONO, load("res/font/VCR_MONO.ttf"));
		this.fonts.put(FontID.CAVIAR_DREAM, load("res/font/CaviarDreams.ttf"));
	}
	
	/**
	 * 
	 * @param path : the filepath of the font
	 * @return the font
	 */
	public Font load(String path) {
		Font font = new Font();
		try {
			font.loadFromFile(Paths.get(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return font;
	}
	
	/**
	 * 
	 * @param id : id of the font
	 * @return the font
	 */
	public Font get(FontID id) {
		return fonts.get(id);
	}
}
