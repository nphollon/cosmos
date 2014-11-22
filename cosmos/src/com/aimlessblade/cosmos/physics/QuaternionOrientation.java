package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

public class QuaternionOrientation implements Orientation {
    @Override
    public Orientation rotate(final Displacement rotation) {
        return null;
    }

    @Override
    public boolean isIdentical(final Orientation other, final double tolerance) {
        return false;
    }

    @Override
    public SimpleMatrix toMatrix() {
        return null;
    }
}
