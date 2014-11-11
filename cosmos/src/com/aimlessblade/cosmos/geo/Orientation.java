package com.aimlessblade.cosmos.geo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Orientation {
    private static final Orientation NULL_QUATERNION = quaternion(0, 0, 0, 0);
    private static final Orientation NULL_ROTATION = quaternion(0, 0, 0, 1);
    private final double x;
    private final double y;
    private final double z;
    private final double w;

    public static Orientation rotationVector(final double rx, final double ry, final double rz) {
        final double magnitudeSquared = rx * rx + ry * ry + rz * rz;

        if (magnitudeSquared == 0) {
            return NULL_ROTATION;
        }

        final double magnitude = Math.sqrt(magnitudeSquared);
        return axisAngle(rx/magnitude, ry/magnitude, rz/magnitude, magnitude);
    }

    public static Orientation axisAngle(final double x, final double y, final double z, final double angle) {
        return axisAngleRadians(x, y, z, Math.toRadians(angle));
    }

    public static Orientation axisAngleRadians(final double x, final double y, final double z, final double angle) {
        final double qx = x*Math.sin(angle / 2);
        final double qy = y*Math.sin(angle / 2);
        final double qz = z*Math.sin(angle / 2);
        final double qw = Math.cos(angle / 2);
        return quaternion(qx, qy, qz, qw);
    }

    public static Orientation quaternion(final double x, final double y, final double z, final double w) {
        return new Orientation(x, y, z, w);
    }

    public SimpleMatrix toMatrix() {
        final SimpleMatrix matrix = new SimpleMatrix(4, 4);

        matrix.set(0, 0, 1 - 2*y*y - 2*z*z);
        matrix.set(0, 1, 2*x*y - 2*z*w);
        matrix.set(0, 2, 2*x*z + 2*y*w);

        matrix.set(1, 0, 2*x*y + 2*z*w);
        matrix.set(1, 1, 1 - 2*x*x - 2*z*z);
        matrix.set(1, 2, 2*y*z - 2*x*w);

        matrix.set(2, 0, 2*x*z -2*y*w);
        matrix.set(2, 1, 2*y*z + 2*x*w);
        matrix.set(2, 2, 1 - 2*y*y - 2*x*x);

        matrix.set(3, 3, 1);
        return matrix;
    }

    public Orientation times(final Orientation factor) {
        return quaternion(
                w * factor.x + x * factor.w + y * factor.z - z * factor.y,
                w * factor.y + y * factor.w + z * factor.x - x * factor.z,
                w * factor.z + z * factor.w + x * factor.y - y * factor.x,
                w * factor.w - x * factor.x - y * factor.y - z * factor.z
        );
    }

    public Orientation normalize(final double tolerance) {
        final double magnitudeSquared = x*x + y*y + z*z + w*w;

        if (doublesEqual(magnitudeSquared, 1, tolerance)) {
            return this;
        }

        if (magnitudeSquared == 0) {
            return NULL_QUATERNION;
        }

        final double magnitude = Math.sqrt(magnitudeSquared);
        return quaternion(x / magnitude, y / magnitude, z / magnitude, w / magnitude);
    }

    public boolean isIdentical(final Orientation other, final double tolerance) {
        return doublesEqual(x, other.x, tolerance) &&
                doublesEqual(y, other.y, tolerance) &&
                doublesEqual(z, other.z, tolerance) &&
                doublesEqual(w, other.w, tolerance);
    }

    private boolean doublesEqual(final double a, final double b, final double tolerance) {
        return Math.abs(a - b) < tolerance;
    }
}
