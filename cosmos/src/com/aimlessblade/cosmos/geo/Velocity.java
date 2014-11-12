package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "cartesian")
public final class Velocity {
    private final double vx;
    private final double vy;
    private final double vz;

    Displacement overTime(final double secondsElapsed) {
        return Displacement.cartesian(vx * secondsElapsed, vy * secondsElapsed, vz * secondsElapsed);
    }
}
