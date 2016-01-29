package gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.event.Event;

import graphics.FontManager;
import graphics.FontManager.FontID;
import graphics.TextureManager;
import graphics.TextureManager.TextureID;

public class GameSpeedGui implements Drawable{
	protected Sprite sprite;
	protected Vector2i position;
	protected Time timer;
	protected boolean pauseFlag;
	protected Text text;
	protected double temp;
	protected String tempS = "";
	
	/**
	 * Constructor
	 * @param textures : texture of the pause
	 * @param x : position x of the pause
	 * @param y : position y of the pause
	 */
	public GameSpeedGui(TextureManager textures, FontManager fonts, int x, int y) {
		this.position = new Vector2i(x, y);
		this.timer = Time.ZERO;
		
		this.sprite = new Sprite();
		this.sprite.setTexture(textures.get(TextureID.PAUSE));
		this.sprite.setPosition(this.position.x , this.position.y);
		
		this.text = new Text();
		this.text.setFont(fonts.get(FontID.CAVIAR_DREAM));
		this.text.setPosition(this.position.x - 10, this.position.y + 50);
		this.text.setColor(Color.WHITE);
		this.text.setCharacterSize(16);
		this.text.setString("00 : 00");
		this.temp = 0;
		
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
			this.temp += dt.asSeconds();
			this.timer = Time.ZERO;
		}
		convertTime();
		this.text.setString(tempS);
	}
	
	/**
	 * Convert time in String
	 * @return String : time in String
	 */
	public void convertTime() {
		double minutes = 0;
		double hour = 0;
		double secondes = 0;
		double temp = this.temp;
		if(temp >= 3600) {
			while(temp >= 3600) {
				temp -= 3600;
				hour++;
			}
		}
		if(temp >= 60) {
			while(temp > 60) {
				temp -= 60;
				minutes++;
			}
		}
		secondes = temp;
		this.tempS = (int)hour + " : " + (int)minutes + " : " + (int)secondes ;
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
		target.draw(text);
		
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
