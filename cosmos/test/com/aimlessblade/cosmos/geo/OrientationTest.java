package com.aimlessblade.cosmos.geo;

import com.aimlessblade.cosmos.util.Assert;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static com.aimlessblade.cosmos.geo.Orientation.*;
import static com.aimlessblade.cosmos.util.Assert.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrientationTest {

    private static final double TOLERANCE = 1e-5;

    @Test
    public void quaternionFactoryWithRadiansShouldMatchFactoryWithDegrees() {
        Orientation degreeQuaternion = axisAngle(1, 0, 0, 180);
        Orientation radianQuaternion = axisAngleRadians(1, 0, 0, Math.PI);

        assertThat(degreeQuaternion, is(radianQuaternion));
    }

    @Test
    public void rotationVectorFactoryShouldConvertMagnitudeToRotationAngle() {
        assertThat(rotationVector(3, 0, 0), is(axisAngle(1, 0, 0, 3)));
        assertThat(rotationVector(0, 4, 0), is(axisAngle(0, 1, 0, 4)));
        assertThat(rotationVector(0, 0, 2), is(axisAngle(0, 0, 1, 2)));
        assertThat(rotationVector(3, 0, 4), is(axisAngle(0.6, 0, 0.8, 5)));
    }

    @Test
    public void nullVectorShouldEqualNullRotation() {
        assertThat(rotationVector(0, 0, 0), is(axisAngle(1, 0, 0, 0)));
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfWIsZero() {
        Orientation quaternion = axisAngle(1.0, 0.0, 0.0, 0.0);

        assertMatrixEquality(quaternion.toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldChangePitchIfAxisIsX() {
        Orientation quaternion = axisAngle(1.0, 0.0, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 0, 1);
        expectedMatrix.set(1, 2, -1);
        expectedMatrix.set(2, 1, 1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldChangeYawIfAxisIsY() {
        Orientation quaternion = axisAngle(0.0, 1.0, 0.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 2, 1);
        expectedMatrix.set(1, 1, 1);
        expectedMatrix.set(2, 0, -1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldChangeRollIfAxisIsZ() {
        Orientation quaternion = axisAngle(0.0, 0.0, 1.0, 90.0);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        expectedMatrix.set(0, 1, -1);
        expectedMatrix.set(1, 0, 1);
        expectedMatrix.set(2, 2, 1);
        expectedMatrix.set(3, 3, 1);

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldRotateAroundXZDiagonal() {
        Orientation quaternion = axisAngle(0.707107, 0.0, 0.707107, 90.0);

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

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldRotateAroundXYDiagonal() {
        Orientation quaternion = axisAngle(0.707107, 0.707107, 0.0, 90.0);

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

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void rotationMatrixShouldRotateAroundYZDiagonal() {
        Orientation quaternion = axisAngle(0.0, 0.707107, 0.707107, 90.0);

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

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix, TOLERANCE);
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
        Orientation quaternion1 = axisAngle(0, 1, 0, 12);
        Orientation quaternion2 = axisAngle(0, 1, 0, 12);
        Assert.assertQuaternionEquality(quaternion1, quaternion2, TOLERANCE, true);
    }

    @Test
    public void isIdenticalShouldBeTrueForQuaternionsWithinTolerance() {
        Orientation quaternion1 = axisAngle(-0.000009, 1.000009, 0.000009, 11.999999);
        Orientation quaternion2 = axisAngle(0, 1, 0, 12);
        Assert.assertQuaternionEquality(quaternion1, quaternion2, TOLERANCE, true);
    }

    @Test
    public void isIdenticalShouldBeFalseIfXsAreDifferent() {
        Orientation quaternion1 = axisAngle(0.0001, 1, 0, 12);
        Orientation quaternion2 = axisAngle(0, 1, 0, 12);
        Assert.assertQuaternionEquality(quaternion1, quaternion2, TOLERANCE, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfYsAreDifferent() {
        Orientation quaternion1 = axisAngle(0, 1, 0, 12);
        Orientation quaternion2 = axisAngle(0, 0.999, 0, 12);
        Assert.assertQuaternionEquality(quaternion1, quaternion2, TOLERANCE, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfZsAreDifferent() {
        Orientation quaternion1 = axisAngle(0, 1, 0.0001, 12);
        Orientation quaternion2 = axisAngle(0, 1, 0, 12);
        Assert.assertQuaternionEquality(quaternion1, quaternion2, TOLERANCE, false);
    }

    @Test
    public void isIdenticalShouldBeFalseIfWsAreDifferent() {
        // Because the quaternion components are products of the factory parameters
        //   very different quaternions can have identical Y components
        Orientation quaternion1 = axisAngle(0, 1, 0, 12);
        Orientation quaternion2 = axisAngle(0, 0.104528, 0, 180);
        Assert.assertQuaternionEquality(quaternion1, quaternion2, TOLERANCE, false);
    }

    @Test
    public void productXXShouldGoToW() {
        Orientation factorA = quaternion(2, 0, 0, 0);
        Orientation factorB = quaternion(3, 0, 0, 0);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 0, 0, -6), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 0, 0, -6), TOLERANCE);
    }

    @Test
    public void productXYShouldGoToZ() {
        Orientation factorA = quaternion(2, 0, 0, 0);
        Orientation factorB = quaternion(0, 3, 0, 0);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 0, 6, 0), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 0, -6, 0), TOLERANCE);
    }

    @Test
    public void productXZShouldGoToY() {
        Orientation factorA = quaternion(2, 0, 0, 0);
        Orientation factorB = quaternion(0, 0, 3, 0);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, -6, 0, 0), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 6, 0, 0), TOLERANCE);
    }

    @Test
    public void productXWShouldGoToX() {
        Orientation factorA = quaternion(2, 0, 0, 0);
        Orientation factorB = quaternion(0, 0, 0, 3);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(6, 0, 0, 0), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(6, 0, 0, 0), TOLERANCE);
    }

    @Test
    public void productYYShouldGoToW() {
        Orientation factorA = quaternion(0, 2, 0, 0);
        Orientation factorB = quaternion(0, 3, 0, 0);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 0, 0, -6), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 0, 0, -6), TOLERANCE);
    }

    @Test
    public void productYZShouldGoToX() {
        Orientation factorA = quaternion(0, 2, 0, 0);
        Orientation factorB = quaternion(0, 0, 3, 0);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(6, 0, 0, 0), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(-6, 0, 0, 0), TOLERANCE);
    }

    @Test
    public void productYWShouldGoToY() {
        Orientation factorA = quaternion(0, 2, 0, 0);
        Orientation factorB = quaternion(0, 0, 0, 3);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 6, 0, 0), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 6, 0, 0), TOLERANCE);
    }

    @Test
    public void productZZShouldGoToW() {
        Orientation factorA = quaternion(0, 0, 2, 0);
        Orientation factorB = quaternion(0, 0, 3, 0);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 0, 0, -6), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 0, 0, -6), TOLERANCE);
    }

    @Test
    public void productZWShouldGoToZ() {
        Orientation factorA = quaternion(0, 0, 2, 0);
        Orientation factorB = quaternion(0, 0, 0, 3);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 0, 6, 0), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 0, 6, 0), TOLERANCE);
    }

    @Test
    public void productWWShouldGoToW() {
        Orientation factorA = quaternion(0, 0, 0, 2);
        Orientation factorB = quaternion(0, 0, 0, 3);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(0, 0, 0, 6), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(0, 0, 0, 6), TOLERANCE);
    }

    @Test
    public void productShouldBeCompositeOfAllComponents() {
        Orientation factorA = quaternion(2, 5, 11, 17);
        Orientation factorB = quaternion(19, 13, 7, 3);
        Assert.assertQuaternionEquality(factorA.times(factorB), quaternion(221, 431, 83, -129), TOLERANCE);
        Assert.assertQuaternionEquality(factorB.times(factorA), quaternion(437, 41, 221, -129), TOLERANCE);
    }

    @Test
    public void normalizeShouldChangeMagnitudeToOneIfItIsNotOne() {
        Orientation quaternion = quaternion(0.5, 0.01, -0.5, 0.3);
        Orientation normalizedQuaternion = quaternion(0.650889, 0.013018, -0.650889, 0.390534);
        Assert.assertQuaternionEquality(quaternion.normalize(TOLERANCE), normalizedQuaternion, TOLERANCE);
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
}
