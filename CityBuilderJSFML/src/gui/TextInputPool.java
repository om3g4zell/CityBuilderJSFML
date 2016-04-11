package gui;

import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.View;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.event.Event;

import graphics.FontManager;
import graphics.FontManager.FontID;

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
	
	// Window
	protected RenderWindow window;
	
	/**
	 * Constructor.
	 */
	public TextInputPool(RenderWindow window) {
		this.window = window;
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
	public void addTextInput(String name, Vector2f centerPosition, Vector2f size, String placeholderTextString, FontID fontId, int charSize, Color backgroundColor, Color textColor, Color borderLineColor, Color focusedBorderLineColor) {
		Text text = new Text();
		Text placeholderText = new Text();
		
		text.setFont(this.fonts.get(fontId));
		placeholderText.setFont(this.fonts.get(fontId));
		
		text.setCharacterSize(charSize);
		placeholderText.setCharacterSize(charSize);
		
		text.setString("");
		placeholderText.setString(placeholderTextString);
		
		text.setColor(textColor);
		
		Color placeholderColor = new Color(textColor.r, textColor.g, textColor.b, textColor.a - 80);
		placeholderText.setColor(placeholderColor);
		
		text.setOrigin(text.getLocalBounds().left + text.getLocalBounds().width / 2, text.getLocalBounds().top + text.getLocalBounds().height / 2);
		placeholderText.setOrigin(placeholderText.getLocalBounds().left + placeholderText.getLocalBounds().width / 2, placeholderText.getLocalBounds().top + placeholderText.getLocalBounds().height / 2);
		
		text.setPosition(centerPosition);
		placeholderText.setPosition(centerPosition);
		
		RectangleShape shape = new RectangleShape();
		
		if(size.x != 0.f && size.y != 0.f)
			shape.setSize(size);
		else
			shape.setSize(new Vector2f(placeholderText.getGlobalBounds().width + 20.f, placeholderText.getGlobalBounds().height + 15.f));
		
		shape.setFillColor(backgroundColor);
		shape.setOrigin(shape.getLocalBounds().left + shape.getLocalBounds().width / 2, shape.getLocalBounds().top + shape.getLocalBounds().height / 2);
		shape.setPosition(centerPosition);
		shape.setOutlineThickness(2.f);
		shape.setOutlineColor(borderLineColor);
		
		TextInput textInput = new TextInput();
		textInput.shape = shape;
		textInput.text = text;
		textInput.placeholderText = placeholderText;
		textInput.textColor = textColor;
		textInput.borderLineColor = borderLineColor;
		textInput.focusedBorderLineColor = focusedBorderLineColor;
		textInput.shapeMinSize = shape.getSize();
		
		this.texts.put(name, textInput);
		
	}

	/**
	 * Adds this text input.
	 * 
	 * @param name : the name of the text input.
	 * @param textInput : the text input to add.
	 */
	public void addTextInput(String name, TextInput textInput) {
		this.texts.put(name, textInput);
	}

	/**
	 * Removes the text input.
	 * 
	 * @param name : the name of the text input.
	 */
	public void removeTextInput(String name) {
		this.texts.remove(this.texts.get(name));
	}

	/**
	 * Updates all the text inputs.
	 */
	public void update() {
		
		// Update hover color.
		for(Map.Entry<String, TextInput> entry : this.texts.entrySet()) {
			FloatRect hitbox = entry.getValue().shape.getGlobalBounds();
			hitbox = new FloatRect(hitbox.left + getPosition().x, hitbox.top + getPosition().y, hitbox.width, hitbox.height);
			
			Vector2i rawMousePosition = Mouse.getPosition(this.window);
			Vector2f mousePosition = this.window.mapPixelToCoords(rawMousePosition);
			mousePosition = new Vector2f(rawMousePosition.x, rawMousePosition.y);
			
			if(this.useCustomView)
				mousePosition = this.window.mapPixelToCoords(rawMousePosition, this.customView);
			
			if(hitbox.contains(mousePosition) || entry.getKey() == this.focusedTextName)
				entry.getValue().shape.setOutlineColor(entry.getValue().focusedBorderLineColor);
			else if(entry.getKey() != this.focusedTextName)
				entry.getValue().shape.setOutlineColor(entry.getValue().borderLineColor);
		}
	}

	/**
	 * Handles events relative to the texts inputs.
	 * 
	 * @param event : the event to handle.
	 */
	public void handleEvent(Event event) {
		if(event.type == Event.Type.MOUSE_BUTTON_PRESSED) {
			
			// Reset the focused text input
			this.focusedTextName = "";
			
			for(Map.Entry<String, TextInput> entry : this.texts.entrySet()) {
				FloatRect hitbox = entry.getValue().shape.getGlobalBounds();
				hitbox = new FloatRect(hitbox.left + getPosition().x, hitbox.top + getPosition().y, hitbox.width, hitbox.height);
				
				// Execute callback if the TextInput is clicked.
				Vector2i rawMousePosition = Mouse.getPosition(this.window);
				Vector2f mousePosition = this.window.mapPixelToCoords(rawMousePosition);
				mousePosition = new Vector2f(rawMousePosition.x, rawMousePosition.y);
				
				if(this.useCustomView)
					mousePosition = this.window.mapPixelToCoords(rawMousePosition, this.customView);
				
				if(hitbox.contains(mousePosition)) {
					this.focusedTextName = entry.getKey();
				}
				
				
			}
		}
		else if(event.type == Event.Type.TEXT_ENTERED && this.focusedTextName != "") {
			// Backspace and delete unicode
			if(event.asTextEvent().unicode == 8 || event.asTextEvent().unicode == 127) {
				String str = this.texts.get(this.focusedTextName).text.getString();
				
				// If the string is not empty, we erase one char.
				if(str.length() > 0) {
					str = str.substring(0, str.length() -1);
					this.texts.get(this.focusedTextName).text.setString(str);
				}
			}
			else {
				String str = this.texts.get(this.focusedTextName).text.getString();
				str += event.asTextEvent().character;
				this.texts.get(this.focusedTextName).text.setString(str);
			}
			
			// String change, origin has to be updated.
			this.texts.get(this.focusedTextName).text.setOrigin(this.texts.get(this.focusedTextName).text.getLocalBounds().left + this.texts.get(this.focusedTextName).text.getLocalBounds().width / 2, this.texts.get(this.focusedTextName).text.getLocalBounds().top + this.texts.get(this.focusedTextName).text.getLocalBounds().height / 2);
			
			// Enlarge the text input if necessary.
			if(this.texts.get(this.focusedTextName).text.getLocalBounds().width >= this.texts.get(this.focusedTextName).shape.getLocalBounds().width) {
				this.texts.get(this.focusedTextName).shape.setSize(new Vector2f(this.texts.get(this.focusedTextName).shape.getLocalBounds().width + 2.f, this.texts.get(this.focusedTextName).shape.getSize().y));
				this.texts.get(this.focusedTextName).shape.setOrigin(this.texts.get(this.focusedTextName).shape.getLocalBounds().left + this.texts.get(this.focusedTextName).shape.getLocalBounds().width / 2, this.texts.get(this.focusedTextName).shape.getLocalBounds().top + this.texts.get(this.focusedTextName).shape.getLocalBounds().height / 2);
			}
			else if(this.texts.get(this.focusedTextName).text.getLocalBounds().width > this.texts.get(this.focusedTextName).shapeMinSize.x) {
				this.texts.get(this.focusedTextName).shape.setSize(new Vector2f(this.texts.get(this.focusedTextName).text.getLocalBounds().width + 2.f, this.texts.get(this.focusedTextName).shape.getSize().y));
				this.texts.get(this.focusedTextName).shape.setOrigin(this.texts.get(this.focusedTextName).shape.getLocalBounds().left + this.texts.get(this.focusedTextName).shape.getLocalBounds().width / 2, this.texts.get(this.focusedTextName).shape.getLocalBounds().top + this.texts.get(this.focusedTextName).shape.getLocalBounds().height / 2);
			}
			else {
				this.texts.get(this.focusedTextName).shape.setSize(this.texts.get(this.focusedTextName).shapeMinSize);
				this.texts.get(this.focusedTextName).shape.setOrigin(this.texts.get(this.focusedTextName).shape.getLocalBounds().left + this.texts.get(this.focusedTextName).shape.getLocalBounds().width / 2, this.texts.get(this.focusedTextName).shape.getLocalBounds().top + this.texts.get(this.focusedTextName).shape.getLocalBounds().height / 2);
			}
		}
	}

	/**
	 * Returns the text contained by the text input.
	 * 
	 * @param name : the name of the text input.
	 * @return the content of the text input.
	 */
	public String getText(String name) {
		return this.texts.get(name).text.getString();
	}

	/**
	 * Sets the custom view to use.
	 * 
	 * @param v : the view to use while rendering.
	 */
	public void setCustomView(View v) {
		this.useCustomView = true;
		this.customView = v;
	}
	
	/**
	 * Clear the text in the TextInput.
	 * @param name : Name of the TextInput
	 */
	public void clearText(String name) {
		this.texts.get(name).text.setString("");
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
		
		for(Map.Entry<String, TextInput> entry : this.texts.entrySet()) {
			target.draw(entry.getValue().shape, newStates);
			
			// We display the text only if we are focused or we have something already written.
			if(entry.getKey() == this.focusedTextName || entry.getValue().text.getString() != "") {
				target.draw(entry.getValue().text, newStates);
			}
			else {
				target.draw(entry.getValue().placeholderText, newStates);
			}
		}
		
		
	}
}
