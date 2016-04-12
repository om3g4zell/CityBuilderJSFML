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
	
	protected Sprite money;
	protected Sprite people;
	
	protected Text moneyText;
	protected Text peopleText;
	protected Text unemployedText;
	
	/**
	 * Initiate sprite and text for the GUI
	 * @param loader : TextureLoader for get the Texture
	 * @param font : FontLoader for get the Font
	 */
	public StatsGui(TextureManager loader,FontManager font) {
		this.money = new Sprite();
		this.people = new Sprite();
		
		this.money.setTexture(loader.get(TextureManager.TextureID.MONEY_TEXTURE));
		this.money.setPosition(10, 10);
		
		this.people.setTexture(loader.get(TextureManager.TextureID.PEOPLE_TEXTURE));
		this.people.setPosition(10, 50);
		
		this.moneyText = new Text();
		this.moneyText.setFont(font.get(FontManager.FontID.VCR_MONO));
		this.moneyText.setCharacterSize(24);
		this.moneyText.setColor(Color.WHITE);
		this.moneyText.setPosition(50, 10);
		
		this.peopleText = new Text();
		this.peopleText.setFont(font.get(FontManager.FontID.VCR_MONO));
		this.peopleText.setCharacterSize(24);
		this.peopleText.setColor(Color.WHITE);
		this.peopleText.setPosition(50, 50);
		
		this.unemployedText = new Text();
		this.unemployedText.setFont(font.get(FontManager.FontID.VCR_MONO));
		this.unemployedText.setCharacterSize(24);
		this.unemployedText.setColor(Color.WHITE);
		this.unemployedText.setPosition(80, 10);
				
	}
	
	/** 
	 * @param population : change the text with the population
	 */
	public void setPopulation(int population) {
		this.peopleText.setString(""+population);
	}
	
	/**
	 * @param money : change the text with the money
	 */
	public void setMoney(int money) {
		this.moneyText.setString(""+money);
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

		target.draw(this.money, newStates);
		target.draw(this.people, newStates);
		
		target.draw(this.moneyText, newStates);
		target.draw(this.peopleText, newStates);
		target.draw(this.unemployedText, newStates);
	}
	
	
}
