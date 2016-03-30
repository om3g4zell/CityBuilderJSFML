package gui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;

import graphics.FontManager;
import graphics.TextureManager;
import sim.Blueprint;
import graphics.FontManager.FontID;
import world.Zone;
import world.ZoneMap;

public class BlueprintGui extends BasicTransformable implements Drawable {
	
	protected Map<Button, List<List<Zone>>> buttons;
	
	/**
	 * Constructor.
	 * 
	 * @param textures : the texture manager.
	 * @param fonts : the font manager.
	 * @throws IOException 
	 */
	public BlueprintGui(TextureManager textures, FontManager fonts) throws IOException {
		this.buttons = new HashMap<Button, List<List<Zone>>>();
		
		File file = new File("res/blueprint");
		File[] files = file.listFiles();
		
		for(int i = 0 ; i < files.length; i++) {
			String extension = files[i].getPath().split(".")[1];
			String name = files[i].getPath().split(".")[0];
			if(extension.equals("blueprint")) {
				Blueprint bp = new Blueprint("res/blueprint/" + files[i].getPath());
				Button button = new Button(name, new Color(128,128,128), new Color(48, 48, 48), Color.WHITE, Color.WHITE, fonts.get(FontID.BEBAS), 12);
				this.buttons.put(button, bp.getZoneMap());
				button.setPosition(50,  10 + i * 50);
			}
		}
	}
	
	/**
	 * Updates the gui.
	 * 
	 * @param window : window to get mouse position.
	 */
	public void update(RenderWindow window) {
		
	}
	
	/**
	 * Renders the blueprint gui.
	 * 
	 * @param target : the target to render on.
	 * @param states : the states to use while rendering.
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		for(this.buttons.)
	}

}
