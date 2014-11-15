package com.aimlessblade.cosmos.physics;

import com.aimlessblade.cosmos.util.Assert;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DisplacementTest {
    private static final double TOLERANCE = 1e-5;
    private static final Displacement ORIGIN = Vectors.position(0, 0, 0);

    @Test
    public void positionsShouldBeIdenticalIfWithinTolerance() {
        Displacement nearOrigin = Vectors.position(0.000001, -0.000001, 0.000009);
        assertThat(ORIGIN.isIdentical(nearOrigin, TOLERANCE), is(true));
    }

    @Test
    public void positionsShouldNotBeIdenticalIfXIsDifferent() {
        Displacement displacement = Vectors.position(0.0001, 0, 0);
        assertThat(ORIGIN.isIdentical(displacement, TOLERANCE), is(false));
        assertThat(displacement.isIdentical(ORIGIN, TOLERANCE), is(false));
    }

    @Test
    public void positionsShouldNotBeIdenticalIfYIsDifferent() {
        Displacement displacement = Vectors.position(0.0, 0.0001, 0);
        assertThat(ORIGIN.isIdentical(displacement, TOLERANCE), is(false));
        assertThat(displacement.isIdentical(ORIGIN, TOLERANCE), is(false));
    }

    @Test
    public void positionsShouldNotBeIdenticalIfZIsDifferent() {
        Displacement displacement = Vectors.position(0, 0, 0.0001);
        assertThat(ORIGIN.isIdentical(displacement, TOLERANCE), is(false));
        assertThat(displacement.isIdentical(ORIGIN, TOLERANCE), is(false));
    }

    @Test
    public void translationMatrixShouldBeIdentityIfNoOffset() {
        Assert.assertMatrixEquality(ORIGIN.toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void translationMatrixShouldHaveOffsetComponentsInLastColumn() {
        Displacement displacement = Vectors.position(2, 3, 5);

        SimpleMatrix expectedMatrix = SimpleMatrix.identity(4);
        expectedMatrix.set(0, 3, 2);
        expectedMatrix.set(1, 3, 3);
        expectedMatrix.set(2, 3, 5);

        Assert.assertMatrixEquality(displacement.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void addingZeroToADisplacementShouldDoNothing() {
        Displacement displacement = Vectors.position(2, 5, 8);
        assertThat(displacement.plus(ORIGIN), is(displacement));
    }

    @Test
    public void addingDisplacementsShouldAddTheirComponents() {
        Displacement first = Vectors.position(2, 5, 8);
        Displacement second = Vectors.position(1, 11, 4);
        Displacement expectedSum = Vectors.position(3, 16, 12);

        assertThat(first.plus(second), is(expectedSum));
    }
}