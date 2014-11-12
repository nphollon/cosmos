package com.aimlessblade.cosmos.geo;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VelocityTest {
    @Test
    public void velocityOverTimeShouldEqualDisplacement() {
        Velocity original = Velocity.cartesian(-1, 0.5, 3);
        Displacement expectedResult = Displacement.cartesian(-8, 4, 24);
        assertThat(original.overTime(8), is(expectedResult));
    }
}