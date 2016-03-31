package gui;

import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;

import graphics.FontManager;

public class TextInputPool  extends BasicTransformable implements Drawable {
	// Fonts holder.
	protected FontManager fonts;

	// Stores the text inputs (name, shape).
	protected Map<String, TextInput> texts;

	// Name of the focused text input.
	protected String focusedTextName;

	// Custom view.
	protected View customView;

	// Custom view flag.
	protected boolean useCustomView;
	
	/**
	 * Constructor.
	 */
	public TextInputPool() {
		this.fonts = new FontManager();
		this.texts = new HashMap<String, TextInput>();
		this.focusedTextName = "";
		this.customView = new View();
		this.useCustomView = false;
	}

	/**
	 * Adds a text input with the given parameters.
	 * 
	 * @param name : the name of the text input.
	 * @param centerPosition : where the center of the text input should be positioned.
	 * @param size : the size of the text input (leave (0.f, 0.f) for automatic).
	 * @param placeholderTextString : the string to display when the text input is empty and out of focus.
	 * @param fontId : the id of the font to use.
	 * @param charSize : the size of the characters.
	 * @param backgroundColor : the color of the background.
	 * @param textColor : the color of the text.
	 * @param borderLineColor : the color of the border.
	 * @param focusedBorderLineColor : the color of the border when focused.
	 */
	public void addTextInput(String name, Vector2f centerPosition, Vector2f size, String placeholderTextString, int fontId, int charSize, Color backgroundColor, Color textColor, Color borderLineColor, Color focusedBorderLineColor) {
		
	}

	/**
	 * Adds this text input.
	 * 
	 * @param name : the name of the text input.
	 * @param textInput : the text input to add.
	 */
	public void addTextInput(String name, TextInput textInput) {
		
	}

	/**
	 * Removes the text input.
	 * 
	 * @param name : the name of the text input.
	 */
	public void removeTextInput(String name) {
		
	}

	/**
	 * Updates all the text inputs.
	 */
	public void update() {
		
	}

	/**
	 * Handles events relative to the texts inputs.
	 * 
	 * @param event : the event to handle.
	 */
	public void handleEvent(Event event) {
		
	}

	/**
	 * Returns the text contained by the text input.
	 * 
	 * @param name : the name of the text input.
	 * @return the content of the text input.
	 */
	public String getText(String name) {
		return "";
	}

	/**
	 * Sets the custom view to use.
	 * 
	 * @param v : the view to use while rendering.
	 */
	public void setCustomView(View v) {
		
	}

	/**
	 * Draws the text inputs.
	 * @param target : the target to render on.
	 * @param states : the states to use while rendering.
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		// Combine local and global transformations.
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));
		
		
	}
}
