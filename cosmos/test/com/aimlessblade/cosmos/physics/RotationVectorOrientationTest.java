package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;

public class RotationVectorOrientationTest {

    private Orientation zero() {
        return new RotationVectorOrientation(Vectors.position(0, 0, 0));
    }

    private Orientation cartesian(final double x, final double y, final double z) {
        return new RotationVectorOrientation(Vectors.position(x, y, z));
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfNullRotation() {
        assertMatrixEquality(zero().toMatrix(), SimpleMatrix.identity(4));
    }

    @Test
    public void isIdenticalShouldBeTrueForExactlyEqualQuaternions() {
        Orientation rotationVectorOrientation1 = cartesian(0, 1, 0);
        Orientation rotationVectorOrientation2 = cartesian(0, 1, 0);
        Identity.assertOrientationEquality(rotationVectorOrientation1, rotationVectorOrientation2);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Orientation rotationVectorOrientation1 = cartesian(-0.000009, 1.000009, 11.999999);
        Orientation rotationVectorOrientation2 = cartesian(0, 1, 12);
        Identity.assertOrientationEquality(rotationVectorOrientation1, rotationVectorOrientation2);
    }

    @Test
    public void isIdenticalShouldBeFalseIfAnyComponentIsDifferent() {
        Orientation referenceRotationVectorOrientation = cartesian(0, 1, 0);
        Orientation badX = cartesian(0.01, 1, 0);
        Orientation badY = cartesian(0, 0.99, 0);
        Orientation badZ = cartesian(0, 1, 0.01);

        Identity.assertOrientationInequality(referenceRotationVectorOrientation, badX);
        Identity.assertOrientationInequality(referenceRotationVectorOrientation, badY);
        Identity.assertOrientationInequality(referenceRotationVectorOrientation, badZ);
    }

    @Test
    public void toMatrixShouldReturnRotationMatrix() {
        Orientation rotationVectorOrientation = cartesian(7, 5, 3);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4, true,
                1, -3, 5, 0,
                3, 1, -7, 0,
                -5, 7, 1, 0,
                0, 0, 0, 1);

        assertMatrixEquality(rotationVectorOrientation.toMatrix(), expectedMatrix);
    }

    @Test
    public void toMatrixShouldNotGiveAccessToInternalState() {
        final Orientation rotationVectorOrientation = zero();

        final SimpleMatrix matrixCopy = rotationVectorOrientation.toMatrix();
        matrixCopy.set(0, 0, 10);

        Identity.assertOrientationEquality(rotationVectorOrientation, zero());
    }

    @Test
    public void rotatingZeroOrientationShouldReturnTheRotation() {
        final Displacement rotation = Vectors.position(7, 5, 3);
        final Orientation rotationVectorOrientation = zero();

        final Orientation finalOrientation = rotationVectorOrientation.rotate(rotation);

        Identity.assertOrientationEquality(finalOrientation, new RotationVectorOrientation(rotation));
    }

    @Test
    public void rotationsShouldMultiplyMatrices() {
        final Orientation rotationVectorOrientation = cartesian(7, 5, 3);
        final SimpleMatrix initialMatrix = rotationVectorOrientation.toMatrix();

        final Displacement rotation = Vectors.position(-1, -2, -3);
        final SimpleMatrix rotationMatrix = new RotationVectorOrientation(rotation).toMatrix();

        final SimpleMatrix expectedFinalMatrix = rotationMatrix.mult(initialMatrix);

        final Orientation finalOrientation = rotationVectorOrientation.rotate(rotation);

        assertMatrixEquality(finalOrientation.toMatrix(), expectedFinalMatrix);
    }

}
