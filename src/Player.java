import java.util.Map;

public class Player {

	private Vec2D position, lookingDirection;
	public Player(double x, double y) {
		this.position = new Vec2D(x, y);
		this.lookingDirection = new Vec2D(1, 0);
	}

	public void update(Map<Integer, Boolean> input) {
		if(input.containsKey(81) && input.get(81)) {
			lookingDirection.degRotate(-1);
		}

		if(input.containsKey(69) && input.get(69)) {
			lookingDirection.degRotate(1);
		}

		if(input.containsKey(87) && input.get(87)) {
			position.add(lookingDirection.clone().normalize(1));
		}

		if(input.containsKey(83) && input.get(83)) {
			position.sub(lookingDirection.clone().normalize(1));
		}

		if(input.containsKey(65) && input.get(65)) {
			position.add(Vec2D.getOrthogonalVector(lookingDirection).clone().normalize(1));
		}

		if(input.containsKey(68) && input.get(68)) {
			position.sub(Vec2D.getOrthogonalVector(lookingDirection).clone().normalize(1));
		}
	}

	public Vec2D getPosition() {
		return this.position;
	}

	public void setPosition(Vec2D newPos) {
		this.position = newPos;
	}

	public Vec2D getLookingDirection() {
		return this.lookingDirection;
	}

	public void setLookingDirection(Vec2D newHead) {
		this.lookingDirection = newHead;
	}

}
