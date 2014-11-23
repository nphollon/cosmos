package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;
import org.junit.Ignore;
import org.junit.Test;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;

public class QuaternionOrientationTest {

    @Test
    public void nullRotationShouldHaveWComponentOf1() {
        final QuaternionOrientation nullRotation = new QuaternionOrientation(Vectors.position(0, 0, 0));
        final QuaternionOrientation expected = new QuaternionOrientation(0, 0, 0, 1);
        assertMatrixEquality(nullRotation, expected);
    }

    @Test
    public void quaternionShouldBeNormalizedWhenBuildFromComponents() {
        Orientation unnormalized = new QuaternionOrientation(3, 4, 12, 84);
        Orientation normalized = new QuaternionOrientation(3 / 85.0, 4 / 85.0, 12 / 85.0, 84 / 85.0);

        assertMatrixEquality(unnormalized, normalized);
    }

    @Test
    public void quaternionComponentsShouldMatchRotationVector() {
        Orientation fromVector = new QuaternionOrientation(Vectors.position(3, 4, 12));
        Orientation fromComponents = new QuaternionOrientation(
                3 / 13.0 * Math.sin(13 * 0.5),
                4 / 13.0 * Math.sin(13 * 0.5),
                12 / 13.0 * Math.sin(13 * 0.5),
                Math.cos(13 * 0.5)
        );

        assertMatrixEquality(fromVector, fromComponents);
    }

    @Test
    public void matrixShouldBeConvertedFromQuaternionComponents() {
        final double x = 84;
        final double y = 4;
        final double z = 3;
        final double w = 12;
        final double magSq = 85 * 85;
        Orientation quaternion = new QuaternionOrientation(x, y, z, w);

        SimpleMatrix expectedMatrix = new SimpleMatrix(4, 4);
        // Diagonal components
        expectedMatrix.set(0, 0, 1 - 2 / magSq * (y * y + z * z));
        expectedMatrix.set(1, 1, 1 - 2 / magSq * (z * z + x * x));
        expectedMatrix.set(2, 2, 1 - 2 / magSq * (x * x + y * y));
        expectedMatrix.set(3, 3, 1);
        // Upper half
        expectedMatrix.set(0, 1, 2 / magSq * (x * y - z * w));
        expectedMatrix.set(0, 2, 2 / magSq * (z * x + y * w));
        expectedMatrix.set(1, 2, 2 / magSq * (y * z - x * w));
        // Lower half
        expectedMatrix.set(1, 0, 2 / magSq * (x * y + z * w));
        expectedMatrix.set(2, 0, 2 / magSq * (z * x - y * w));
        expectedMatrix.set(2, 1, 2 / magSq * (y * z + x * w));

        assertMatrixEquality(quaternion.toMatrix(), expectedMatrix);
    }

    @Test
    public void rotateShouldMultiplyQuaternions() {
        Orientation initialOrientation = new QuaternionOrientation(Vectors.position(1, 2, 3));
        Displacement rotation = Vectors.position(0.5, 0.6, 0.7);
        Orientation expectedFinalOrientation =
                new QuaternionOrientation(0.199220668760, 0.25985578148, 0.61302625167, -0.71901662896);

        assertMatrixEquality(initialOrientation.rotate(rotation), expectedFinalOrientation);
    }
}