package com.aimlessblade.cosmos.physics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
class CartesianVelocity implements Velocity {
    private final double vx;
    private final double vy;
    private final double vz;

    @Override
    public Displacement overTime(final double secondsElapsed) {
        return Vectors.position(vx * secondsElapsed, vy * secondsElapsed, vz * secondsElapsed);
    }

    @Override
    public Velocity plus(final Velocity addend) {
        Displacement d = addend.overTime(1);
        return new CartesianVelocity(vx + d.getX(), vy + d.getY(), vz + d.getZ());
    }

    @Override
    public Velocity negative() {
        return new CartesianVelocity(-vx, -vy, -vz);
    }
}
