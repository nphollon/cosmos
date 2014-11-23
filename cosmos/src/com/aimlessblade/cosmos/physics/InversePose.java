package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

public class InversePose implements Movable {
    private Displacement displacement;
    private Orientation orientation;
    private Velocity velocity;
    private Velocity angularVelocity;

    public InversePose(Displacement displacement, Orientation orientation) {
        this.displacement = displacement;
        this.orientation = orientation;

        this.velocity = Vectors.velocity(0, 0, 0);
        this.angularVelocity = Vectors.velocity(0, 0, 0);
    }

    @Override
    public void evolve(double dt) {
        displacement = displacement.plus(velocity.overTime(dt));
        orientation = orientation.rotate(angularVelocity.overTime(dt));
    }

    @Override
    public void impulse(Velocity velocityChange) {
        this.velocity = this.velocity.plus(velocityChange.negative());
    }

    @Override
    public void angularImpulse(Velocity angularVelocityChange) {
        this.angularVelocity = this.angularVelocity.plus(angularVelocityChange.negative());
    }

    @Override
    public SimpleMatrix toMatrix() {
        return orientation.toMatrix().mult(displacement.toMatrix());
    }
}
