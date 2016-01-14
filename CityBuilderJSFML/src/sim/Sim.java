package sim;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Time;
import org.jsfml.window.VideoMode;

/*
 * Main class contain init, update and render
 */

public class Sim {
	public int WIDTH;
	public int HEIGHT;
	public String TITLE;
	
	public static RenderWindow window = new RenderWindow();
	
	// Constructor
	public Sim(int width, int height,String title) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.TITLE = title;
		window.create(new VideoMode(this.WIDTH,this.HEIGHT), TITLE);
		Init();
		
	}
	
	// Initialisation
	public void Init() {
		
	}
	
	//UPDATE
	public void Update(Time dt) {
		
	}
	//RENDER
	public void Render() {
		
		window.clear(Color.WHITE);
		/////////////
		
		// Draw HERE
		
		/////////////
		window.display();
		
	}
}
