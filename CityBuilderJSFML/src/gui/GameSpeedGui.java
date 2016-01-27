package gui;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import graphics.TextureManager;
import graphics.TextureManager.TextureID;

public class GameSpeedGui implements Drawable{
	protected Sprite sprite;
	protected Vector2i position;
	protected Time timer;
	protected boolean pauseFlag;
	
	/**
	 * Constructor
	 * @param textures : texture of the pause
	 * @param x : position x of the pause
	 * @param y : position y of the pause
	 */
	public GameSpeedGui(TextureManager textures, int x, int y) {
		this.position = new Vector2i(x, y);
		this.timer = Time.ZERO;
		
		this.sprite = new Sprite();
		this.sprite.setTexture(textures.get(TextureID.PAUSE));
		this.sprite.setPosition(this.position.x , this.position.y);
		
	}
	
	/**
	 * update
	 * @param dt : elapsed time
	 */
	public void update(Time dt) {
		
		if(this.pauseFlag) {
			this.timer = Time.add(dt, timer);
		}
		else {
			this.timer = Time.ZERO;
		}
	}
	
	/**
	 * 
	 * @param e : event
	 */
	public void handleEvent(Event e) {
		if(e.type == Event.Type.KEY_RELEASED && e.asKeyEvent().key == Key.P) {
			this.pauseFlag = !this.pauseFlag;
		}
	}
	
	/**
	 * return the pause flag
	 * @return boolean : if the game is in pause
	 */
	public boolean isInPause() {
		return this.pauseFlag;
	}
	
	/**
	 * set the pause on or false
	 * @param b : boolean the state of pause
	 */
	public void setPaused(boolean b) {
		this.pauseFlag = b;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		if(!this.pauseFlag) {
			return;
		}
		
		if(this.timer.asSeconds() <= 0.5f ) {
			target.draw(sprite);
		}
		else if(this.timer.asSeconds() >= 1.f) {
			this.timer = Time.ZERO;
		}
		
	}
}
