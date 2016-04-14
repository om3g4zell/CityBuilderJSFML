package gui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
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
	protected TextInputPool textInputPool;
	protected FontManager fonts;
	protected String TextInputName;
	protected Text title;
	
	/**
	 * Constructor.
	 * 
	 * @param textures : the texture manager.
	 * @param fonts : the font manager.
	 * @throws IOException 
	 */
	public BlueprintGui(FontManager fonts, TextInputPool textInputPool) throws IOException {
		this.buttons = new HashMap<Button, String>();
		this.textInputPool = textInputPool;
		this.TextInputName = "BluePrintInput";
		this.fonts = fonts;
		
		this.title = new Text();
		this.title.setFont(this.fonts.get(FontID.POINTFREE));
		this.title.setCharacterSize(24);
		this.title.setPosition(400, 150);
		this.title.setColor(Color.WHITE);
		this.title.setString("Plans : ");
		
		
		this.textInputPool.addTextInput(this.TextInputName, new Vector2f(560.f, 60.f), new Vector2f(0.f,0.f), "Rentrer le nom du bluePrint", FontID.BEBAS, 12, new Color(255, 255, 255, 200), Color.BLACK, Color.BLACK, Color.RED);
		
		this.saveButton = new Button("Save", new Color(128, 128, 128), new Color(48, 48, 48), Color.WHITE, Color.WHITE, fonts.get(FontID.BEBAS), 12);
		this.saveButton.setPosition(this.textInputPool.getTextInput(TextInputName).shape.getGlobalBounds().left + this.textInputPool.getTextInput(TextInputName).shape.getGlobalBounds().width + 10.f, 45.f);
		
		reload();
	}
	
	/**
	 * Reload all button
	 */
	public void reload() {
		this.buttons.clear();
		this.textInputPool.clearText(this.TextInputName);
		File file = new File("res/blueprint");
		File[] files = file.listFiles();
		int j = 0;
		Button previousButton = null;
		int margin = 400;
		
		for(int i = 0 ; i < files.length; i++) {
			int dotPlace = files[i].getName().lastIndexOf('.');
			String name = files[i].getName().substring(0, dotPlace);
			String extension = files[i].getName().substring(dotPlace+1,files[i].getName().length());
			
			if(extension.equals(this.extension)) {
				// Create and configure the button.
				Button button = new Button(name, new Color(128, 128, 128), new Color(48, 48, 48), Color.WHITE, Color.WHITE, this.fonts.get(FontID.BEBAS), 12);
				
				if(previousButton != null) {
					FloatRect localHitbox = previousButton.getRectgangleShape().getGlobalBounds();
					FloatRect hitbox = getTransform().transformRect(localHitbox);
					
					margin = (int)previousButton.getPosition().x + (int)hitbox.width + 20;
				}
				
				previousButton = button;
				button.setPosition(margin,  200 + j * 50);
				
				FloatRect localHitbox = button.getRectgangleShape().getGlobalBounds();
				FloatRect hitbox = getTransform().transformRect(localHitbox);
				
				if(button.getPosition().x + hitbox.width >= 900) {
					j++;
					margin = 400;
					System.out.println(j);
					button.setPosition(margin,  200 + j * 50);
				}
				
				// Add it to the list.
				this.buttons.put(button, name);
			}
		}
	}
	
	public void update() {
		this.saveButton.setPosition(this.textInputPool.getTextInput(TextInputName).shape.getGlobalBounds().left + this.textInputPool.getTextInput(TextInputName).shape.getGlobalBounds().width + 10.f, this.saveButton.getPosition().y);
	}
	/**
	 * Updates the gui.
	 * 
	 * @param window : window to get mouse position.
	 */
	public void handleEvent(RenderWindow window, Event event, ZoneMap zoneMap, LogGui log, ZoneDrawingGui zoneDrawingGui) {
		for(Entry<Button, String> buttonEntry : this.buttons.entrySet()) {
			buttonEntry.getKey().update(new Vector2f(Mouse.getPosition(window).x, Mouse.getPosition(window).y), event);
			
			if(buttonEntry.getKey().isClicked()) {
				try {
					Blueprint bp = new Blueprint(this.folder + buttonEntry.getValue()+ "." + extension);
					zoneMap.setZoneMap(bp.getZoneMap());
					zoneDrawingGui.newRoadAdded = true;
					log.write("Succesfully load " + buttonEntry.getValue(), LogGui.SUCCESS);
				}
				catch(IOException exception) {
					exception.printStackTrace();
				}
			}
		}
		
		this.saveButton.update(new Vector2f(Mouse.getPosition(window).x, Mouse.getPosition(window).y), event);
		
		if(this.saveButton.isClicked() && this.textInputPool.getString(this.TextInputName) != "") {
			try {
				Blueprint.saveToBlueprint(this.folder + this.textInputPool.getString(this.TextInputName) + "." + extension, zoneMap.getZoneMap());
				log.write("Succesfully saved at : " + this.folder + this.textInputPool.getString(this.TextInputName) + "." + extension, LogGui.SUCCESS);
				reload();
			}
			catch(IOException exception) {
				exception.printStackTrace();
			}
		}else if(this.textInputPool.getString(this.TextInputName) == "" && this.saveButton.isClicked()) {
			log.write("Veuillez entrez un nom", LogGui.WARNING);
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
		target.draw(this.title);
	}

}
