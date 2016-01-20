package graphics;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Font;

public class FontManager {
	
	public static enum FontID {
		VCR_MONO
	}
	
	protected Map<FontID, Font> fonts;
	
	public FontManager() {
		this.fonts = new HashMap<FontID, Font>();
		this.fonts.put(FontID.VCR_MONO, load("res/font/VCR_MONO.ttf"));
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
