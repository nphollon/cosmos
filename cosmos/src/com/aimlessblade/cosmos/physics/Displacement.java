package com.aimlessblade.cosmos.physics;

import lombok.*;
import org.ejml.simple.SimpleMatrix;

@EqualsAndHashCode
@ToString
@Getter(AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "cartesian")
public final class Displacement {
    private final double x;
    private final double y;
    private final double z;

    double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    boolean isIdentical(final Displacement other, final double tolerance) {
        return doublesEqual(x, other.x, tolerance) &&
                doublesEqual(y, other.y, tolerance) &&
                doublesEqual(z, other.z, tolerance);
    }

    SimpleMatrix toMatrix() {
        final SimpleMatrix matrix = SimpleMatrix.identity(4);
        matrix.set(0, 3, x);
        matrix.set(1, 3, y);
        matrix.set(2, 3, z);
        return matrix;
    }

    Displacement plus(final Displacement addend) {
        return Displacement.cartesian(x + addend.x, y + addend.y, z + addend.z);
    }

    private static boolean doublesEqual(final double a, final double b, final double tolerance) {
        return Math.abs(a - b) < tolerance;
    }
}