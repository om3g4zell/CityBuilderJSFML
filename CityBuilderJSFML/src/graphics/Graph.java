package graphics;

import java.util.List;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;

/**
 * Draws a function graph.
 */
public class Graph extends BasicTransformable implements Drawable {
	// Attributes.
	protected int numberOfPoints;
	protected VertexArray vertexArray;
	protected Vector2f size;
	protected List<Vector2f> values;
	
	/**
	 * Constructor.
	 */
	public Graph() {
		this.numberOfPoints = 0;
	}
	
	/**
	 * Sets the size of the graph view.
	 * @param x : width of the view
	 * @param y : height of the view
	 */
	public void setSize(float x, float y) {
		this.size = new Vector2f(x, y);
	}
	
	/**
	 * Adds a value to the graph.
	 * @param x
	 * @param y
	 */
	public void addValue(float x, float y) {
		this.values.add(new Vector2f(x, y));
	}
	
	/**
	 * Updates the graph.
	 * @param dt
	 */
	public void update(Time dt) {
		this.vertexArray.clear();
	    float maxx = 1.f;
	    float maxy = 1.f;

	    int start = 0;
	    int end = Math.min(this.numberOfPoints, this.values.size() - 1);

	    if(this.numberOfPoints < this.values.size())
	    {
	        start = this.values.size() - this.numberOfPoints - 1;
	        end = start + this.numberOfPoints;
	    }

	    for(int i = start ; i < end ; ++i)
	    {
	        if(this.values.get(i).x - this.values.get(start).x > maxx)
	            maxx = this.values.get(i).x;

	        if(this.values.get(i).y > maxy)
	            maxy = this.values.get(i).y;
	    }

	    for(int i = start ; i < end ; ++i)
	    {
	        Vector2f position = new Vector2f((this.values.get(i).x - this.values.get(start).x),
	        					this.size.y - this.size.y * this.values.get(i).y / maxy);
	        Vertex point = new Vertex(position, Color.GREEN);

	        this.vertexArray.add(point);
	    }
	}
	
	/**
	 * Sets the number of points to see on the graph.
	 * 
	 * @param numberOfPoints
	 */
	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	
	/**
	 * Draws the graph.
	 * 
	 * @param target : the target to draw on
	 * @param states : the states to use draw
	 */
	@Override
	public void draw(RenderTarget target, RenderStates states) {
		RenderStates newStates = new RenderStates(Transform.combine(states.transform, this.getTransform()));

		target.draw(this.vertexArray, newStates);
	}
}
