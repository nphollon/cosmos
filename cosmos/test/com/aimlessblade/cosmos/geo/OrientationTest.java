package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrientationTest {

    private static final double TOLERANCE = 1e-5;

    @Test
    public void rotationMatrixShouldBeIdentityIfWIsZero() {
        Orientation quaternion = Orientation.axisAngleDegrees(1.0, 0.0, 0.0, 0.0);

        assertMatrixEquality(quaternion.toMatrix(), SimpleMatrix.identity(4));
    }

    @Test
    public void rotationMatrixShouldChangePitchIfAxisIsX() {
        Orientation quaternion = Orientation.axisAngleDegrees(1.0, 0.0, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 0, 1);
        expectedMatrix.set(1, 2, -1);
        expectedMatrix.set(2, 1, 1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldChangeYawIfAxisIsY() {
        Orientation quaternion = Orientation.axisAngleDegrees(0.0, 1.0, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 2, 1);
        expectedMatrix.set(1, 1, 1);
        expectedMatrix.set(2, 0, -1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldChangeRollIfAxisIsZ() {
        Orientation quaternion = Orientation.axisAngleDegrees(0.0, 0.0, 1.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 1, -1);
        expectedMatrix.set(1, 0, 1);
        expectedMatrix.set(2, 2, 1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldRotateAroundXZDiagonal() {
        Orientation quaternion = Orientation.axisAngleDegrees(0.707107, 0.0, 0.707107, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 0, 0.5);
        expectedMatrix.set(0, 1, -0.707107);
        expectedMatrix.set(0, 2, 0.5);
        expectedMatrix.set(1, 0, 0.707107);
        expectedMatrix.set(1, 2, -0.707107);
        expectedMatrix.set(2, 0, 0.5);
        expectedMatrix.set(2, 1, 0.707107);
        expectedMatrix.set(2, 2, 0.5);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldRotateAroundXYDiagonal() {
        Orientation quaternion = Orientation.axisAngleDegrees(0.707107, 0.707107, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 0, 0.5);
        expectedMatrix.set(0, 1, 0.5);
        expectedMatrix.set(0, 2, 0.707107);
        expectedMatrix.set(1, 0, 0.5);
        expectedMatrix.set(1, 1, 0.5);
        expectedMatrix.set(1, 2, -0.707107);
        expectedMatrix.set(2, 0, -0.707107);
        expectedMatrix.set(2, 1, 0.707107);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldRotateAroundYZDiagonal() {
        Orientation quaternion = Orientation.axisAngleDegrees(0.0, 0.707107, 0.707107, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 1, -0.707107);
        expectedMatrix.set(0, 2, 0.707107);
        expectedMatrix.set(1, 0, 0.707107);
        expectedMatrix.set(1, 1, 0.5);
        expectedMatrix.set(1, 2, 0.5);
        expectedMatrix.set(2, 0, -0.707107);
        expectedMatrix.set(2, 1, 0.5);
        expectedMatrix.set(2, 2, 0.5);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldRotateByArbitraryAngle() {
        Orientation quaternion = Orientation.axisAngleDegrees(0.632456, 0.6, 0.2, 10.0);

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

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void quaternionFactoryWithRadiansShouldMatchFactoryWithDegrees() {
        Orientation degreeQuaternion = Orientation.axisAngleDegrees(1, 0, 0, 180);
        Orientation radianQuaternion = Orientation.axisAngle(1, 0, 0, Math.PI);

        assertThat(degreeQuaternion, is(radianQuaternion));
    }

    @Test
    public void isIdenticalShouldBeTrueForExactlyEqualQuaternions() {
        Orientation quaternion1 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        Orientation quaternion2 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, true);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Orientation quaternion1 = Orientation.axisAngleDegrees(-0.000009, 1.000009, 0.000009, 11.999999);
        Orientation quaternion2 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, true);
    }

    @Test
    public void isIdenticalShouldBeFalseIfXsAreDifferent() {
        Orientation quaternion1 = Orientation.axisAngleDegrees(0.0001, 1, 0, 12);
        Orientation quaternion2 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfYsAreDifferent() {
        Orientation quaternion1 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        Orientation quaternion2 = Orientation.axisAngleDegrees(0, 0.999, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfZsAreDifferent() {
        Orientation quaternion1 = Orientation.axisAngleDegrees(0, 1, 0.0001, 12);
        Orientation quaternion2 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfWsAreDifferent() {
        // Because the quaternion quaternion are products of the factory parameters
        //   very different quaternions can have identical Y parameters
        Orientation quaternion1 = Orientation.axisAngleDegrees(0, 1, 0, 12);
        Orientation quaternion2 = Orientation.axisAngleDegrees(0, 0.104528, 0, 180);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void productXXShouldGoToW() {
        Orientation factorA = Orientation.quaternion(2, 0, 0, 0);
        Orientation factorB = Orientation.quaternion(3, 0, 0, 0);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 0, 0, -6));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 0, 0, -6));
    }

    @Test
    public void productXYShouldGoToZ() {
        Orientation factorA = Orientation.quaternion(2, 0, 0, 0);
        Orientation factorB = Orientation.quaternion(0, 3, 0, 0);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 0, 6, 0));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 0, -6, 0));
    }

    @Test
    public void productXZShouldGoToY() {
        Orientation factorA = Orientation.quaternion(2, 0, 0, 0);
        Orientation factorB = Orientation.quaternion(0, 0, 3, 0);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, -6, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 6, 0, 0));
    }

    @Test
    public void productXWShouldGoToX() {
        Orientation factorA = Orientation.quaternion(2, 0, 0, 0);
        Orientation factorB = Orientation.quaternion(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(6, 0, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(6, 0, 0, 0));
    }

    @Test
    public void productYYShouldGoToW() {
        Orientation factorA = Orientation.quaternion(0, 2, 0, 0);
        Orientation factorB = Orientation.quaternion(0, 3, 0, 0);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 0, 0, -6));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 0, 0, -6));
    }

    @Test
    public void productYZShouldGoToX() {
        Orientation factorA = Orientation.quaternion(0, 2, 0, 0);
        Orientation factorB = Orientation.quaternion(0, 0, 3, 0);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(6, 0, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(-6, 0, 0, 0));
    }

    @Test
    public void productYWShouldGoToY() {
        Orientation factorA = Orientation.quaternion(0, 2, 0, 0);
        Orientation factorB = Orientation.quaternion(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 6, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 6, 0, 0));
    }

    @Test
    public void productZZShouldGoToW() {
        Orientation factorA = Orientation.quaternion(0, 0, 2, 0);
        Orientation factorB = Orientation.quaternion(0, 0, 3, 0);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 0, 0, -6));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 0, 0, -6));
    }

    @Test
    public void productZWShouldGoToZ() {
        Orientation factorA = Orientation.quaternion(0, 0, 2, 0);
        Orientation factorB = Orientation.quaternion(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 0, 6, 0));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 0, 6, 0));
    }

    @Test
    public void productWWShouldGoToW() {
        Orientation factorA = Orientation.quaternion(0, 0, 0, 2);
        Orientation factorB = Orientation.quaternion(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(0, 0, 0, 6));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(0, 0, 0, 6));
    }

    @Test
    public void productShouldBeCompositeOfAllComponents() {
        Orientation factorA = Orientation.quaternion(2, 5, 11, 17);
        Orientation factorB = Orientation.quaternion(19, 13, 7, 3);
        assertQuaternionEquality(factorA.times(factorB), Orientation.quaternion(221, 431, 83, -129));
        assertQuaternionEquality(factorB.times(factorA), Orientation.quaternion(437, 41, 221, -129));
    }

    @Test
    public void normalizeShouldChangeMagnitudeToOneIfItIsNotOne() {
        Orientation quaternion = Orientation.quaternion(0.5, 0.01, -0.5, 0.3);
        Orientation normalizedQuaternion = Orientation.quaternion(0.650889, 0.013018, -0.650889, 0.390534);
        assertQuaternionEquality(quaternion.normalize(TOLERANCE), normalizedQuaternion);
    }

    @Test
    public void normalizeShouldNotChangeQuaternionIfCloseToOne() {
        Orientation quaternion = Orientation.quaternion(1.0000001, 0.000001, 0, 0);
        assertThat(quaternion.normalize(TOLERANCE), is(quaternion));
    }

    @Test
    public void normalizeShouldReturnZeroIfQuaternionIsCloseToZero() {
        Orientation zero = Orientation.quaternion(0, 0, 0, 0);
        Orientation quaternion = Orientation.quaternion(0, 0, Double.MIN_VALUE, 0);
        assertThat(quaternion.normalize(TOLERANCE), is(zero));
    }

    private void assertQuaternionEquality(final Orientation quaternion1, final Orientation quaternion2) {
        assertQuaternionEquality(quaternion1, quaternion2, true);
    }

    private void assertMatrixEquality(SimpleMatrix actual, SimpleMatrix expected) {
        String error = "Actual:\n" + actual.toString() + "Expected:\n" + expected;
        assertThat(error, actual.isIdentical(expected, TOLERANCE), is(true));
    }

    private void assertQuaternionEquality(final Orientation quaternion1, final Orientation quaternion2, final boolean expected) {
        String message = "\nActual:\n" + quaternion1 + "\nExpected:\n" + quaternion2;
        assertThat(message, quaternion1.isIdentical(quaternion2, TOLERANCE), is(expected));
        assertThat(message, quaternion2.isIdentical(quaternion1, TOLERANCE), is(expected));
    }
}
