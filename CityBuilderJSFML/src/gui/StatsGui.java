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
	
	public StatsGui(TextureManager loader,FontManager font) {
		this.money = new Sprite();
		this.people = new Sprite();
		
		this.money.setTexture(loader.get(TextureManager.TextureID.MONEY_TEXTURE));
		this.money.setPosition(10, 10);
		
		this.people.setTexture(loader.get(TextureManager.TextureID.PEOPLE_TEXTURE));
		this.people.setPosition(10, 10);
		
		this.moneyText = new Text();
		this.moneyText.setFont(font.get(FontManager.FontID.BASIC));
		this.moneyText.setCharacterSize(16);
		this.moneyText.setColor(Color.BLACK);
		
		this.peopleText = new Text();
		this.peopleText.setFont(font.get(FontManager.FontID.BASIC));
		this.peopleText.setCharacterSize(16);
		this.peopleText.setColor(Color.BLACK);
				
	}
	public void setPopulation(int population) {
		this.peopleText.setString(""+population);
	}
	public void setMoney(int money) {
		this.moneyText.setString(""+money+" $");
	}
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));

		target.draw(this.money, newStates);
		target.draw(this.people, newStates);
		
		target.draw(this.moneyText, newStates);
		target.draw(this.peopleText, newStates);
		
	}
	
	
}
