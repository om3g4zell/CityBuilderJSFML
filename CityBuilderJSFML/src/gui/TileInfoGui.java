package gui;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.CircleShape;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import graphics.FontManager;
import graphics.Tile;
import world.Building;
import world.Need;
import world.Resource;
import world.ResourcesMap;
import world.ResourcesStack;

public class TileInfoGui implements Drawable {

	protected String infoString;
	protected Text infoText;
	protected Vector2i position;
	protected ResourcesMap resourceMap;
	protected TileSelector tileSelector;
	protected List<ArrayList<Tile>> tileMap;
	protected Tile actualTile;
	protected Building building;
	protected RectangleShape rectangleShape;
	protected CircleShape range;
	
	public TileInfoGui(List<ArrayList<Tile>> tiles, FontManager fontmanager) {
		
		this.tileMap = tiles;
		this.infoString = new String();
		this.range = new CircleShape();
		this.range.setFillColor(new Color(255, 255, 0, 40));
		this.range.setOrigin((this.range.getLocalBounds().left + this.range.getLocalBounds().width) / 2.f, (this.range.getLocalBounds().top + this.range.getLocalBounds().height) / 2.f);
		
		// we instanciate the Text
		this.infoText = new Text();
		this.infoText.setFont(fontmanager.get(FontManager.FontID.CAVIAR_DREAM));
		this.infoText.setString(this.infoString);
		this.infoText.setCharacterSize(12);
		this.infoText.setColor(Color.WHITE);
		
		// we instanciate the RectangleShape
		this.rectangleShape = new RectangleShape();
		this.rectangleShape.setFillColor(new Color(0, 0, 25, 100));		
	}

	/**
	 * set the string of resources on the tile or batiment
	 * @param resourcemap : to get resources on the tile or batiment
	 * @param tileselector : to get the position of the mouse
	 * @param buildings : to get buildings on the map
	 */
	public void update(ResourcesMap resourcemap, TileSelector tileselector, List<Building> buildings) {
		this.tileSelector = tileselector;
		this.resourceMap = resourcemap;
		this.infoString = "";
		this.range.setRadius(0);
		
		this.position = new Vector2i(this.tileSelector.selectedTile.x,this.tileSelector.selectedTile.y);
		this.actualTile = this.tileMap.get(position.y).get(position.x);
		
		// show Tile info
		this.infoString += "Tile position : {" + position.x + ", " + position.y + "}\n";
		this.infoString += "TILE_TYPE : " + this.actualTile.getTileType().toString() + "\n";
		for(Resource.ResourceType resource : Resource.ResourceType.values()) {
			if(resourcemap.getResources(this.position.x, this.position.y).get(resource) > 0) {
				this.infoString += "\t" + resource.toString() + " : " + resourcemap.getResources(this.position.x, this.position.y).get(resource) + "\n";
			}
		}
		
		// show Building info
		for(Building b : buildings) {
			if(b.getHitbox().contains(position)) {
				this.building = b;
				break;
			}
		}

		if(this.building != null) {
			this.infoString += "BUILDING_NAME : " + this.building.getType().toString() + "[" + this.building.getId() + "] \n";
			this.infoString += "Level : " + this.building.getLevel() + "\n";
			
			// Get the resources available for the building.
			ResourcesStack availableResources = new ResourcesStack();
			
			for(int x = this.building.getHitbox().left ; x < this.building.getHitbox().left + this.building.getHitbox().width ; ++x) {
				for(int y = this.building.getHitbox().top ; y < this.building.getHitbox().top + this.building.getHitbox().height ; ++y) {
					availableResources.add(resourcemap.getResources(new Vector2i(x, y)));
				}
			}
			
			for(Resource.ResourceType resource : Resource.ResourceType.values()) {
				// show %
				for(Need need : this.building.getNeeds()) {
					if(need.type.equals(resource)){
						this.infoString += "\t" + resource.toString() + " : " + availableResources.get(resource);
						this.infoString += " : " + (int)((availableResources.get(resource) / need.amount) * 100) + " %";
						this.infoString += "\n";
					}
				}
			}
			
			this.infoString += "Halted : " + this.building.isHalted() + "\n";
			
			this.infoString += "Inhabitants : " + this.building.getInhabitants().size();
			if(this.building.getUnemployedInhabitantCount() != -1)
				this.infoString += " (" + this.building.getUnemployedInhabitantCount() + " unemployed)";
			this.infoString += "\n";
			
			this.infoString += "Clients : " + this.building.getClients().size() + "\n";
			this.infoString += "Employees : " + this.building.getEmployees().size() + "\n";
		}
		
		// we set the text
		this.infoText.setString(infoString);
		this.infoText.setPosition(this.position.x*16 +20, this.position.y*16 +20);
		
		// we set the rectangle
		this.rectangleShape.setPosition(this.position.x *16, this.position.y*16);
		this.rectangleShape.setSize(new Vector2f(this.infoText.getGlobalBounds().width + 30,this.infoText.getGlobalBounds().height + 30));
		
		// we set the range
		if(this.building != null) {
			this.range.setPosition(this.building.getHitbox().left * 16 + this.building.getHitbox().width * 8, this.building.getHitbox().top * 16 + this.building.getHitbox().height * 8);
			this.range.setRadius(this.building.getHitbox().width * 8 + this.building.getRange() * 16);
			this.range.setOrigin((this.range.getLocalBounds().left + this.range.getLocalBounds().width) / 2.f, (this.range.getLocalBounds().top + this.range.getLocalBounds().height) / 2.f);
		}
		
		// we reset the building
		this.building = null;
		
	} 
	
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		
		target.draw(this.rectangleShape, states);
		target.draw(this.infoText, states);
		if(range.getRadius() != 0)
			target.draw(range, states);
		
	}
}
