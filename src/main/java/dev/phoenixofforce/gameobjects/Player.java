package dev.phoenixofforce.gameobjects;

import dev.phoenixofforce.frame.control.KeyInputs;
import dev.phoenixofforce.math.Line;
import dev.phoenixofforce.math.Vec2D;
import dev.phoenixofforce.raycast.RayCastResult;
import lombok.Data;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
public class Player {

	private Vec2D position, lookingDirection;

	public Player(double x, double y) {
		this.position = new Vec2D(x, y);
		this.lookingDirection = new Vec2D(1, 0);
	}

	public void update(KeyInputs input, List<Obstacle> obstacles) {
		Vec2D newPos = position.clone();
		if(input.isPressed(81)) {
			lookingDirection.degRotate(-1);
		}

		if(input.isPressed(69)) {
			lookingDirection.degRotate(1);
		}

		if(input.isPressed(87)) {
			newPos.add(lookingDirection.clone().normalize(1));
		}

		if(input.isPressed(83)) {
			newPos.sub(lookingDirection.clone().normalize(1));
		}

		if(input.isPressed(65)) {
			newPos.add(Vec2D.getOrthogonalVector(lookingDirection).clone().normalize(1));
		}

		if(input.isPressed(68)) {
			newPos.sub(Vec2D.getOrthogonalVector(lookingDirection).clone().normalize(1));
		}

		Line playerMovement = new Line(position, newPos);
		for(Obstacle obstacle: obstacles) {
			Polygon p = obstacle.getPolygon();
			for(int firstPointIndex = 0; firstPointIndex < p.npoints; firstPointIndex++) {
				int secondPointIndex = (firstPointIndex + 1) % p.npoints;

				Vec2D firstPoint = new Vec2D(p.xpoints[firstPointIndex], p.ypoints[firstPointIndex]);
				Vec2D secondPoint = new Vec2D(p.xpoints[secondPointIndex], p.ypoints[secondPointIndex]);
				Line side = new Line(firstPoint, secondPoint);

				Optional<Vec2D> possibleIntersection = playerMovement.findIntersection(side);
				if(possibleIntersection.isPresent()) {
					Vec2D intersection = possibleIntersection.get();
					Vec2D offset = intersection.clone().sub(position);
					newPos = position.clone().add(offset.sub(4, 4).max(0));
					playerMovement = new Line(position, newPos);
				}
			}
		}
		position = newPos;
	}

}
