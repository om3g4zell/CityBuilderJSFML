package maths;

import org.jsfml.system.Vector2i;
import org.jsfml.system.Vector2f;

/*
 * Various distance algorithms on various types.
 */
public class Distance {
	// Returns the manhattan distance between the two points.
	public static float manhattan(Vector2i a, Vector2i b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	// Returns the manhattan distance between the two points.
	public static float manhattan(Vector2f a, Vector2f b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	// Returns the euclidean distance between the two points.
	public static double euclidean(Vector2i a, Vector2i b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
	// Returns the euclidean distance between the two points.
	public static double euclidean(Vector2f a, Vector2f b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
}
