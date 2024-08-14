package dev.phoenixofforce.math;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ray implements Intersectable {

    private Vec2D start;
    private Vec2D direction;

    @Override
    public IntersectionRestraints getRestraints() {
        return IntersectionRestraints.IS_IN_FRONT;
    }
}
