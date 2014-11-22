package com.aimlessblade.cosmos.physics;

public interface Movable extends ToMatrix {
    void evolve(final double dt);

    void impulse(final Velocity velocity);

    void angularImpulse(final Velocity angularVelocity);
}
