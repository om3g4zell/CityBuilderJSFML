package gui;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;

import graphics.FontManager;
import graphics.TextureManager;
import world.Zone;
import world.Zone.ZoneClass;
import world.ZoneMap;

public class ZoneDrawingGui implements Drawable {
	protected List<CheckBox> checkboxes;
	protected Time lastZoneClassChange;
	
	/**
	 * Constructor.
	 * @param textures : the textures manager
	 * @param fonts : the fonts manager
	 */
	public ZoneDrawingGui(TextureManager textures, FontManager fonts) {
		this.checkboxes = new ArrayList<CheckBox>();
		this.lastZoneClassChange = Time.ZERO;
		
		int i = 0;
		for(ZoneClass z : ZoneClass.values()) {
			this.checkboxes.add(new CheckBox(27, 117 + i * 17, textures, fonts, z.toString(), z.hashCode()));
			i++;
		}
	}
	
	/**
	 * Updates the zone drawing gui, draws on the zone map.
	 * @param zoneMap : the zone map
	 * @param tileSelector : the tile selector
	 */
	public void update(Time dt, RenderWindow window, ZoneMap zoneMap, TileSelector tileSelector) {
		// Get the window position.
	    Vector2i rawMousePosition = Mouse.getPosition(window);
	    Vector2f mousePosition = window.mapPixelToCoords(rawMousePosition);
	    
		lastZoneClassChange = Time.add(lastZoneClassChange, dt);

		if(Mouse.isButtonPressed(Mouse.Button.LEFT) && Time.ratio(lastZoneClassChange, Time.getSeconds(0.5f)) >= 1.f) {
			// Do not draw under checkboxes.
			boolean underCheckbox = false;
			for(CheckBox cb : this.checkboxes) {
				if(cb.getHitbox().contains((int)mousePosition.x, (int)mousePosition.y))
					underCheckbox = true;
			}
			
			if(!underCheckbox) {
				// Get the zone class and draw.
				ZoneClass zoneClass = ZoneClass.FREE;
				for(CheckBox cb : this.checkboxes) {
					if(cb.isChecked()) {
						int zoneClassHashCode = cb.getValue();
						
						for(ZoneClass z : ZoneClass.values())
							if(z.hashCode() == zoneClassHashCode)
								zoneClass = z;
					}
				}
				
				Vector2i selectedTile = tileSelector.getSelectedTile();
				Zone z = zoneMap.getZoneMap().get(selectedTile.y).get(selectedTile.x);
				z.setType(zoneClass);
			}
		}
	}
	
	/**
	 * Handle the JSFML event.
	 * @param event : the event to handle
	 */
	public void handleEvent(Event event) {
		for(CheckBox cb : this.checkboxes) {
			boolean checkedCache = cb.isChecked();
			
			cb.handleEvent(event);
			
			if(checkedCache != cb.isChecked()) {
				disableCheckboxes(cb);
				lastZoneClassChange = Time.ZERO;
			}
		}
	}
	
	/**
	 * Disable all the checkboxes.
	 */
	protected void disableCheckboxes(CheckBox doNotDisable) {
		for(CheckBox cb : this.checkboxes) {
			if(!cb.equals(doNotDisable))
				cb.setChecked(false);
		}
	}

	/**
	 * Draws the checkboxes.
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		for(CheckBox cb : this.checkboxes) {
			target.draw(cb, states);
		}
	}
}
