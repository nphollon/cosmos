package com.aimlessblade.cosmos.geo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@ToString(exclude = "velocityPerSecond")
public final class Pose implements Movable {
    @Getter(AccessLevel.PACKAGE)
    private Displacement displacement;

    @Getter(AccessLevel.PACKAGE)
    private Orientation orientation;

    private Displacement velocityPerSecond;

    public static Pose build(Displacement displacement, Orientation orientation) {
        return new Pose(displacement, orientation);
    }

    @Override
    public SimpleMatrix toMatrix() {
        return displacement.toMatrix().mult(orientation.toMatrix());
    }

    @Override
    public void evolve(final double dt) {
        displacement = displacement.plus(velocityPerSecond.times(dt));
    }

    @Override
    public void impulse(final double vx, final double vy, final double vz) {
        velocityPerSecond = Displacement.cartesian(vx, vy, vz);
    }

    @Override
    public void angularImpulse(final double vPitch, final double vYaw, final double vRoll) {

    }

    boolean isIdentical(final Pose otherPose, final double tolerance) {
        return toMatrix().isIdentical(otherPose.toMatrix(), tolerance);
    }

    private Pose(final Displacement displacement, final Orientation orientation) {
        this.displacement = displacement;
        this.orientation = orientation;
        velocityPerSecond = Displacement.cartesian(0, 0, 0);
    }
}
