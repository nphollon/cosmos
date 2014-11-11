package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;

public interface Movable {
    void evolve(double dt);

    void impulse(double vx, double vy, double vz);

    void angularImpulse(double vPitch, double vYaw, double vRoll);

    SimpleMatrix toMatrix();
}
