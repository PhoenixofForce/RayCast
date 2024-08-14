package dev.phoenixofforce.math;

import java.util.Optional;

public interface Intersectable {

    Vec2D getStart();
    Vec2D getDirection();
    IntersectionRestraints getRestraints();

    default Optional<Vec2D> findIntersection(Intersectable other) {
        return findIntersection(
            getStart(), getDirection(), getRestraints(),
            other.getStart(), other.getDirection(), other.getRestraints()
        );
    }

    static Optional<Vec2D> findIntersection(Vec2D point1, Vec2D direction1, IntersectionRestraints restraints1, Vec2D point2, Vec2D direction2, IntersectionRestraints restraints2) {
        /*  Line-Line intersection between Ray and the side
              let i be the point of intersection then
                - t, denotes the distance of i from the firstPoint (in direction of secondPoint) such that firstPoint + sideDirection * t = i
                - u, denotes the distance of i from the start (in the given direction dir) such that start + dir * u = i

            When Object1 (point1, direction1, restraint1) is a Beam, so t is the important variable, then
            - all values of t are allowed since a beam goes both ways

            When Object1 (point1, direction1, restraint1) is a Ray, so t is the important variable, then
            - t needs to be bigger than 0, since Rays go forward

            When Object1 (point1, direction1, restraint1) is a Line, so t is the important variable, then
            - t needs to be between 0 and 1, since the intersection needs to be after the start and before the end
         */

        double t = point1.clone().sub(point2).cross(direction1) / direction2.cross(direction1);
        double u = point2.clone().sub(point1).cross(direction2) / direction1.cross(direction2);

        if(restraints1.isIntersectionOnSegmentValid(u) && restraints2.isIntersectionOnSegmentValid(t)) {
            double x  = Math.round(point1.x + u * direction1.x);
            double y  = Math.round(point1.y + u * direction1.y);
            return Optional.of(new Vec2D(x, y));
        }

        return Optional.empty();
    }
}
