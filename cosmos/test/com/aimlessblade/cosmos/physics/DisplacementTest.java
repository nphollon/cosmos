package com.aimlessblade.cosmos.physics;

import com.aimlessblade.cosmos.util.Assert;
import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DisplacementTest {
    private static final double TOLERANCE = 1e-5;
    private static final Displacement ORIGIN = Displacement.cartesian(0, 0, 0);

    @Test
    public void positionsShouldBeIdenticalIfWithinTolerance() {
        Displacement nearOrigin = Displacement.cartesian(0.000001, -0.000001, 0.000009);
        assertThat(ORIGIN.isIdentical(nearOrigin, TOLERANCE), is(true));
    }

    @Test
    public void positionsShouldNotBeIdenticalIfXIsDifferent() {
        Displacement displacement = Displacement.cartesian(0.0001, 0, 0);
        assertThat(ORIGIN.isIdentical(displacement, TOLERANCE), is(false));
        assertThat(displacement.isIdentical(ORIGIN, TOLERANCE), is(false));
    }

    @Test
    public void positionsShouldNotBeIdenticalIfYIsDifferent() {
        Displacement displacement = Displacement.cartesian(0.0, 0.0001, 0);
        assertThat(ORIGIN.isIdentical(displacement, TOLERANCE), is(false));
        assertThat(displacement.isIdentical(ORIGIN, TOLERANCE), is(false));
    }

    @Test
    public void positionsShouldNotBeIdenticalIfZIsDifferent() {
        Displacement displacement = Displacement.cartesian(0, 0, 0.0001);
        assertThat(ORIGIN.isIdentical(displacement, TOLERANCE), is(false));
        assertThat(displacement.isIdentical(ORIGIN, TOLERANCE), is(false));
    }

    @Test
    public void translationMatrixShouldBeIdentityIfNoOffset() {
        Assert.assertMatrixEquality(ORIGIN.toMatrix(), SimpleMatrix.identity(4), TOLERANCE);
    }

    @Test
    public void translationMatrixShouldHaveOffsetComponentsInLastColumn() {
        Displacement displacement = Displacement.cartesian(2, 3, 5);

        SimpleMatrix expectedMatrix = SimpleMatrix.identity(4);
        expectedMatrix.set(0, 3, 2);
        expectedMatrix.set(1, 3, 3);
        expectedMatrix.set(2, 3, 5);

        Assert.assertMatrixEquality(displacement.toMatrix(), expectedMatrix, TOLERANCE);
    }

    @Test
    public void addingZeroToADisplacementShouldDoNothing() {
        Displacement displacement = Displacement.cartesian(2, 5, 8);
        assertThat(displacement.plus(ORIGIN), is(displacement));
    }

    @Test
    public void addingDisplacementsShouldAddTheirComponents() {
        Displacement first = Displacement.cartesian(2, 5, 8);
        Displacement second = Displacement.cartesian(1, 11, 4);
        Displacement expectedSum = Displacement.cartesian(3, 16, 12);

        assertThat(first.plus(second), is(expectedSum));
    }
}