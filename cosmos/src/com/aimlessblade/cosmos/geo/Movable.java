package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

public interface Movable {
    void evolve(final double dt);

    void impulse(final Velocity velocity);

    void angularImpulse(final Velocity angularVelocity);

    SimpleMatrix toMatrix();
}
