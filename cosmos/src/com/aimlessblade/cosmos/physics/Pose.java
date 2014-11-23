package com.aimlessblade.cosmos.physics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@ToString(exclude = { "velocity", "angularVelocity" })
final class Pose implements Movable {
    @Getter(AccessLevel.PACKAGE)
    private Displacement displacement;

    @Getter(AccessLevel.PACKAGE)
    private Orientation orientation;

    private Velocity velocity;
    private Velocity angularVelocity;

    Pose(final Displacement displacement, final Orientation orientation) {
        this.displacement = displacement;
        this.orientation = orientation;
        velocity = Vectors.velocity(0, 0, 0);
        angularVelocity = Vectors.velocity(0, 0, 0);
    }

    @Override
    public void evolve(final double dt) {
        displacement = displacement.plus(velocity.overTime(dt));
        orientation = orientation.rotate(angularVelocity.overTime(dt));
    }

    @Override
    public void impulse(final Velocity velocityChange) {
        this.velocity = this.velocity.plus(velocityChange);
    }

    @Override
    public void angularImpulse(Velocity angularVelocityChange) {
        this.angularVelocity = this.angularVelocity.plus(angularVelocityChange);
    }

    @Override
    public SimpleMatrix toMatrix() {
        return displacement.toMatrix().mult(orientation.toMatrix());
    }

}
