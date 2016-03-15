package maths;

import org.jsfml.system.Vector2i;
import org.jsfml.system.Vector2f;

/**
 * Various distance algorithms on various types.
 */
public class Distance {
	/**
	 * Returns the manhattan distance between the two points.
	 * @param a : the first point
	 * @param b : the second point
	 * @return the distance between the two points
	 */
	public static float manhattan(Vector2i a, Vector2i b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	/**
	 * Returns the manhattan distance between the two points.
	 * @param a : the first point
	 * @param b : the second point
	 * @return the distance between the two points
	 */
	public static float manhattan(Vector2f a, Vector2f b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	/**
	 * Returns the euclidean distance between the two points.
	 * @param a : the first point
	 * @param b : the second point
	 * @return the distance between the two points
	 */
	public static double euclidean(Vector2i a, Vector2i b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
	public static double euclidean(int xa, int ya, int xb, int yb) {
		return Math.sqrt(Math.pow(xa - xb, 2) + Math.pow(ya - yb, 2));
	}
	
	/**
	 * Returns the euclidean distance between the two points.
	 * @param a : the first point
	 * @param b : the second point
	 * @return the distance between the two points
	 */
	public static double euclidean(Vector2f a, Vector2f b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}
	
	/**
	 * Returns the squared euclidean distance between the two points.
	 * @param a : the first point
	 * @param b : the second point
	 * @return the squared distance between the two points
	 */
	public static double squaredEuclidean(Vector2i a, Vector2i b) {
		return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
	}
	
	/**
	 * Returns the squared euclidean distance between the two points.
	 * @param a : the first point
	 * @param b : the second point
	 * @return the squared distance between the two points
	 */
	public static double squaredEuclidean(Vector2f a, Vector2f b) {
		return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
	}
}
