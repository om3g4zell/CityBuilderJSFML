package sim;

import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

/**
 * Main program/loop.
 */
public class Main {
	// Window constants.
	public static final int HEIGHT = 720;
	public static final int WIDTH = 1280;
	public static final String TITLE = "City Builder";
	
	// Number of frames we can skip to compute simulation logic.
	protected static final int MAX_SKIPPED_FRAMES = 9;
	
	// Tickrate and time per tick.
	protected static final int TICKRATE = 60;
	protected static final Time TIME_PER_TICK = Time.getSeconds(1.f / TICKRATE);

	public static void main(String[] args) {
		// Create a simulation.
		Sim simulation = new Sim(WIDTH, HEIGHT, TITLE);
		simulation.init();
		
		// FPS/ticks count.
		int ticks = 0;
		int frames = 0;
		Time fpsTime = Time.ZERO;
		
		//Main loop
		Clock clock = new Clock();
		Time elapsed = Time.ZERO;
		Time frameTime = Time.ZERO;
		int skippedFrames = 0;
		
		boolean hasFocus = true;
		
		while(simulation.getWindow().isOpen()) {
			frameTime = clock.restart();
		    elapsed = Time.add(frameTime, elapsed);
			
		    // Update 60 times per seconds
		    while(Time.ratio(elapsed, TIME_PER_TICK) >= 1.f && skippedFrames < MAX_SKIPPED_FRAMES) {
				elapsed = Time.sub(elapsed, TIME_PER_TICK);
				skippedFrames++;

		    	// Handle events
			    for(Event event : simulation.getWindow().pollEvents()) {
			        if(event.type == Event.Type.CLOSED) {
			        	simulation.getWindow().close();
			        }
			        else if(event.type == Event.Type.GAINED_FOCUS) {
			        	hasFocus = true;
			        }
			        else if(event.type == Event.Type.LOST_FOCUS) {
			        	hasFocus = false;
			        }
			        else if(event.type == Event.Type.KEY_PRESSED && event.asKeyEvent().key == Keyboard.Key.ESCAPE) {
			        	simulation.getWindow().close();
			        }
			        else if(hasFocus) {
			        	simulation.handleEvent(event);
			        }
			    }
			    
			    if(!hasFocus)
			    	continue;
			    
			    // Update
		    	simulation.update(TIME_PER_TICK);
		    	
		    	// Tick count.
				ticks++;
		    }
		    
		    simulation.render(elapsed);
		    frames++;
		    skippedFrames = 0;
		    
		    // Compute average FPS and display it in the window's title.
		    fpsTime = Time.add(frameTime, fpsTime);
		    if(fpsTime.asSeconds() > 1f) {
		    	fpsTime = Time.sub(fpsTime, Time.getSeconds(1.0f));
		    	simulation.getWindow().setTitle(TITLE  + " | Ticks : " + ticks + ", FPS : " + frames);
				
		    	// Reset counters.
		    	ticks = 0;
				frames = 0;
			}
		}
	}
}
