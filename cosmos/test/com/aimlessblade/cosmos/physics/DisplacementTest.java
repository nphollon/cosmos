package com.aimlessblade.cosmos.physics;

import org.ejml.simple.SimpleMatrix;
import org.junit.Test;

import static com.aimlessblade.cosmos.physics.Identity.assertMatrixEquality;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DisplacementTest {
    private static final Displacement ORIGIN = Vectors.position(0, 0, 0);

    @Test
    public void translationMatrixShouldBeIdentityIfNoOffset() {
        assertMatrixEquality(ORIGIN.toMatrix(), SimpleMatrix.identity(4));
    }

    @Test
    public void translationMatrixShouldHaveOffsetComponentsInLastColumn() {
        Displacement displacement = Vectors.position(2, 3, 5);

        SimpleMatrix expectedMatrix = SimpleMatrix.identity(4);
        expectedMatrix.set(0, 3, 2);
        expectedMatrix.set(1, 3, 3);
        expectedMatrix.set(2, 3, 5);

        assertMatrixEquality(displacement.toMatrix(), expectedMatrix);
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