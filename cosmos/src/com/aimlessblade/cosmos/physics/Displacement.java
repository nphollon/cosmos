package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

public interface Displacement {
    boolean isIdentical(final Displacement other, final double tolerance);

    SimpleMatrix toMatrix();

    Displacement plus(final Displacement addend);

    double getX();
    double getY();
    double getZ();
}
