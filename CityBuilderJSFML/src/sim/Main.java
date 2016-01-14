package sim;

import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;



public class Main {
	public static int HEIGHT = 720;
	public static int WIDTH = 1280;
	
	public static boolean tick = false;
	public static boolean render = false;
	protected static final int TILE_SCALE = 32;
	public static String Title = "City Builder";
	public static void main(String[] args) {
		Sim simulation = new Sim(1280,720,Title);
		
		
		int ticks = 0;
		int frames = 0;
		Time elapsed = Time.ZERO;
		Time dt = Time.ZERO;
		Time time = Time.ZERO;
		//Main loop
		Clock clock = new Clock();
		while(Sim.window.isOpen()) {
			
		    dt = clock.restart();
		    elapsed = Time.add(dt, elapsed);
		    tick = false;
			
		    // Update 60 times per seconds
		    if(elapsed.asSeconds() > 1/60f) {
		    	
		    	//Handle events
			    for(Event event : Sim.window.pollEvents()) {
			        if(event.type == Event.Type.CLOSED)
			        	Sim.window.close();
			        else if(event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.ESCAPE)
			        	Sim.window.close();
			    }
			    
		    	elapsed = Time.sub(elapsed,Time.getSeconds(1/60f));
		    	simulation.Update(Time.getSeconds(1/60f));
				ticks++;
		    }
		    
		    simulation.Render();
		    frames++;
		    
		    time = Time.add(dt, time);
		    
		    if(time.asSeconds() > 1f) {
		    	time = Time.sub(time, Time.getSeconds(1.0f));
		    	Sim.window.setTitle(Title  +" ticks : " + ticks + " , FPS : " + frames);
				ticks = 0;
				frames = 0;
			}
		}
		
	}
}
