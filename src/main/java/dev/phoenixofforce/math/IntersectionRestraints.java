package dev.phoenixofforce.math;

import java.util.function.Function;

public enum IntersectionRestraints {

    ALL(t -> true),                     // For Beams
    IS_ON_LINE(t -> 0 <= t && t <= 1),  //For Rays
    IS_IN_FRONT(t -> 0 <= t);           //For Lines

    private final Function<Double, Boolean> isIntersectionOnSegmentValid;
    IntersectionRestraints(Function<Double, Boolean> isIntersectionOnSegmentValid) {
        this.isIntersectionOnSegmentValid = isIntersectionOnSegmentValid;
    }

    public boolean isIntersectionOnSegmentValid(double t) {
        return isIntersectionOnSegmentValid.apply(t);
    }
}
