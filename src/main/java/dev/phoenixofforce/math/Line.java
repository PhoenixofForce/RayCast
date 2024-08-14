package dev.phoenixofforce.math;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Line implements Intersectable {

    private Vec2D start;
    private Vec2D end;

    public Vec2D getDirection() {
        return end.clone().sub(start);
    }

    @Override
    public IntersectionRestraints getRestraints() {
        return IntersectionRestraints.IS_ON_LINE;
    }

}
