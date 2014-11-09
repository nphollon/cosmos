package com.aimlessblade.cosmos.geo;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PoseTest {
    private static final Displacement DISPLACEMENT = Displacement.cartesian(1, 2, 3);
    private static final Orientation ORIENTATION = Orientation.axisAngle(1, 0, 0, 90);
    private static final double TOLERANCE = 1e-5;

    private Pose pose;

    @Before
    public void setup() {
        pose = Pose.build(DISPLACEMENT, ORIENTATION);
    }

    @Test
    public void placingAMovableShouldGiveItADisplacementAndOrientation() {
        assertThat(pose.getDisplacement(), is(DISPLACEMENT));
        assertThat(pose.getOrientation(), is(ORIENTATION));
    }

}
