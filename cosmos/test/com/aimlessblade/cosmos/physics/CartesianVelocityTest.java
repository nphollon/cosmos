package com.aimlessblade.cosmos.physics;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CartesianVelocityTest {
    @Test
    public void velocityOverTimeShouldEqualDisplacement() {
        Velocity original = Vectors.velocity(-1, 0.5, 3);
        Displacement expectedResult = Vectors.position(-8, 4, 24);
        assertThat(original.overTime(8), is(expectedResult));
    }

    @Test
    public void sumOfVelocitiesShouldBeSumOfComponents() {
        Velocity velocityA = Vectors.velocity(4, 3, 2);
        Velocity velocityB = Vectors.velocity(1.1, 2.1, 3.8);
        Velocity expectedSum = Vectors.velocity(5.1, 5.1, 5.8);
        assertThat(velocityA.plus(velocityB), is(expectedSum));
    }
}