package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

final class QuaternionOrientation implements Orientation {
    private final double qx;
    private final double qy;
    private final double qz;
    private final double qw;

    public QuaternionOrientation(Displacement rotation) {
        final double magnitude = rotation.getMagnitude();

        if (magnitude == 0) {
            qx = 0;
            qy = 0;
            qz = 0;
            qw = 1;
        } else {
            final double theta = magnitude / 2;
            qx = rotation.getX() * Math.sin(theta) / magnitude;
            qy = rotation.getY() * Math.sin(theta) / magnitude;
            qz = rotation.getZ() * Math.sin(theta) / magnitude;
            qw = Math.cos(theta);
        }
    }

    public QuaternionOrientation(final double x, final double y, final double z, final double w) {
        final double magnitude = Math.sqrt(x * x + y * y + z * z + w * w);
        qx = x / magnitude;
        qy = y / magnitude;
        qz = z / magnitude;
        qw = w / magnitude;
    }

    @Override
    public Orientation rotate(final Displacement rotation) {
        QuaternionOrientation r = new QuaternionOrientation(rotation);
        return new QuaternionOrientation(
                qw * r.qx + qx * r.qw - qy * r.qz + qz * r.qy,
                qw * r.qy + qy * r.qw - qz * r.qx + qx * r.qz,
                qw * r.qz + qz * r.qw - qx * r.qy + qy * r.qx,
                qw * r.qw - qx * r.qx - qy * r.qy - qz * r.qz
                );
    }

    @Override
    public SimpleMatrix toMatrix() {
        SimpleMatrix m1 = new SimpleMatrix(4,4,true,
                qw, -qz, qy, qx,
                qz, qw, -qx, qy,
                -qy, qx, qw, qz,
                -qx, -qy, -qz, qw);

        SimpleMatrix m2 = new SimpleMatrix(4,4,true,
                qw, -qz, qy, -qx,
                qz, qw, -qx, -qy,
                -qy, qx, qw, -qz,
                qx, qy, qz, qw);

        return m1.mult(m2);
    }
}
