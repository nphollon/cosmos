package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "cartesian")
public final class Velocity {
    private final double vx;
    private final double vy;
    private final double vz;

    Displacement overTime(final double secondsElapsed) {
        return Displacement.cartesian(vx * secondsElapsed, vy * secondsElapsed, vz * secondsElapsed);
    }

    public Velocity plus(final Velocity addend) {
        return Velocity.cartesian(vx + addend.vx, vy + addend.vy, vz + addend.vz);
    }
}
