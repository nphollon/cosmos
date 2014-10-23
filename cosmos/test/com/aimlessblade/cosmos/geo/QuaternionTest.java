package com.aimlessblade.cosmos.geo;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class QuaternionTest {

    private static final double TOLERANCE = 1e-5;

    @Test
    public void rotationMatrixShouldBeIdentityIfWIsZero() {
        Quaternion quaternion = Quaternion.axisAngleDegrees(1.0, 0.0, 0.0, 0.0);

        assertMatrixEquality(quaternion.toMatrix(), SimpleMatrix.identity(4));
    }

    @Test
    public void rotationMatrixShouldChangePitchIfAxisIsX() {
        Quaternion quaternion = Quaternion.axisAngleDegrees(1.0, 0.0, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 0, 1);
        expectedMatrix.set(1, 2, -1);
        expectedMatrix.set(2, 1, 1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldChangeYawIfAxisIsY() {
        Quaternion quaternion = Quaternion.axisAngleDegrees(0.0, 1.0, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 2, 1);
        expectedMatrix.set(1, 1, 1);
        expectedMatrix.set(2, 0, -1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldChangeRollIfAxisIsZ() {
        Quaternion quaternion = Quaternion.axisAngleDegrees(0.0, 0.0, 1.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 1, -1);
        expectedMatrix.set(1, 0, 1);
        expectedMatrix.set(2, 2, 1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotationMatrixShouldRotateAroundXZDiagonal() {
        Quaternion quaternion = Quaternion.axisAngleDegrees(0.707107, 0.0, 0.707107, 90.0);

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
        Quaternion quaternion = Quaternion.axisAngleDegrees(0.707107, 0.707107, 0.0, 90.0);

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
        Quaternion quaternion = Quaternion.axisAngleDegrees(0.0, 0.707107, 0.707107, 90.0);

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
        Quaternion quaternion = Quaternion.axisAngleDegrees(0.632456, 0.6, 0.2, 10.0);

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
        Quaternion degreeQuaternion = Quaternion.axisAngleDegrees(1, 0, 0, 180);
        Quaternion radianQuaternion = Quaternion.axisAngle(1, 0, 0, Math.PI);

        assertThat(degreeQuaternion, is(radianQuaternion));
    }

    @Test
    public void isIdenticalShouldBeTrueForExactlyEqualQuaternions() {
        Quaternion quaternion1 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        Quaternion quaternion2 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, true);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Quaternion quaternion1 = Quaternion.axisAngleDegrees(-0.000009, 1.000009, 0.000009, 11.999999);
        Quaternion quaternion2 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, true);
    }

    @Test
    public void isIdenticalShouldBeFalseIfXsAreDifferent() {
        Quaternion quaternion1 = Quaternion.axisAngleDegrees(0.0001, 1, 0, 12);
        Quaternion quaternion2 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfYsAreDifferent() {
        Quaternion quaternion1 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        Quaternion quaternion2 = Quaternion.axisAngleDegrees(0, 0.999, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfZsAreDifferent() {
        Quaternion quaternion1 = Quaternion.axisAngleDegrees(0, 1, 0.0001, 12);
        Quaternion quaternion2 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfWsAreDifferent() {
        // Because the quaternion components are products of the factory parameters
        //   very different quaternions can have identical Y parameters
        Quaternion quaternion1 = Quaternion.axisAngleDegrees(0, 1, 0, 12);
        Quaternion quaternion2 = Quaternion.axisAngleDegrees(0, 0.104528, 0, 180);
        assertQuaternionEquality(quaternion1, quaternion2, false);
    }

    @Test
    public void productXXShouldGoToW() {
        Quaternion factorA = Quaternion.components(2, 0, 0, 0);
        Quaternion factorB = Quaternion.components(3, 0, 0, 0);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 0, 0, -6));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 0, 0, -6));
    }

    @Test
    public void productXYShouldGoToZ() {
        Quaternion factorA = Quaternion.components(2, 0, 0, 0);
        Quaternion factorB = Quaternion.components(0, 3, 0, 0);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 0, 6, 0));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 0, -6, 0));
    }

    @Test
    public void productXZShouldGoToY() {
        Quaternion factorA = Quaternion.components(2, 0, 0, 0);
        Quaternion factorB = Quaternion.components(0, 0, 3, 0);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, -6, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 6, 0, 0));
    }

    @Test
    public void productXWShouldGoToX() {
        Quaternion factorA = Quaternion.components(2, 0, 0, 0);
        Quaternion factorB = Quaternion.components(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(6, 0, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(6, 0, 0, 0));
    }

    @Test
    public void productYYShouldGoToW() {
        Quaternion factorA = Quaternion.components(0, 2, 0, 0);
        Quaternion factorB = Quaternion.components(0, 3, 0, 0);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 0, 0, -6));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 0, 0, -6));
    }

    @Test
    public void productYZShouldGoToX() {
        Quaternion factorA = Quaternion.components(0, 2, 0, 0);
        Quaternion factorB = Quaternion.components(0, 0, 3, 0);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(6, 0, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(-6, 0, 0, 0));
    }

    @Test
    public void productYWShouldGoToY() {
        Quaternion factorA = Quaternion.components(0, 2, 0, 0);
        Quaternion factorB = Quaternion.components(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 6, 0, 0));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 6, 0, 0));
    }

    @Test
    public void productZZShouldGoToW() {
        Quaternion factorA = Quaternion.components(0, 0, 2, 0);
        Quaternion factorB = Quaternion.components(0, 0, 3, 0);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 0, 0, -6));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 0, 0, -6));
    }

    @Test
    public void productZWShouldGoToZ() {
        Quaternion factorA = Quaternion.components(0, 0, 2, 0);
        Quaternion factorB = Quaternion.components(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 0, 6, 0));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 0, 6, 0));
    }

    @Test
    public void productWWShouldGoToW() {
        Quaternion factorA = Quaternion.components(0, 0, 0, 2);
        Quaternion factorB = Quaternion.components(0, 0, 0, 3);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(0, 0, 0, 6));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(0, 0, 0, 6));
    }

    @Test
    public void productShouldBeCompositeOfAllComponents() {
        Quaternion factorA = Quaternion.components(2, 5, 11, 17);
        Quaternion factorB = Quaternion.components(19, 13, 7, 3);
        assertQuaternionEquality(factorA.times(factorB), Quaternion.components(221, 431, 83, -129));
        assertQuaternionEquality(factorB.times(factorA), Quaternion.components(437, 41, 221, -129));
    }

    @Test
    public void normalizeShouldChangeMagnitudeToOneIfItIsNotOne() {
        Quaternion quaternion = Quaternion.components(0.5, 0.01, -0.5, 0.3);
        Quaternion normalizedQuaternion = Quaternion.components(0.650889, 0.013018, -0.650889, 0.390534);
        assertQuaternionEquality(quaternion.normalize(TOLERANCE), normalizedQuaternion);
    }

    @Test
    public void normalizeShouldNotChangeQuaternionIfCloseToOne() {
        Quaternion quaternion = Quaternion.components(1.0000001, 0.000001, 0, 0);
        assertThat(quaternion.normalize(TOLERANCE), is(quaternion));
    }

    private void assertQuaternionEquality(final Quaternion quaternion1, final Quaternion quaternion2) {
        assertQuaternionEquality(quaternion1, quaternion2, true);
    }

    private void assertMatrixEquality(SimpleMatrix actual, SimpleMatrix expected) {
        String error = "Actual:\n" + actual.toString() + "Expected:\n" + expected;
        assertThat(error, actual.isIdentical(expected, TOLERANCE), is(true));
    }

    private void assertQuaternionEquality(final Quaternion quaternion1, final Quaternion quaternion2, final boolean expected) {
        String message = "\nActual:\n" + quaternion1 + "\nExpected:\n" + quaternion2;
        assertThat(message, quaternion1.isIdentical(quaternion2, TOLERANCE), is(expected));
        assertThat(message, quaternion2.isIdentical(quaternion1, TOLERANCE), is(expected));
    }
}
