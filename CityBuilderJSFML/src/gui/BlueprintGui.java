package gui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;

import graphics.FontManager;
import sim.Blueprint;
import graphics.FontManager.FontID;
import world.ZoneMap;

public class BlueprintGui extends BasicTransformable implements Drawable {
	
	protected Map<Button, String> buttons;
	protected Button saveButton;
	protected String extension = "blueprint";
	protected String folder = "res/blueprint/";
	
	/**
	 * Constructor.
	 * 
	 * @param textures : the texture manager.
	 * @param fonts : the font manager.
	 * @throws IOException 
	 */
	public BlueprintGui(FontManager fonts) throws IOException {
		this.buttons = new HashMap<Button, String>();
		
		this.saveButton = new Button("Save", new Color(128, 128, 128), new Color(48, 48, 48), Color.WHITE, Color.WHITE, fonts.get(FontID.BEBAS), 12);
		this.saveButton.setPosition(10, 100);
		
		File file = new File("res/blueprint");
		File[] files = file.listFiles();
		
		for(int i = 0 ; i < files.length; i++) {
			int dotPlace = files[i].getName().lastIndexOf('.');
			String name = files[i].getName().substring(0, dotPlace);
			String extension = files[i].getName().substring(dotPlace+1,files[i].getName().length());
			
			if(extension.equals(this.extension)) {
				// Create and configure the button.
				Button button = new Button(name, new Color(128, 128, 128), new Color(48, 48, 48), Color.WHITE, Color.WHITE, fonts.get(FontID.BEBAS), 12);
				button.setPosition(200,  50 + i * 50);
				
				// Add it to the list.
				this.buttons.put(button, name);
			}
		}
	}
	
	/**
	 * Updates the gui.
	 * 
	 * @param window : window to get mouse position.
	 */
	public void handleEvent(RenderWindow window, Event event, ZoneMap zoneMap, LogGui log) {
		for(Entry<Button, String> buttonEntry : this.buttons.entrySet()) {
			buttonEntry.getKey().update(new Vector2f(Mouse.getPosition(window).x, Mouse.getPosition(window).y), event);
			
			if(buttonEntry.getKey().isClicked()) {
				try {
					Blueprint bp = new Blueprint(this.folder + buttonEntry.getValue()+ "." + extension);
					zoneMap.setZoneMap(bp.getZoneMap());
					
					log.write("Succesfully load " + buttonEntry.getValue(), LogGui.SUCCESS);
				}
				catch(IOException exception) {
					exception.printStackTrace();
				}
			}
		}
		
		this.saveButton.update(new Vector2f(Mouse.getPosition(window).x, Mouse.getPosition(window).y), event);
		
		if(this.saveButton.isClicked()) {
			try {
				Blueprint.saveToBlueprint(this.folder + "test" + "." + extension, zoneMap.getZoneMap());

				log.write("Succesfully saved at ...", LogGui.SUCCESS);
			}
			catch(IOException exception) {
				exception.printStackTrace();
			}
		}
	}
	
	/**
	 * Renders the blueprint gui.
	 * 
	 * @param target : the target to render on.
	 * @param states : the states to use while rendering.
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));
		
		for(Entry<Button, String> button : this.buttons.entrySet()) {
			target.draw(button.getKey(), newStates);
		}
		target.draw(saveButton, newStates);
	}

}
