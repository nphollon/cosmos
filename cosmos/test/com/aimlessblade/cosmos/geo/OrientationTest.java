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
    public void rotationVectorFactoryShouldConvertMagnitudeToRotationAngle() {
        final Orientation fromVector = rotationVector(84, 5, 12);

        final double scale = Math.sin(Math.toRadians(85) * 0.5) / 85;
        final double cosine = Math.cos(Math.toRadians(85) * 0.5);

        final Orientation fromQuaternion = quaternion(scale * 84, scale * 5, scale * 12, cosine);
        assertQuaternionEquality(fromVector, fromQuaternion);
    }

    @Test
    public void nullVectorShouldEqualNullRotation() {
        assertQuaternionEquality(rotationVector(0, 0, 0), zero());
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfNullRotation() {
        Orientation quaternion = rotationVector(0, 0, 0);

        assertMatrixEquality(quaternion.toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldRotateByArbitraryAngle() {
        // Magnitude of rotation vector = 130
        // Therefore, rotate by 130 degrees around axis < 30, 40, 120 >
        Orientation orientation = rotationVector(30, 40, 120);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4, true,
         -0.555302, -0.590470, 0.585649, 0,
          0.823766, -0.487257, 0.289811, 0,
          0.114237,  0.643370, 0.756984, 0,
          0,         0,        0,        1 );

        assertMatrixEquality(orientation.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void isIdenticalShouldBeTrueForExactlyEqualQuaternions() {
        Orientation quaternion1 = quaternion(0, 1, 0, 12);
        Orientation quaternion2 = quaternion(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Orientation quaternion1 = quaternion(-0.000009, 1.000009, 0.000009, 11.999999);
        Orientation quaternion2 = quaternion(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2);
    }

    @Test
    public void isIdenticalShouldBeFalseIfAnyComponentIsDifferent() {
        Orientation referenceOrientation = quaternion(0, 1, 0, 12);
        Orientation badX = quaternion(0.0001, 1, 0, 12);
        Orientation badY = quaternion(0, 0.999, 0, 12);
        Orientation badZ = quaternion(0, 1, 0.0001, 12);
        Orientation badW = quaternion(0, 1, 0, 0.5);

        assertQuaternionInequality(referenceOrientation, badX);
        assertQuaternionInequality(referenceOrientation, badY);
        assertQuaternionInequality(referenceOrientation, badZ);
        assertQuaternionInequality(referenceOrientation, badW);
    }

    @Test
    public void productShouldBeCompositeOfAllComponents() {
        Orientation factorA = quaternion(2, 5, 11, 17);
        Orientation factorB = quaternion(19, 13, 7, 3);
        assertQuaternionEquality(factorA.times(factorB), quaternion(221, 431, 83, -129));
        assertQuaternionEquality(factorB.times(factorA), quaternion(437, 41, 221, -129));
    }

    @Test
    public void normalizeShouldChangeMagnitudeToOneIfItIsNotOne() {
        Orientation quaternion = quaternion(0.5, 0.01, -0.5, 0.3);
        Orientation normalizedQuaternion = quaternion(0.650889, 0.013018, -0.650889, 0.390534);
        assertQuaternionEquality(quaternion.normalize(TOLERANCE), normalizedQuaternion);
    }

    @Test
    public void normalizeShouldNotChangeQuaternionIfCloseToOne() {
        Orientation quaternion = quaternion(1.0000001, 0.000001, 0, 0);
        assertThat(quaternion.normalize(TOLERANCE), is(quaternion));
    }

    @Test
    public void normalizeShouldReturnZeroIfQuaternionIsCloseToZero() {
        Orientation zero = quaternion(0, 0, 0, 0);
        Orientation quaternion = quaternion(0, 0, Double.MIN_VALUE, 0);
        assertThat(quaternion.normalize(TOLERANCE), is(zero));
    }

    private static void assertQuaternionEquality(final Orientation quaternion1, final Orientation quaternion2) {
        String message = "\nActual:\n" + quaternion1 + "\nExpected:\n" + quaternion2;
        assertThat(message, quaternion1.isIdentical(quaternion2, TOLERANCE), is(true));
        assertThat(message, quaternion2.isIdentical(quaternion1, TOLERANCE), is(true));
    }

    private static void assertQuaternionInequality(final Orientation quaternion1, final Orientation quaternion2) {
        String message = "\nActual:\n" + quaternion1 + "\nExpected not to equal:\n" + quaternion2;
        assertThat(message, quaternion1.isIdentical(quaternion2, TOLERANCE), is(false));
        assertThat(message, quaternion2.isIdentical(quaternion1, TOLERANCE), is(false));
    }
}
