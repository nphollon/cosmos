package com.aimlessblade.cosmos.physics;

import lombok.*;
import org.ejml.simple.SimpleMatrix;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class CartesianDisplacement implements Displacement {
    private final double x;
    private final double y;
    private final double z;

    @Override
    public SimpleMatrix toMatrix() {
        final SimpleMatrix matrix = SimpleMatrix.identity(4);
        matrix.set(0, 3, x);
        matrix.set(1, 3, y);
        matrix.set(2, 3, z);
        return matrix;
    }

    @Override
    public Displacement plus(final Displacement addend) {
        return Vectors.position(x + addend.getX(), y + addend.getY(), z + addend.getZ());
    }

    @Override
    public double getMagnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }
}
