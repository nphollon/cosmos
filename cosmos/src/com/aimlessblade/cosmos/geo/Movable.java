package com.aimlessblade.cosmos.geo;

public interface Movable {
    void evolve(long dt);

    void impulse(double vx, double vy, double vz);

    void angularImpulse(double vPitch, double vYaw, double vRoll);
}
