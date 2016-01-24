package gui;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import graphics.TextureManager;

/*
 * A simple checkbox class
 */
public class CheckBox implements Drawable{
	
	protected IntRect hitbox;
	protected boolean checkFlag;
	protected Sprite checkedSprite;
	protected Sprite noCheckedSprite;
	
	/**
	 * Constructor of the checkbox
	 * @param x : the position x of the checkbox
	 * @param y : the position y of the checkbox
	 * @param loader : to get the texture of the checkbox
	 */
	public CheckBox(int x, int y, TextureManager loader) {
		this.hitbox = new IntRect(x, y, 16, 16);
		
		this.checkedSprite = new Sprite();
		this.checkedSprite.setTexture(loader.get(TextureManager.TextureID.CHECKBOX_TEXTURE));
		this.checkedSprite.setTextureRect(new IntRect(0, 0, 16, 16));
		this.checkedSprite.setPosition(x, y);
		
		this.noCheckedSprite = new Sprite();
		this.noCheckedSprite.setTexture(loader.get(TextureManager.TextureID.CHECKBOX_TEXTURE));
		this.noCheckedSprite.setTextureRect(new IntRect(16, 0, 16, 16));
		this.noCheckedSprite.setPosition(x, y);
	}
	
	/**
	 * set the checkflag of the checkbox
	 * @param b : new states of the checkbox
	 */
	public void setChecked(boolean b) {
		this.checkFlag = b;
	}
	
	/**
	 * return the checkflag
	 * @return boolean : the checkflag
	 */
	public boolean isChecked() {
		return this.checkFlag;
	}
	
	/**
	 * return the hitbox of the checkbox
	 * @return IntRect : the hitbox of the checkbox
	 */
	public IntRect getHitbox() {
		return this.hitbox;
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {
		if(this.checkFlag) {
			target.draw(this.checkedSprite);
		}
		else {
			target.draw(noCheckedSprite);
		}
		
	}
}
