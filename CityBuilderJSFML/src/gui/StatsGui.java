package gui;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.Transform;

import graphics.FontManager;
import graphics.TextureManager;

public class StatsGui extends BasicTransformable implements Drawable{
	
	protected Sprite people;
	
	protected Text peopleText;
	protected Text unemployedText;
	
	/**
	 * Initiate sprite and text for the GUI
	 * @param loader : TextureLoader for get the Texture
	 * @param font : FontLoader for get the Font
	 */
	public StatsGui(TextureManager loader,FontManager font) {
		this.people = new Sprite();
			
		this.people.setTexture(loader.get(TextureManager.TextureID.PEOPLE_TEXTURE));
		this.people.setPosition(10, 50);
		
		this.peopleText = new Text();
		this.peopleText.setFont(font.get(FontManager.FontID.VCR_MONO));
		this.peopleText.setCharacterSize(24);
		this.peopleText.setColor(Color.WHITE);
		this.peopleText.setPosition(50, 50);
		
		this.unemployedText = new Text();
		this.unemployedText.setFont(font.get(FontManager.FontID.VCR_MONO));
		this.unemployedText.setCharacterSize(24);
		this.unemployedText.setColor(Color.WHITE);
		this.unemployedText.setPosition(10, 10);
				
	}
	
	/** 
	 * @param population : change the text with the population
	 */
	public void setPopulation(int population) {
		this.peopleText.setString(""+population);
	}
	
	/**
	 * 
	 * @param rate : percentage of unemployed people
	 */
	public void setUnemployedRate(float rate) {
		this.unemployedText.setString("Chomage : " + rate + " %");
	}
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));

		target.draw(this.people, newStates);
		
		target.draw(this.peopleText, newStates);
		target.draw(this.unemployedText, newStates);
	}
	
	
}
