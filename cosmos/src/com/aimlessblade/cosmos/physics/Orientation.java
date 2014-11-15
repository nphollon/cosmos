package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;

interface Orientation {
    Orientation rotate(final Displacement rotation);

    boolean isIdentical(final Orientation other, final double tolerance);

    SimpleMatrix toMatrix();
}
