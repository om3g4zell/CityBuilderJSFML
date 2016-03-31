package gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 * Stores the data relative to a text input.
 */
public class TextInput {
	public RectangleShape shape;
	public Text text;
	public Text placeholderText;
	public Color textColor;
	public Color borderLineColor;
	public Color focusedBorderLineColor;
	public Vector2f shapeMinSize;
}
