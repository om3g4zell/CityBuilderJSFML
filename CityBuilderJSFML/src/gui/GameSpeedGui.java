package gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.IntRect;
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
	protected float coef;
	protected GameSpeed gameSpeed;
	
	public static enum GameSpeed {
		PAUSE,
		x1,
		x2,
		x3,
		x4
	}
	
	/**
	 * Constructor
	 * @param textures : texture of the pause
	 * @param x : position x of the pause
	 * @param y : position y of the pause
	 */
	public GameSpeedGui(TextureManager textures, FontManager fonts, int x, int y) {
		this.position = new Vector2i(x, y);
		setSpeedCoeff(GameSpeed.PAUSE);
		this.timer = Time.ZERO;
		
		this.sprite = new Sprite();
		this.sprite.setTexture(textures.get(TextureID.PAUSE));
		this.sprite.setPosition(this.position.x , this.position.y);
		
		this.pauseFlag = true;
		this.text = new Text();
		this.text.setFont(fonts.get(FontID.CAVIAR_DREAM));
		this.text.setPosition(this.position.x - 10, this.position.y + 50);
		this.text.setColor(Color.WHITE);
		this.text.setCharacterSize(16);
		this.text.setString("00 : 00");
		this.temp = 0;
		
	}
	
	/**
	 * set the coef
	 * @param state : GameSpeed
	 */
	public void setSpeedCoeff(GameSpeed state) {
		this.gameSpeed = state;
		switch(this.gameSpeed) {
		case PAUSE:
			this.coef = 0.f;
			break;
		case x1:
			this.coef = 1.f;
			break;
		case x2:
			this.coef = 2.f;
			break;
		case x3:
			this.coef = 3.f;
			break;
		case x4:
			this.coef = 4.f;
			break;
		}
	}
	
	/**
	 * return the speed coeff
	 * @return float : the speed coeff
	 */
	public float getSpeedCoeff() {
		return this.coef;
	}
	
	/**
	 * update
	 * @param dt : elapsed time
	 */
	public void update(Time dt) {
		this.timer = Time.add(dt, timer);
		if(this.pauseFlag) {
			this.sprite.setTextureRect(new IntRect(0,0,32,32));
		}
		else {
			this.temp += dt.asSeconds() * this.coef;
			this.sprite.setTextureRect(new IntRect(32,0,32,32));
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
		
		if(e.type == Event.Type.KEY_RELEASED && e.asKeyEvent().key == Key.ADD) {
			int i = 0;
			for(; i < GameSpeed.values().length ; i++)
				if(this.gameSpeed == GameSpeed.values()[i])
					break;
			i = Math.min(GameSpeed.values().length - 1, i + 1);
			setSpeedCoeff(GameSpeed.values()[i]);
		}
		
		if(e.type == Event.Type.KEY_RELEASED && e.asKeyEvent().key == Key.SUBTRACT) {
			int i = 0;
			for(; i < GameSpeed.values().length ; i++)
				if(this.gameSpeed == GameSpeed.values()[i])
					break;
			i = Math.max(1, i - 1);
			setSpeedCoeff(GameSpeed.values()[i]);
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
		
		if(this.timer.asSeconds() <= 0.5f ) {
			target.draw(sprite);
		}
		else if(this.timer.asSeconds() >= 1.f) {
			this.timer = Time.ZERO;
		}
		
	}
}
