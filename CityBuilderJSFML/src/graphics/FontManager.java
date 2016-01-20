package graphics;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Font;

public class FontManager {
	
	public static enum FontID {
		BASIC
	}
	
	protected Map<FontID, Font> fonts;
	
	public FontManager() {
		this.fonts = new HashMap<FontID, Font>();
		this.fonts.put(FontID.BASIC, load("res/basic.ttf"));
	}
	
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
	
	public Font get(FontID id) {
		return fonts.get(id);
	}
}
