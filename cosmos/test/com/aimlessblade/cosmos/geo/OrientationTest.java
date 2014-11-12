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
        assertQuaternionEquality(rotationVector(Displacement.cartesian(3, 0, 0)), axisAngle(1, 0, 0, 3));
        assertQuaternionEquality(rotationVector(Displacement.cartesian(0, 4, 0)), axisAngle(0, 1, 0, 4));
        assertQuaternionEquality(rotationVector(Displacement.cartesian(0, 0, 2)), axisAngle(0, 0, 1, 2));
        assertQuaternionEquality(rotationVector(Displacement.cartesian(3, 0, 4)), axisAngle(0.6, 0, 0.8, 5));
    }

    @Test
    public void nullVectorShouldEqualNullRotation() {
        assertQuaternionEquality(rotationVector(Displacement.cartesian(0, 0, 0)), axisAngle(1, 0, 0, 0));
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfNullRotation() {
        Orientation quaternion = rotationVector(Displacement.cartesian(0, 0, 0));

        assertMatrixEquality(quaternion.toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldRotateByArbitraryAngle() {
        Orientation quaternion = axisAngle(0.632456, 0.6, 0.2, 10.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 0, 0.993923);
        expectedMatrix.set(0, 1, -0.028965);
        expectedMatrix.set(0, 2, 0.106111);
        expectedMatrix.set(1, 0, 0.040495);
        expectedMatrix.set(1, 1, 0.993315);
        expectedMatrix.set(1, 2, -0.108002);
        expectedMatrix.set(2, 0, -0.102267);
        expectedMatrix.set(2, 1, 0.111648);
        expectedMatrix.set(2, 2, 0.988454);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
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
