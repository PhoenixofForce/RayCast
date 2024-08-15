package dev.phoenixofforce.gameobjects;

import dev.phoenixofforce.Main;
import dev.phoenixofforce.math.Util;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.*;
import java.awt.geom.PathIterator;

@Data
@AllArgsConstructor
public class Obstacle {

	private Shape shape;
	private Color color;
	private float height;
	private float z;

	public Obstacle(Shape shape) {
		this(shape, Color.WHITE, 1F, (float) (Math.random() * 10.0F));
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

		shape = p;
		return p;
	}

	public Color getColor(double dis) {
		double distanceScore = dis;
		double maxDistanceScore = Main.MAX_DISTANCE + Main.VIEW_ANGLE_BONUS;

		int red = (int) Util.map(distanceScore, 0, maxDistanceScore, color.getRed(), 0);
		int green = (int) Util.map(distanceScore, 0, maxDistanceScore, color.getGreen(), 0);
		int blue = (int) Util.map(distanceScore, 0, maxDistanceScore, color.getBlue(), 0);

		return new Color(red, green, blue);
	}
}
