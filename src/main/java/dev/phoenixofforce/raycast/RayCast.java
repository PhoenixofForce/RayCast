package dev.phoenixofforce.raycast;

import dev.phoenixofforce.gameobjects.Obstacle;
import dev.phoenixofforce.math.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    public static List<RayCastResult> castRayIn(List<Obstacle> polygons, Vec2D start, Vec2D dir) {
        List<RayCastResult> possibleDestinations = new ArrayList<>();
        Ray ray = new Ray(start, dir);

        for(Obstacle currentObstacle: polygons) {
            Polygon currentObstacleShape = currentObstacle.getPolygon();

            for(int firstPointIndex = 0; firstPointIndex < currentObstacleShape.npoints; firstPointIndex++) {
                int secondPointIndex = (firstPointIndex + 1) % currentObstacleShape.npoints;

                Vec2D firstPoint = new Vec2D(currentObstacleShape.xpoints[firstPointIndex], currentObstacleShape.ypoints[firstPointIndex]);
                Vec2D secondPoint = new Vec2D(currentObstacleShape.xpoints[secondPointIndex], currentObstacleShape.ypoints[secondPointIndex]);
                Line side = new Line(firstPoint, secondPoint);

                Optional<Vec2D> possibleIntersection = ray.findIntersection(side);
                possibleIntersection.ifPresent(e -> possibleDestinations.add(new RayCastResult(ray, currentObstacle, side, e)));
            }
        }

        possibleDestinations.sort(Comparator.comparingDouble(p -> p.getHitPoint().distanceTo(start)));
        return possibleDestinations;
    }
}
