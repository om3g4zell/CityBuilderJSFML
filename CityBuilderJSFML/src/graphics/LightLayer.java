package graphics;

import java.io.IOException;
import java.nio.file.Paths;
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
	protected Shader shader;
	protected int alpha;
	
	/**
	 * Constructor.
	 * 
	 * @throws TextureCreationException 
	 * @throws ShaderSourceException 
	 * @throws IOException 
	 */
	public LightLayer(Vector2i size) throws TextureCreationException, IOException, ShaderSourceException {
		this.lastLightId = 0;
		this.layerSize = size;
		this.internalTexture = new RenderTexture();
		this.internalTexture.create(size.x, size.y);
		this.lightsVertexArrays = new HashMap<Integer, VertexArray>();
		
		this.shader = new Shader();
		this.shader.loadFromFile(Paths.get("res/shaders/blur.frag"), Shader.Type.FRAGMENT);
		this.shader.setParameter("blur_radius", (float)0.005f);
		
		this.alpha = 0;
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
	 * Interpolates the alpha.
	 * 
	 * @throws TextureCreationException 
	 */
	public void interpolateAlpha(int last, int current, int end) throws TextureCreationException {
		
		
		this.virtualDraw();
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
	 * Do an internal drawing to prepare the "real" drawing.
	 * 
	 * @throws TextureCreationException 
	 */
	public void virtualDraw() throws TextureCreationException {
		this.internalTexture.create(this.layerSize.x, this.layerSize.y);
		
		if(this.alpha > 32)
			this.internalTexture.clear(new Color(this.alpha, this.alpha, this.alpha));
		else
			this.internalTexture.clear(new Color(32, 32, 32));
		
		RenderStates states = new RenderStates(BlendMode.ADD);

		for(Map.Entry<Integer, VertexArray> va : this.lightsVertexArrays.entrySet()) {
			this.internalTexture.draw(va.getValue(), states);
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
		newStates = new RenderStates(newStates, this.shader);
		
		Sprite sprite = new Sprite(this.internalTexture.getTexture());
		sprite.setColor(new Color(255, 255, 255));
		
		target.draw(sprite, newStates);
	}
	
	/**
	 * Changes the rendering alpha.
	 * 
	 * @param alpha : the alpha to use
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
		//System.out.println("alpha: " + this.alpha);
	}
	
	/**
	 * Returns the rendering alpha.
	 */
	public int getAlpha() {
		return this.alpha;
	}
}
