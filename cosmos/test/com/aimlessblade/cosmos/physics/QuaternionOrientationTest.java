package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;

public class QuaternionOrientationTest {

    private Orientation zero() {
        return new QuaternionOrientation(Vectors.position(0, 0, 0));
    }

    @Test
    public void rotationMatrixShouldBeIdentityIfNullRotation() {
        assertMatrixEquality(zero().toMatrix(), SimpleMatrix.identity(4));
    }
}