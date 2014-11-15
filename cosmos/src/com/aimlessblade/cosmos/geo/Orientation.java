package com.aimlessblade.cosmos.geo;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@EqualsAndHashCode
@ToString
public final class Orientation {
    private SimpleMatrix matrix;

    private Orientation(final SimpleMatrix matrix) {
        this.matrix = matrix;
    }

    public static Orientation zero() {
        return cartesian(0, 0, 0);
    }

    static Orientation cartesian(final double rx, final double ry, final double rz) {
        return Orientation.rotationVector(Displacement.cartesian(rx, ry, rz));
    }

    static Orientation rotationVector(final Displacement rotation) {
        return new Orientation(vectorToTensor(rotation));
    }


    public Orientation rotate(final Displacement rotation) {
        return new Orientation(vectorToTensor(rotation).mult(matrix));
    }

    private static SimpleMatrix vectorToTensor(final Displacement vector) {
        final double x = vector.getX();
        final double y = vector.getY();
        final double z = vector.getZ();

        return new SimpleMatrix(4, 4, true,
                1, -z, y, 0,
                z, 1, -x, 0,
                -y, x, 1, 0,
                0, 0, 0, 1);
    }

    boolean isIdentical(final Orientation other, final double tolerance) {
        return matrix.isIdentical(other.matrix, tolerance);
    }

    SimpleMatrix toMatrix() {
        return matrix.copy();
    }
}
