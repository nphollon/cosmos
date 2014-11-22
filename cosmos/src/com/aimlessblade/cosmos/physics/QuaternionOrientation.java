package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

final class QuaternionOrientation implements Orientation {
    private final double qx;
    private final double qy;
    private final double qz;
    private final double qw;

    public QuaternionOrientation(Displacement rotation) {
        final double theta = rotation.getMagnitude() / 2;
        qx = rotation.getX() * Math.sin(theta);
        qy = rotation.getY() * Math.sin(theta);
        qz = rotation.getZ() * Math.sin(theta);
        qw = Math.cos(theta);
    }

    @Override
    public Orientation rotate(final Displacement rotation) {
        return null;
    }

    @Override
    public SimpleMatrix toMatrix() {
        return SimpleMatrix.diag(1, 1, 1, 1);
    }
}
