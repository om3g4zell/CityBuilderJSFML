package gui;

import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

import graphics.FontManager;
import graphics.FontManager.FontID;
import sim.Main;

public class LogGui implements Drawable{
	
	protected ArrayList<String> logs;
	protected ArrayList<Text> logText;
	protected FontManager fonts;
	protected Text title;
	protected RectangleShape background;
	protected Time timer = Time.ZERO;
	protected int pointer = 0;
	
	/**
	 * Constructor
	 * @param fonts : all the font
	 */
	public LogGui(FontManager fonts) {
		this.fonts = fonts;
		
		this.logs = new ArrayList<String>();
		this.logText = new ArrayList<Text>();
		
		this.background = new RectangleShape();
		this.background.setSize(new Vector2f(400, Main.HEIGHT));
		this.background.setFillColor(new Color(0, 0, 0, 125));
		this.background.setPosition(new Vector2f(880, 0));
		
		this.title = new Text();
		this.title.setCharacterSize(16);
		this.title.setColor(Color.WHITE);
		this.title.setFont(this.fonts.get(FontID.BEBAS));
		this.title.setPosition(900, 75);
		this.title.setString("Messages :");
		
	}
	
	/**
	 * update the logGui
	 * @param dt : elapsed time
	 */
	public void update(Time dt) {
		this.timer = Time.add(dt, timer);
	}
	
	public void write(String s, Color color) {
		write(s, true, color);
	}
	
	public void write(String s, boolean timerPrefix,Color color) {
		if(timerPrefix) {
			String prefix = "[" + this.timer.asSeconds() + "] ";
			this.logs.add(prefix + s);
		}
		else {
			this.logs.add(s);
		}

		if(this.logText.size() > 0 && this.logText.get(this.logText.size() -1).getGlobalBounds().top + this.logText.get(this.logText.size()-1).getGlobalBounds().height > Main.HEIGHT ) {
			pointer++;
		}
		
		this.logText.clear();
		
		int nb = 0;
		for(int i = pointer ; i < this.logs.size() ; i++, nb++) {
			
			Text text = initText(color);
			String str = this.logs.get(i);
			
			if(this.logs.get(i).length() >= 60) {
				for(int j = 1 ; j <= (int)(str.length()/60); j++) {
					str = new StringBuilder(str).insert(j*60, "\n").toString();
				}
			}
			
			text.setString(str);
			
			if(nb == 0)
				text.setPosition(new Vector2f(900, 100));
			else
				text.setPosition(new Vector2f(900, this.logText.get(nb-1).getGlobalBounds().top + this.logText.get(nb-1).getGlobalBounds().height +5));
			this.logText.add(text);
		}
	}
	
	/**
	 * Count line number of a string
	 * @param str : String
	 * @return nb : number of lines
	 */
	public int getLineNumber(String str) {
		int nb = 1;
		for(char c : str.toCharArray()) {
			if(c == '\n') {
				nb++;
			}
		}
		System.out.println(nb);
		return nb;
	}
	
	/**
	 * initiate text
	 * @return text : the text with our norm
	 */
	public Text initText(Color color) {
		Text text = new Text();
		text.setCharacterSize(12);
		text.setColor(color);
		text.setFont(this.fonts.get(FontID.POINTFREE));
		
		return text;
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		target.draw(background);
		target.draw(title);
		for(Text lt : this.logText) {
			target.draw(lt);
		}
		
	}
}
