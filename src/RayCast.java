import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RayCast {

    /**
     * Casts a ray from a given start point in a given direction, and calculates intersection with given obstacles
     * The function calculates the intersection of a polygon by calculating the intersection with each side
     * The function calculates intersection of a side by calculating the intersection of two rays
     *   - the first ray is the sideDirection from the firstPoint to the secondPoint
     *   - the second ray is the given ray
     *
     *                  +-------+ p
     *                  |       |
     *                  |   ^   |
     *                  |  /    |
     *  firstPoint ->   +-?----->   <- secondPoint
     *                   /      ^
     *                  /       | sideDirection
     *         dir ->  /
     *               * <- start
     *
     *   ? - possible intersection
     *   p - current polygon
     *
     * @return list of intersections, sorted by distance to the start
     */
    public static List<Pair> castRayIn(List<Obstacle> polygons, Vec2D start, Vec2D dir) {
        List<Pair> possibleDestinations = new ArrayList<>();

        for(Obstacle currentObstacle: polygons) {
            Polygon currentObstacleShape = currentObstacle.getPolygon();

            for(int i = 0; i < currentObstacleShape.npoints; i++) {
                int i2 = (i + 1) % currentObstacleShape.npoints;

                Vec2D firstPoint = new Vec2D(currentObstacleShape.xpoints[i], currentObstacleShape.ypoints[i]);
                Vec2D secondPoint = new Vec2D(currentObstacleShape.xpoints[i2], currentObstacleShape.ypoints[i2]);

                Vec2D sideDirection = secondPoint.clone().sub(firstPoint);

                /*  Line-Line intersection between Ray and the side
                      let i be the point of intersection then
                        - t, denotes the distance of i from the firstPoint (in direction of secondPoint) such that firstPoint + sideDirection * t = i
                        - u, denotes the distance of i from the start (in the given direction dir) such that start + dir * u = i
                      The following conditions must be met, such the intersection lies on the current side
                        - u >= 0, if u would be smaller than 0, the given direction would be reversed
                        - 0 <= t <= 1, t values outside this range, would mean an intersection outside the given side
                 */
                double t = start.clone().sub(firstPoint).cross(dir) / sideDirection.cross(dir);
                double u = firstPoint.clone().sub(start).cross(sideDirection) / dir.cross(sideDirection);

                boolean intersectionIsInGivenDirection = u >= 0;
                boolean intersectionLaysOnSide = 0 <= t && t <= 1.0f;
                if(intersectionIsInGivenDirection && intersectionLaysOnSide) {
                    double x  = Math.round(start.x + u * dir.x);
                    double y  = Math.round(start.y + u * dir.y);
                    possibleDestinations.add(new Pair(currentObstacle, new Vec2D(x, y)));
                }
            }
        }

        possibleDestinations.sort(Comparator.comparingDouble(p -> p.point.distanceTo(start)));
        return possibleDestinations;
    }

}
