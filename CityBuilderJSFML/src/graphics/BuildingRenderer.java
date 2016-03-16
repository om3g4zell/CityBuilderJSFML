package graphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsfml.graphics.*;
import org.jsfml.system.*;

import world.Building;

public class BuildingRenderer {
	protected TextureManager textures;
	protected Map<Building.BuildingType, Color> colorPlaceholders;
	protected Map<Building.BuildingType, FloatRect> texturesRect;
	protected Vector2f tileSize;
	
	public BuildingRenderer(Vector2f tileSize) {
		this.colorPlaceholders = new HashMap<Building.BuildingType, Color>();
		this.texturesRect = new HashMap<Building.BuildingType, FloatRect>();
		this.textures.load("res/grocery_store.64x32.png");
		this.tileSize = tileSize;
	}
	
	protected boolean hasTexture(Building.BuildingType btype) {
		switch(btype) {
			case GROCERY_STORE:
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
	
	public void render(RenderTarget target, List<Building> buildings) {
		VertexArray coloredVA = new VertexArray();
		VertexArray texturedVA = new VertexArray();

		for(Building b : buildings) {
			if(hasTexture(b.getType())) {
				Vertex lt = new Vertex(
							new Vector2f(b.getHitbox().left * this.tileSize.x, b.getHitbox().top * this.tileSize.y),
							new Vector2f(this.texturesRect.get(b.getType()).left, this.texturesRect.get(b.getType()).top)
						);
				Vertex rt = new Vertex(
						new Vector2f((b.getHitbox().left + b.getHitbox().width) * this.tileSize.x, b.getHitbox().top * this.tileSize.y),
						new Vector2f(this.texturesRect.get(b.getType()).left, this.texturesRect.get(b.getType()).top)
					);
				Vertex rb = new Vertex(
						new Vector2f((b.getHitbox().left + b.getHitbox().width) * this.tileSize.x, (b.getHitbox().top + b.getHitbox().height) * this.tileSize.y),
						new Vector2f(this.texturesRect.get(b.getType()).left, this.texturesRect.get(b.getType()).top)
					);
				Vertex lb = new Vertex(
						new Vector2f(b.getHitbox().left * this.tileSize.x, (b.getHitbox().top + b.getHitbox().height) * this.tileSize.y),
						new Vector2f(this.texturesRect.get(b.getType()).left, this.texturesRect.get(b.getType()).top)
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
		
		target.draw(coloredVA);
		target.draw(texturedVA);
	}
}
