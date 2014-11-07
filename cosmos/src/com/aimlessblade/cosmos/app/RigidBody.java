package com.aimlessblade.cosmos.app;

import com.aimlessblade.cosmos.geo.Entity;
import com.aimlessblade.cosmos.geo.Movable;
import org.ejml.simple.SimpleMatrix;

public class RigidBody implements Entity, Movable {
    @Override
    public double[] getVertexData() {
        return new double[0];
    }

    @Override
    public int getVertexCount() {
        return 0;
    }

    @Override
    public int[] getElementData() {
        return new int[0];
    }

    @Override
    public SimpleMatrix getGeoTransform() {
        return null;
    }

    @Override
    public void evolve(final long dt) {

    }

    @Override
    public void impulse(final double vx, final double vy, final double vz) {

    }

    @Override
    public void angularImpulse(final double vPitch, final double vYaw, final double vRoll) {

    }
}
