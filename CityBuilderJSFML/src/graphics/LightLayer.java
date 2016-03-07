package graphics;

import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

public class LightLayer implements Drawable {
	protected int lastLightId;
	protected Vector2i layerSize;
	protected RenderTexture internalTexture;
	protected Map<Integer, VertexArray> lightsVertexArrays;
	
	/**
	 * Constructor.
	 * 
	 * @throws TextureCreationException 
	 */
	public LightLayer(Vector2i size) throws TextureCreationException {
		this.lastLightId = 0;
		this.layerSize = size;
		this.internalTexture = new RenderTexture();
		this.internalTexture.create(size.x, size.y);
		this.lightsVertexArrays = new HashMap<Integer, VertexArray>();
	}
	
	/**
	 * Removes all the lights.
	 */
	public void clearLights() {
		this.lightsVertexArrays.clear();
	}
	
	/**
	 * Adds a new light to the light layer.
	 * 
	 * @param center : the center of the light
	 * @param radius : the radius of the light
	 * @param color : the color of the light
	 * @return the id of the light
	 */
	public int addLight(Vector2f center, float radius, Color brightColor, Color darkColor) {
		int id = this.lastLightId++;
		
		center = new Vector2f(center.x, this.layerSize.y - center.y);
		
		VertexArray va = new VertexArray(PrimitiveType.TRIANGLE_FAN);
		va.ensureCapacity(64);
		
		va.add(new Vertex(center, brightColor));
		
		final double angle = 2.0 * Math.PI / 64.0;
		
		for(int i = 0 ; i < 64 ; i++) {			
			Vertex v = new Vertex(new Vector2f(
									(float)(center.x + Math.cos(angle * i) * radius),
									(float)(center.y + Math.sin(angle * i) * radius)
								),
								darkColor
							);

			va.add(v);
			
			v = new Vertex(new Vector2f(
					(float)(center.x + Math.cos(angle * (i + 1)) * radius),
					(float)(center.y + Math.sin(angle * (i + 1)) * radius)
				),
				darkColor
			);
			
			va.add(v);
		}
		
		this.lightsVertexArrays.put(id, va);
		
		return id;
	}
	
	/**
	 * Removes a light.
	 * 
	 * @param id : the id of the light to remove
	 */
	public void removeLight(int id) {
		this.lightsVertexArrays.remove(id);
	}
	
	/**
	 * Do a internal drawing to prepare the "real" drawing.
	 * 
	 * @throws TextureCreationException 
	 */
	public void virtualDraw() throws TextureCreationException {
		this.internalTexture.create(this.layerSize.x, this.layerSize.y);
		this.internalTexture.clear(new Color(32, 32, 32, 200));
		
		RenderStates states = new RenderStates(BlendMode.ADD);

		for(Map.Entry<Integer, VertexArray> va : this.lightsVertexArrays.entrySet()) {
			internalTexture.draw(va.getValue(), states);
		}
	}

	/**
	 * Draws the light layer.
	 * 
	 * @param target : the target to draw on
	 * @param states : the states to use
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(states, BlendMode.MULTIPLY);
		
		target.draw(new Sprite(this.internalTexture.getTexture()), newStates);
	}
}
