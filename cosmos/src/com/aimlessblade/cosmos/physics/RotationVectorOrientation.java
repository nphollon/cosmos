package com.aimlessblade.cosmos.physics;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@EqualsAndHashCode
@ToString
final class RotationVectorOrientation implements Orientation {
    private final SimpleMatrix matrix;

    RotationVectorOrientation(final Displacement rotation) {
        matrix = vectorToTensor(rotation);
    }

    private RotationVectorOrientation(final SimpleMatrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public Orientation rotate(final Displacement rotation) {
        return new RotationVectorOrientation(vectorToTensor(rotation).mult(matrix));
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

    @Override
    public SimpleMatrix toMatrix() {
        return matrix.copy();
    }
}
