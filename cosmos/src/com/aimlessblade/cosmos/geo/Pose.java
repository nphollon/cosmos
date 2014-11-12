package com.aimlessblade.cosmos.geo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@ToString(exclude = "velocity")
public final class Pose implements Movable {
    @Getter(AccessLevel.PACKAGE)
    private Displacement displacement;

    @Getter(AccessLevel.PACKAGE)
    private Orientation orientation;

    private Velocity velocity;

    public static Pose build(Displacement displacement, Orientation orientation) {
        return new Pose(displacement, orientation);
    }

    @Override
    public SimpleMatrix toMatrix() {
        return displacement.toMatrix().mult(orientation.toMatrix());
    }

    @Override
    public void evolve(final double dt) {
        displacement = displacement.plus(velocity.overTime(dt));
    }

    @Override
    public void impulse(final Velocity velocity) {
        this.velocity = velocity;
    }

    @Override
    public void angularImpulse(AngularVelocity angularVelocity) {

    }

    boolean isIdentical(final Pose otherPose, final double tolerance) {
        return toMatrix().isIdentical(otherPose.toMatrix(), tolerance);
    }

    private Pose(final Displacement displacement, final Orientation orientation) {
        this.displacement = displacement;
        this.orientation = orientation;
        velocity = Velocity.cartesian(0, 0, 0);
    }
}
