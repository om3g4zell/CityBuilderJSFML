package sim;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.window.VideoMode;

/*
 * Simulation class.
 * Contains init, update and render.
 */
public class Sim {
	// Attributes.
	protected RenderWindow window;
	
	// Constructor
	public Sim(int width, int height, String title) {
		this.window = new RenderWindow(new VideoMode(width, height), title);
	}
	
	// Initialization
	public void init() {
		
	}
	
	// Updates all the simulation.
	public void update(Time dt) {
		
	}
	
	// Renders all the simulation.
	public void render() {
		this.window.clear(Color.WHITE);
		/////////////
		
		// Draw HERE
		
		/////////////
		this.window.display();
		
	}
	
	// Returns the window.
	public RenderWindow getWindow() {
		return this.window;
	}
}
