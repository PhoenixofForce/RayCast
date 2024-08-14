package dev.phoenixofforce.raycast;

import dev.phoenixofforce.gameobjects.Obstacle;
import dev.phoenixofforce.math.Line;
import dev.phoenixofforce.math.Ray;
import dev.phoenixofforce.math.Vec2D;
import lombok.Data;

@Data
public class RayCastResult {

    private final Ray raySend;
    private final Obstacle hitObstacle;
    private final Line hitSide;
    private final Vec2D hitPoint;

}
