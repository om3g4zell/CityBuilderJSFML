package gui;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Mouse;


public class Button extends BasicTransformable implements Drawable{
	
	private RectangleShape background;
	private Text text;
	private Color backgroundColor;
	private Color backgroundHoverColor;
	private Color textColor;
	private Color textHoverColor;
	private Font font;
	private boolean clickedFlag;
	
	public Button(String text, Color backgroundColor, Color backgroundHoverColor,Color textColor, Color textHoverColor, Font font) {
		this.backgroundColor = backgroundColor;
		this.backgroundHoverColor = backgroundHoverColor;
		this.textColor = textColor;
		this.textHoverColor = textHoverColor;
		this.font = font;
		
		this.text = new Text();
		this.text.setFont(this.font);
		this.text.setPosition(new Vector2f(10, 10));
		this.text.setString(text);
		this.text.setColor(textColor);
		
		this.background = new RectangleShape();
		this.background.setSize(new Vector2f(this.text.getGlobalBounds().width, this.text.getGlobalBounds().height));
		this.background.setFillColor(this.backgroundColor);
	}
	
	/**
	 * update the button
	 * @param mousePosition : the world position of the mouse
	 */
	public void update(Vector2f mousePosition) {
		this.clickedFlag = false;
		FloatRect localHitbox = this.background.getGlobalBounds();
		FloatRect hitbox = getTransform().transformRect(localHitbox);
		
		if(hitbox.contains(mousePosition)) {
			this.background.setFillColor(backgroundHoverColor);
			this.text.setColor(textHoverColor);
			if(Mouse.isButtonPressed(Mouse.Button.LEFT)) {
				this.clickedFlag = true;
			}
		}
		else {
			this.background.setFillColor(backgroundColor);
			this.text.setColor(textColor);
		}
	}
	
	/**
	 * the clicked flag
	 * @return clickedFlag
	 */
	public boolean isCLicked() {
		return this.clickedFlag;
	}
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		 RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));
		 
		 target.draw(this.background,newStates);
		 target.draw(this.text, newStates);
	}
}
