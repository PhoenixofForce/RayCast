import java.awt.*;
import java.awt.geom.PathIterator;

public class Obstacle {

	private Shape shape;
	private Color color;
	private float height;
	private boolean highlight;

	public Obstacle(Shape shape) {
		this(shape, Color.WHITE, 1, false);
	}

	public Obstacle(Shape shape, Color color, float height, boolean highlight) {
		this.shape = shape;
		this.color = color;
		this.height = height;
		this.highlight = highlight;
	}

	public Shape getShape() {
		return shape;
	}

	public float getHeight() {
		return height;
	}

	public boolean isHighlighted() {
		return highlight;
	}

	public Polygon getPolygon() {
		if(shape instanceof Polygon polygon) {
			return polygon;
		}

		PathIterator iterator = shape.getPathIterator(null);
		float[] floats = new float[6];

		Polygon p = new Polygon();

		while (!iterator.isDone()) {
			int type = iterator.currentSegment(floats);
			int x = (int) floats[0];
			int y = (int) floats[1];
			if(type != PathIterator.SEG_CLOSE) {
				p.addPoint(x, y);
			}
			iterator.next();
		}

		return p;
	}

	public Color getColor(double dis) {
		double disSq = dis * dis;
		double maxSq = Main.MAX_DISTANCE * Main.MAX_DISTANCE;

		int red = (int)Main.map(disSq, 0, maxSq, color.getRed(), 0);
		int green = (int)Main.map(disSq, 0, maxSq, color.getGreen(), 0);
		int blue = (int)Main.map(disSq, 0, maxSq, color.getBlue(), 0);

		return new Color(red, green, blue);
	}
}
