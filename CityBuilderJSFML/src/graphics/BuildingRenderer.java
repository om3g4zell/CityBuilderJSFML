package graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import graphics.TextureManager.TextureID;
import world.Building;

public class BuildingRenderer implements Drawable {
	protected TextureManager textures;
	protected Map<Building.BuildingType, Color> colorPlaceholders;
	protected Map<Building.BuildingType, FloatRect> texturesRect;
	protected Vector2f tileSize;
	protected List<Building> buildings;
	
	public BuildingRenderer(Vector2f tileSize, TextureManager tm) {
		this.colorPlaceholders = new HashMap<Building.BuildingType, Color>();
		this.texturesRect = new HashMap<Building.BuildingType, FloatRect>();
		this.tileSize = tileSize;
		this.textures = tm;
		this.buildings = new ArrayList<Building>();
	}
	
	protected boolean hasTexture(Building.BuildingType btype) {
		switch(btype) {
			case ROAD:
			case HOUSE:
			case GENERATOR:
			case HYDROLIC_STATION:
			case GROCERY_STORE:
			case ANTENNA_4G:
			case CASINOS:
			case HOSPITAL:
			case CINEMA:
			case FIRE_STATION:
			case POLICE_STATION:
			case STADIUM:
				return true;
			default:
				return false;
		}
	}
	
	public void setColorPlaceholder(Building.BuildingType btype, Color color) {
		this.colorPlaceholders.put(btype, color);
	}
	
	public void setTextureRect(Building.BuildingType btype, FloatRect textureRect) {
		this.texturesRect.put(btype, textureRect);
	}
	
	public void setBuildingList(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	public void draw(RenderTarget target, RenderStates states) {
		VertexArray coloredVA = new VertexArray(PrimitiveType.QUADS);
		VertexArray texturedVA = new VertexArray(PrimitiveType.QUADS);

		for(Building b : buildings) {
			if(hasTexture(b.getType())) {
				Vertex lt = new Vertex(
							new Vector2f(b.getHitbox().left * this.tileSize.x, b.getHitbox().top * this.tileSize.y),
							new Vector2f(this.texturesRect.get(b.getType()).left, this.texturesRect.get(b.getType()).top)
						);
				Vertex rt = new Vertex(
						new Vector2f((b.getHitbox().left + b.getHitbox().width) * this.tileSize.x, b.getHitbox().top * this.tileSize.y),
						new Vector2f(this.texturesRect.get(b.getType()).left + this.texturesRect.get(b.getType()).width, this.texturesRect.get(b.getType()).top)
					);
				Vertex rb = new Vertex(
						new Vector2f((b.getHitbox().left + b.getHitbox().width) * this.tileSize.x, (b.getHitbox().top + b.getHitbox().height) * this.tileSize.y),
						new Vector2f(this.texturesRect.get(b.getType()).left + this.texturesRect.get(b.getType()).width, this.texturesRect.get(b.getType()).top + this.texturesRect.get(b.getType()).height)
					);
				Vertex lb = new Vertex(
						new Vector2f(b.getHitbox().left * this.tileSize.x, (b.getHitbox().top + b.getHitbox().height) * this.tileSize.y),
						new Vector2f(this.texturesRect.get(b.getType()).left, this.texturesRect.get(b.getType()).top + this.texturesRect.get(b.getType()).height)
					);

				texturedVA.add(lt);
				texturedVA.add(rt);
				texturedVA.add(rb);
				texturedVA.add(lb);
			}
			else {
				Vertex lt = new Vertex(
						new Vector2f(b.getHitbox().left * this.tileSize.x, b.getHitbox().top * this.tileSize.y),
						this.colorPlaceholders.get(b.getType())
					);
				Vertex rt = new Vertex(
						new Vector2f((b.getHitbox().left + b.getHitbox().width) * this.tileSize.x, b.getHitbox().top * this.tileSize.y),
						this.colorPlaceholders.get(b.getType())
					);
				Vertex rb = new Vertex(
						new Vector2f((b.getHitbox().left + b.getHitbox().width) * this.tileSize.x, (b.getHitbox().top + b.getHitbox().height) * this.tileSize.y),
						this.colorPlaceholders.get(b.getType())
					);
				Vertex lb = new Vertex(
						new Vector2f(b.getHitbox().left * this.tileSize.x, (b.getHitbox().top + b.getHitbox().height) * this.tileSize.y),
						this.colorPlaceholders.get(b.getType())
					);
		
				coloredVA.add(lt);
				coloredVA.add(rt);
				coloredVA.add(rb);
				coloredVA.add(lb);
			}
		}
		
		target.draw(coloredVA, states);
		
		states = new RenderStates(states, (ConstTexture)(this.textures.get(TextureID.BUILDINGS_TEXTURE)));
		target.draw(texturedVA, states);
	}
}
