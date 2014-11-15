package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static com.aimlessblade.cosmos.geo.Orientation.*;
import static com.aimlessblade.cosmos.util.Assert.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrientationTest {
    private static final double TOLERANCE = 1e-5;

    @Test
    public void nullVectorShouldEqualNullRotation() {
        assertOrientationEquality(cartesian(0, 0, 0), zero());
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfNullRotation() {
        assertMatrixEquality(Orientation.zero().toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void isIdenticalShouldBeTrueForExactlyEqualQuaternions() {
        Orientation orientation1 = cartesian(0, 1, 0);
        Orientation orientation2 = cartesian(0, 1, 0);
        assertOrientationEquality(orientation1, orientation2);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Orientation orientation1 = cartesian(-0.000009, 1.000009, 11.999999);
        Orientation orientation2 = cartesian(0, 1, 12);
        assertOrientationEquality(orientation1, orientation2);
    }

    @Test
    public void isIdenticalShouldBeFalseIfAnyComponentIsDifferent() {
        Orientation referenceOrientation = cartesian(0, 1, 0);
        Orientation badX = cartesian(0.01, 1, 0);
        Orientation badY = cartesian(0, 0.99, 0);
        Orientation badZ = cartesian(0, 1, 0.01);

        assertOrientationInequality(referenceOrientation, badX);
        assertOrientationInequality(referenceOrientation, badY);
        assertOrientationInequality(referenceOrientation, badZ);
    }

    @Test
    public void toMatrixShouldReturnRotationMatrix() {
        Orientation orientation = cartesian(7, 5, 3);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4, true,
                1, -3, 5, 0,
                3, 1, -7, 0,
                -5, 7, 1, 0,
                0, 0, 0, 1);

        assertMatrixEquality(orientation.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void toMatrixShouldNotGiveAccessToInternalState() {
        final Orientation orientation = zero();

        final SimpleMatrix matrixCopy = orientation.toMatrix();
        matrixCopy.set(0, 0, 10);

        assertOrientationEquality(orientation, zero());
    }

    @Test
    public void rotatingZeroOrientationShouldReturnTheRotation() {
        final Displacement rotation = Displacement.cartesian(7, 5, 3);
        final Orientation orientation = zero();

        final Orientation finalOrientation = orientation.rotate(rotation);

        assertOrientationEquality(finalOrientation, rotationVector(rotation));
    }

    @Test
    public void rotationsShouldMultiplyMatrices() {
        final Orientation orientation = cartesian(7, 5, 3);
        final SimpleMatrix initialMatrix = orientation.toMatrix();

        final Displacement rotation = Displacement.cartesian(-1, -2, -3);
        final SimpleMatrix rotationMatrix = Orientation.rotationVector(rotation).toMatrix();

        final SimpleMatrix expectedFinalMatrix = rotationMatrix.mult(initialMatrix);

        final Orientation finalOrientation = orientation.rotate(rotation);

        assertMatrixEquality(finalOrientation.toMatrix(), expectedFinalMatrix, TOLERANCE);
    }

    private static void assertOrientationEquality(final Orientation orientation1, final Orientation orientation2) {
        String message = "\nActual:\n" + orientation1 + "\nExpected:\n" + orientation2;
        assertThat(message, orientation1.isIdentical(orientation2, TOLERANCE), is(true));
        assertThat(message, orientation2.isIdentical(orientation1, TOLERANCE), is(true));
    }

    private static void assertOrientationInequality(final Orientation orientation1, final Orientation orientation2) {
        String message = "\nActual:\n" + orientation1 + "\nExpected not to equal:\n" + orientation2;
        assertThat(message, orientation1.isIdentical(orientation2, TOLERANCE), is(false));
        assertThat(message, orientation2.isIdentical(orientation1, TOLERANCE), is(false));
    }
}
