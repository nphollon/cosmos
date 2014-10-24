package com.aimlessblade.cosmos.geo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.ejml.simple.SimpleMatrix;

@EqualsAndHashCode
@ToString
@AllArgsConstructor(staticName = "cartesian")
public class Displacement {
    private final double x;
    private final double y;
    private final double z;

    public boolean isIdentical(final Displacement other, final double tolerance) {
        return doublesEqual(x, other.x, tolerance) &&
                doublesEqual(y, other.y, tolerance) &&
                doublesEqual(z, other.z, tolerance);
    }

    public boolean doublesEqual(final double a, final double b, final double tolerance) {
        return Math.abs(a - b) < tolerance;
    }

    public SimpleMatrix toMatrix() {
        SimpleMatrix matrix = SimpleMatrix.identity(4);
        matrix.set(0, 3, x);
        matrix.set(1, 3, y);
        matrix.set(2, 3, z);
        return matrix;
    }

    public Displacement plus(final Displacement addend) {
        return Displacement.cartesian(x + addend.x, y + addend.y, z + addend.z);
    }
}