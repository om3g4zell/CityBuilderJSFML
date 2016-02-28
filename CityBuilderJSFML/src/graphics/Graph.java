package graphics;

import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.BasicTransformable;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Transform;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;

/**
 * Draws a function graph.
 */
public class Graph extends BasicTransformable implements Drawable {
	// Attributes.
	protected int numberOfPoints;
	protected VertexArray vertexArray;
	protected VertexArray axesVertexArray;
	protected Vector2f size;
	protected List<Vector2f> values;
	protected boolean drawAxes;
	protected boolean yscaled;
	
	/**
	 * Constructor.
	 */
	public Graph() {
		this.numberOfPoints = 0;
		this.vertexArray = new VertexArray(PrimitiveType.LINE_STRIP);
		this.axesVertexArray = new VertexArray(PrimitiveType.LINES);
		this.size = new Vector2f(0.f, 0.f);
		this.values = new ArrayList<Vector2f>();
		this.drawAxes = false;
		this.yscaled = false;
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
	 */
	public void update() {
		this.vertexArray.clear();
	    float maxx = 1.f;
	    float maxy = 1.f;
	    float miny = Float.MAX_VALUE;

	    int start = 0;
	    int end = Math.min(this.numberOfPoints, this.values.size() - 1);

	    // Get the first and last point.
	    if(this.numberOfPoints < this.values.size())
	    {
	        start = this.values.size() - this.numberOfPoints - 1;
	        end = start + this.numberOfPoints;
	    }

	    // Get the maximum x, maximum y and minimum y.
	    for(int i = start ; i < end ; ++i)
	    {
	        if(this.values.get(i).x - this.values.get(start).x > maxx)
	            maxx = this.values.get(i).x - this.values.get(start).x;

	        if(this.values.get(i).y > maxy)
	            maxy = this.values.get(i).y;
	        
	        if(this.values.get(i).y < miny)
	        	miny = this.values.get(i).y;
	    }

	    // Create vertices.
	    for(int i = start ; i < end ; ++i)
	    {
	    	// Adapt the vertices position.
	    	Vector2f position = new Vector2f(this.size.x * (this.values.get(i).x - this.values.get(start).x) / maxx,
											 this.size.y - this.size.y * (this.values.get(i).y) / maxy);
	    	
	    	if(yscaled) {
	    		position = new Vector2f(this.size.x * (this.values.get(i).x - this.values.get(start).x) / maxx,
	        							this.size.y - this.size.y * (this.values.get(i).y - miny) / maxy);
	    	}
	        
	        Vertex point = new Vertex(position, Color.GREEN);

	        this.vertexArray.add(point);
	    }
	    
	    // Create axes if necessary.
	    if(this.drawAxes) {
	    	Vertex origin = new Vertex(new Vector2f(0, this.size.y), Color.GREEN);
	    	Vertex point = new Vertex(new Vector2f(this.size.x, origin.position.y), Color.GREEN);
	    	this.axesVertexArray.add(origin);
	    	this.axesVertexArray.add(point);
	    	
	    	point = new Vertex(new Vector2f(origin.position.x, 0), Color.GREEN);
	    	this.axesVertexArray.add(origin);
	    	this.axesVertexArray.add(point);
	    }
	}
	
	/**
	 * Sets the number of points to see on the graph.
	 * 
	 * @param numberOfPoints : the number of points to display at once on the graph
	 */
	public void setNumberOfPoints(int numberOfPoints) {
		this.numberOfPoints = numberOfPoints;
	}
	
	/**
	 * Enables/disables the drawing of the axes.
	 * 
	 * @param draw : true to draw the axes, false otherwise
	 */
	public void drawAxes(boolean draw) {
		this.drawAxes = draw;
	}
	
	/**
	 * Enables/disables the scaling of the y axis.
	 * 
	 * @param scale : true to scale the y axis, false otherwise
	 */
	public void scaleYAxis(boolean scale) {
		this.yscaled = scale;
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

		if(this.drawAxes)
			target.draw(this.axesVertexArray, newStates);

		target.draw(this.vertexArray, newStates);
	}
}
