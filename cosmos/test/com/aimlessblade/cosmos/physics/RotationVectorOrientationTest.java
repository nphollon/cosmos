package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static com.aimlessblade.cosmos.util.Assert.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RotationVectorOrientationTest {
    private static final double TOLERANCE = 1e-5;

    private Orientation zero() {
        return new RotationVectorOrientation(Vectors.position(0, 0, 0));
    }

    private Orientation cartesian(final double x, final double y, final double z) {
        return new RotationVectorOrientation(Vectors.position(x, y, z));
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfNullRotation() {
        assertMatrixEquality(zero().toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void isIdenticalShouldBeTrueForExactlyEqualQuaternions() {
        Orientation rotationVectorOrientation1 = cartesian(0, 1, 0);
        Orientation rotationVectorOrientation2 = cartesian(0, 1, 0);
        assertOrientationEquality(rotationVectorOrientation1, rotationVectorOrientation2);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Orientation rotationVectorOrientation1 = cartesian(-0.000009, 1.000009, 11.999999);
        Orientation rotationVectorOrientation2 = cartesian(0, 1, 12);
        assertOrientationEquality(rotationVectorOrientation1, rotationVectorOrientation2);
    }

    @Test
    public void isIdenticalShouldBeFalseIfAnyComponentIsDifferent() {
        Orientation referenceRotationVectorOrientation = cartesian(0, 1, 0);
        Orientation badX = cartesian(0.01, 1, 0);
        Orientation badY = cartesian(0, 0.99, 0);
        Orientation badZ = cartesian(0, 1, 0.01);

        assertOrientationInequality(referenceRotationVectorOrientation, badX);
        assertOrientationInequality(referenceRotationVectorOrientation, badY);
        assertOrientationInequality(referenceRotationVectorOrientation, badZ);
    }

    @Test
    public void toMatrixShouldReturnRotationMatrix() {
        Orientation rotationVectorOrientation = cartesian(7, 5, 3);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4, true,
                1, -3, 5, 0,
                3, 1, -7, 0,
                -5, 7, 1, 0,
                0, 0, 0, 1);

        assertMatrixEquality(rotationVectorOrientation.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void toMatrixShouldNotGiveAccessToInternalState() {
        final Orientation rotationVectorOrientation = zero();

        final SimpleMatrix matrixCopy = rotationVectorOrientation.toMatrix();
        matrixCopy.set(0, 0, 10);

        assertOrientationEquality(rotationVectorOrientation, zero());
    }

    @Test
    public void rotatingZeroOrientationShouldReturnTheRotation() {
        final Displacement rotation = Vectors.position(7, 5, 3);
        final Orientation rotationVectorOrientation = zero();

        final Orientation finalOrientation = rotationVectorOrientation.rotate(rotation);

        assertOrientationEquality(finalOrientation, new RotationVectorOrientation(rotation));
    }

    @Test
    public void rotationsShouldMultiplyMatrices() {
        final Orientation rotationVectorOrientation = cartesian(7, 5, 3);
        final SimpleMatrix initialMatrix = rotationVectorOrientation.toMatrix();

        final Displacement rotation = Vectors.position(-1, -2, -3);
        final SimpleMatrix rotationMatrix = new RotationVectorOrientation(rotation).toMatrix();

        final SimpleMatrix expectedFinalMatrix = rotationMatrix.mult(initialMatrix);

        final Orientation finalOrientation = rotationVectorOrientation.rotate(rotation);

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
