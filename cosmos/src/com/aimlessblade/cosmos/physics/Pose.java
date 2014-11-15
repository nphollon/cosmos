package com.aimlessblade.cosmos.physics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@ToString(exclude = { "velocity", "angularVelocity" })
public final class Pose implements Movable {
    @Getter(AccessLevel.PACKAGE)
    private Displacement displacement;

    @Getter(AccessLevel.PACKAGE)
    private Orientation orientation;

    private Velocity velocity;
    private Velocity angularVelocity;

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
        orientation = orientation.rotate(angularVelocity.overTime(dt));
    }

    @Override
    public void impulse(final Velocity velocity) {
        this.velocity = this.velocity.plus(velocity);
    }

    @Override
    public void angularImpulse(Velocity angularVelocity) {
        this.angularVelocity = this.angularVelocity.plus(angularVelocity);
    }

    boolean isIdentical(final Pose otherPose, final double tolerance) {
        return displacement.isIdentical(otherPose.displacement, tolerance) &&
                orientation.isIdentical(otherPose.orientation, tolerance);
    }

    private Pose(final Displacement displacement, final Orientation orientation) {
        this.displacement = displacement;
        this.orientation = orientation;
        velocity = Velocity.cartesian(0, 0, 0);
        angularVelocity = Velocity.cartesian(0, 0, 0);
    }
}
