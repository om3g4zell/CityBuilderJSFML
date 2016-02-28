package gui;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import graphics.FontManager;
import graphics.FontManager.FontID;
import graphics.Graph;

public class GraphStatsGui implements Drawable {
	protected RectangleShape darkBackground;
	protected Graph populationGraph;
	protected Graph moneyGraph;
	protected Graph numberOfBuildingsGraph;
	protected Text populationLegend;
	protected Text moneyLegend;
	protected Text numberOfBuildingsLegend;
	protected int lastx;
	
	/**
	 * Constructor.
	 */
	public GraphStatsGui(Vector2i windowSize, FontManager fonts) {		
		// Create the dark background.
		this.darkBackground = new RectangleShape(new Vector2f(windowSize.x, windowSize.y));
		this.darkBackground.setFillColor(new Color(0, 0, 0, 125));
			
		// Prepare the graphs.
		this.populationGraph = new Graph();
		this.moneyGraph = new Graph();
		this.numberOfBuildingsGraph = new Graph();
		
		this.populationGraph.setSize(300, 400);
		this.moneyGraph.setSize(300, 400);
		this.numberOfBuildingsGraph.setSize(300, 400);
		
		this.populationGraph.setPosition(50, 200);
		this.moneyGraph.setPosition(500, 200);
		this.numberOfBuildingsGraph.setPosition(950, 200);
		
		this.populationGraph.setNumberOfPoints(40);
		this.moneyGraph.setNumberOfPoints(40);
		this.numberOfBuildingsGraph.setNumberOfPoints(40);
		
		this.populationGraph.drawAxes(true);
		this.moneyGraph.drawAxes(true);
		this.numberOfBuildingsGraph.drawAxes(true);
		
		this.populationGraph.update();
		this.moneyGraph.update();
		this.numberOfBuildingsGraph.update();
		
		// Prepare the legends.
		this.populationLegend = new Text("Evolution de la population", fonts.get(FontID.VCR_MONO));
		this.moneyLegend = new Text("Evolution du compte banquaire", fonts.get(FontID.VCR_MONO));
		this.numberOfBuildingsLegend = new Text("Evolution du nombre de batiments", fonts.get(FontID.VCR_MONO));
		
		final int characterSize = 15;
		this.populationLegend.setCharacterSize(characterSize);
		this.moneyLegend.setCharacterSize(characterSize);
		this.numberOfBuildingsLegend.setCharacterSize(characterSize);
		
		// Center the origin.
		this.populationLegend.setOrigin(this.populationLegend.getGlobalBounds().width / 2.f, this.populationLegend.getGlobalBounds().height / 2.f);
		this.moneyLegend.setOrigin(this.moneyLegend.getGlobalBounds().width / 2.f, this.moneyLegend.getGlobalBounds().height / 2.f);
		this.numberOfBuildingsLegend.setOrigin(this.numberOfBuildingsLegend.getGlobalBounds().width / 2.f, this.numberOfBuildingsLegend.getGlobalBounds().height / 2.f);
		
		this.populationLegend.setPosition(this.populationGraph.getPosition().x + 150, this.populationGraph.getPosition().y + 425);
		this.moneyLegend.setPosition(this.moneyGraph.getPosition().x + 150, this.moneyGraph.getPosition().y + 425);
		this.numberOfBuildingsLegend.setPosition(this.numberOfBuildingsGraph.getPosition().x + 150, this.numberOfBuildingsGraph.getPosition().y + 425);
		
		lastx = 0;
	}
	
	/**
	 * Update the graphs.
	 */
	public void update(int population, int money, int numberOfBuildings) {
		this.populationGraph.addValue(this.lastx, population);
		this.moneyGraph.addValue(this.lastx, money);
		this.numberOfBuildingsGraph.addValue(this.lastx, numberOfBuildings);
		
		this.lastx++;
		
		// Let the graphs update themselves.
		this.populationGraph.update();
		this.moneyGraph.update();
		this.numberOfBuildingsGraph.update();
	}

	/**
	 * Draws the graph stats.
	 * 
	 * @param target : the target to render on
	 * @param states : the states to use to render
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		target.draw(this.darkBackground, states);
		
		target.draw(this.populationGraph, states);
		target.draw(this.moneyGraph, states);
		target.draw(this.numberOfBuildingsGraph, states);
		
		target.draw(this.populationLegend, states);
		target.draw(this.moneyLegend, states);
		target.draw(this.numberOfBuildingsLegend, states);
	}

}
